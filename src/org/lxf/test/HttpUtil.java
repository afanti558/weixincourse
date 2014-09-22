//package org.lxf.test;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ConnectException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//
//import org.lxf.weixin.util.MyX509TrustManager;
//import org.lxf.weixin.util.WeixinUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import net.sf.json.JSONObject;
//
//public class HttpUtil {
//	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
//	
//	public static JSONObject httpRequests(String requestUrl, String requestMethod, String outputStr) {
//		//用于返回
//		JSONObject jsonObject = null;
//		StringBuffer buffer = new StringBuffer();
//		try {
//
//			URL url = new URL(requestUrl);
//			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//			System.out.println("\n\nhttpUrlConn::"+httpUrlConn+"\n\n");
//			httpUrlConn.setDoOutput(true);
//			httpUrlConn.setDoInput(true);
//			httpUrlConn.setUseCaches(false);
//			// 设置请求方式（GET/POST）
//			httpUrlConn.setRequestMethod(requestMethod);
//			// 当有数据需要提交时
//			if (null != outputStr) {
//				OutputStream outputStream = httpUrlConn.getOutputStream();
//				// 注意参数编码格式，防止中文乱码
//				outputStream.write(outputStr.getBytes("UTF-8"));
//				outputStream.close();
//			}
//			// 将返回的输入流转换成字符串
//			InputStream inputStream = httpUrlConn.getInputStream();
//			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//			String str = null;
//			while ((str = bufferedReader.readLine()) != null) {
//				buffer.append(str);
//			}
//			bufferedReader.close();
//			inputStreamReader.close();
//			// 释放资源
//			inputStream.close();
//			inputStream = null;
//			httpUrlConn.disconnect();
//			jsonObject = JSONObject.fromObject(buffer.toString());
//		} catch (ConnectException ce) {
//			log.error("Weixin server connection timed out.");
//		} catch (Exception e) {
//			log.error("https request error:{}", e);
//		}
//		return jsonObject;
//	}
//	
//	public JSONObject httpRequests(String requestUrl,String requestMethod) throws Exception{
//		JSONObject jsonObject = null;
//		URL url = new URL(requestUrl);
//		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//		httpUrlConn.setDoInput(true);
//		httpUrlConn.setRequestMethod(requestMethod);
//		httpUrlConn.connect();
//		
//		
//		return jsonObject;
//	}
//	
//	public static void main(String args[]){
//		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx65d73e5e3d662b5d&"
//				+ "secret=f628212e882ee6a7aa984906e190dc50";
//		JSONObject jsonObject = httpRequests(url,"POST",null);
//		System.out.println(jsonObject.toString());
//	}
//}
