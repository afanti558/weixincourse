package org.lxf.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.pojo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 公众平台通用接口工具类
 * 
 * @author liuyq
 * @date 2013-08-09
 */
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 媒体文件上传（POST/FORM）
	public static String MEDIA_UPLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	/**
	 * 创建菜单
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static String createMenu(Menu menu, String accessToken) {
		String result = "";
		// 拼装创建菜单的url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.toJSONString(menu);
		// 调用接口创建菜单
		JSONObject jsonObject = httpsRequests(url, "POST", jsonMenu);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				result = "创建菜单失败:" + jsonObject.getString("errcode") + "," + jsonObject.getString("errmsg");
				log.info("创建菜单失败:", jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
			}
			result = "创建菜单成功.";
			log.info("创建菜单成功.");
		}
		return result;
	}
	
	/**
	 * 获取access_token
	 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpsRequests(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			if(jsonObject.getString("errcode") == null){//没有错误码返回说明返回成功
				accessToken = JSONObject.toJavaObject(jsonObject, AccessToken.class);
			}else{
				log.info("获取accessToken失败," + jsonObject.getString("errcode") + "," + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequests(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setSSLSocketFactory(ssf);

			httpsUrlConn.setDoOutput(true);
			httpsUrlConn.setDoInput(true);
			httpsUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpsUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpsUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpsUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpsUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpsUrlConn.disconnect();
			//json格式字符串转换为json对象
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("Weixin server connection timed out.");
		} catch (Exception e) {
			System.out.println("https request error:{}" + e);
		}
		return jsonObject;
	}
	
	/**
     * 发起http POST请求调用文件接口接口实现多媒体文件的上传
     * @requestUrl 请求的URL
     * @param fileType 文件类型
     * @param filePath 文件路径
     * @return JSONObject 返回的json格式结果
     * @throws Exception
     */
    public static JSONObject httpRequests(String requestUrl, String fileType, String filePath) throws Exception {  
    	JSONObject jsonObject = null;
        String result = null;  
        File file = new File(filePath);  
        if (!file.exists() || !file.isFile()) {  
            System.out.println("文件不存在");  
        }  
        
        URL url = new URL(requestUrl);  
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
        httpUrlConn.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式  
        httpUrlConn.setDoInput(true);  
        httpUrlConn.setDoOutput(true);  
        httpUrlConn.setUseCaches(false); // post方式不能使用缓存  
        // 设置请求头信息  
        httpUrlConn.setRequestProperty("Connection", "Keep-Alive");  
        httpUrlConn.setRequestProperty("Charset", "UTF-8");  
        // 设置边界  
        String BOUNDARY = ""+System.currentTimeMillis();  //随机生成数字或字母
        httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
        // 开头部分
        StringBuilder sb = new StringBuilder();  
        sb.append("--"); // 必须多两道线  
        sb.append(BOUNDARY);  
        sb.append("\r\n");  //回车换行System.getProperty("line.separator")
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");  
        sb.append("Content-Type:application/octet-stream\r\n\r\n"); 
        System.out.print(sb);
        byte[] head = sb.toString().getBytes("utf-8");  
        OutputStream out = new DataOutputStream(httpUrlConn.getOutputStream());  
        out.write(head);  
        // 文件正文部分     把文件以流文件的方式 推入到url中  
        DataInputStream in = new DataInputStream(new FileInputStream(file));  
        int bytes = 0;  
        byte[] bufferOut = new byte[1024];  
        while ((bytes = in.read(bufferOut)) != -1) {  
        	out.write(bufferOut, 0, bytes);  
        }  
        in.close();  
        System.out.print("推送的正文部分，以流的方式");
        // 结尾部分  
        byte[] foot = ("\r\n--" + BOUNDARY).getBytes("utf-8");// 定义最后数据分隔线  
        System.out.print("\r\n--" + BOUNDARY);
        out.write(foot);  
        out.flush();  
        out.close();  
        StringBuffer buffer = new StringBuffer();  
        BufferedReader reader = null;  
        try {  
	        // 定义BufferedReader输入流来读取URL的响应  
	        reader = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));  
	        String line = null;  
	        while ((line = reader.readLine()) != null) {  
		        buffer.append(line);  
	        }  
	        if(result==null){  
	        	result = buffer.toString();  
	        }  
        } catch (IOException e) {  
	        System.out.println("发送POST请求出现异常！" + e);  
	        e.printStackTrace();  
	        throw new IOException("数据读取异常");  
        } finally {  
	        if(reader!=null){  
	        	reader.close();  
	        }  
        } 
        jsonObject = (JSONObject) JSONObject.parseObject(buffer.toString());
        return jsonObject;  
    }
    
    //Test
    public static void main(String args[]) throws Exception{
//    	System.out.println("获取到的accessToken:" + getAccessToken("wx65d73e5e3d662b5d","f628212e882ee6a7aa984906e190dc50"));
    	String requestUrl = MEDIA_UPLOAD_URL.replace("ACCESS_TOKEN", "p-cPV7BkPukGkq7q-h0HFoyGNnDsUwvb-HIJaNIS1KTLCu5i4p4meklwCJ98pdUe4iiKSOFC-WZIuodvJjs9Tw")
    			.replace("TYPE", "image");
    	
    	System.out.println("\r\n" + httpRequests(requestUrl,"mp3","G:\\hello.mp3"));
    }
}
