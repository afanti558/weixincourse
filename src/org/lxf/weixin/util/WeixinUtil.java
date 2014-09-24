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

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.pojo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 公众平台通用接口工具类
 * 
 * @author liuyq
 * @date 2013-08-09
 */
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 媒体文件上传（POST/FORM）
	public static String media_upload_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		log.info("开始创建菜单");
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		log.info("*********"+jsonMenu);
		
		// 调用接口创建菜单
		log.info("调用接口发起POST请求");
		JSONObject jsonObject = httpsRequests(url, "POST", jsonMenu);
//		JSONObject jsonObject = null;
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
			log.info("创建菜单成功");
		}
		return result;
	}
	
	/**
	 * 获取access_token
	 * 
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		log.info("开始获取token");
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		log.info("调用接口发起GET请求");
		JSONObject jsonObject = httpsRequests(requestUrl, "GET", null);
//		JSONObject jsonObject = null;
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		log.info("获取token成功："+accessToken);
		return accessToken;
	}
	
	
	/**
	 * 发起https请求并获取结果
	 * 
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
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
	/**
     * 发起http请求调用文件接口接口
     * @requestUrl 请求的URL
     * @param fileType 文件类型
     * @param filePath 文件路径
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject httpRequests(String requestUrl, String fileType, String filePath) throws Exception {  
    	JSONObject jsonObject = null;
        String result = null;  
        File file = new File(filePath);  
        if (!file.exists() || !file.isFile()) {  
            throw new IOException("文件不存在");  
        }  
        /** 
        * 第一部分 
        */  
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
        String BOUNDARY = "----------" + System.currentTimeMillis();  
        httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
        // 请求正文信息  
        // 第一部分：  
        StringBuilder sb = new StringBuilder();  
        sb.append("--"); // 必须多两道线  
        sb.append(BOUNDARY);  
        sb.append("\r\n");  
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");  
        sb.append("Content-Type:application/octet-stream\r\n\r\n");  
        byte[] head = sb.toString().getBytes("utf-8");  
        // 获得输出流  
        OutputStream out = new DataOutputStream(httpUrlConn.getOutputStream());  
        // 输出表头  
        out.write(head);  
        // 文件正文部分  
        // 把文件以流文件的方式 推入到url中  
        DataInputStream in = new DataInputStream(new FileInputStream(file));  
        int bytes = 0;  
        byte[] bufferOut = new byte[1024];  
        while ((bytes = in.read(bufferOut)) != -1) {  
        	out.write(bufferOut, 0, bytes);  
        }  
        in.close();  
        // 结尾部分  
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
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
		        //System.out.println(line);  
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
        jsonObject = JSONObject.fromObject(buffer.toString());  
        return jsonObject;  
    }
    
    public static void main(String args[]) throws Exception{
//    	public static String media_upload_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    	String requestUrl = media_upload_url.replace("ACCESS_TOKEN", "dZO-wSB7St1B7SjzplzZ23gN_SAV2tvrlE_c8DRQ4FkN5C-ueYN_vG0s7sqZMziZJG4rO3gvqrnWtFeRxs850g").replace("TYPE", "voice");
    	String filePath = "G:"+File.separator+"hello.mp3";
    	String fileType = "mp3";
    	JSONObject jsonObject = httpRequests(requestUrl, fileType, filePath);
    	System.out.println("*****"+jsonObject.toString());
    }
}
