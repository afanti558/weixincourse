package org.lxf.test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class HttpHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

	/**
	 * 返回http请求得到的数据
	 * @param requrl 请求路径
	 * @param reqMethod 请求方法
	 * @param params 请求参数(json格式，如果没有为null)
	 * @return
	 */
	public static String getHttpRequestResults(String requrl,String reqMethod , String params) {
		URL url = null;
		HttpURLConnection con = null;
		DataOutputStream dos = null;
		InputStream is = null;
		StringBuffer result = new StringBuffer();
		try {
			url = new URL(requrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(20 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			//提交方式
			con.setRequestMethod(reqMethod);
			if("GET".equals(reqMethod)){
				con.connect();
			}
			//设置头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			
			dos = new DataOutputStream(con.getOutputStream());
			if (null != params) {
				String param = "param=" + params;//这个参数固定格式json
				dos.write(param.getBytes("UTF-8"));
				dos.flush();
			}
			is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			is.close();
			dos.close();
		} catch (MalformedURLException e) {
			LOGGER.info("MalformedURLException", e.fillInStackTrace());
		} catch (IOException e) {
			LOGGER.info("IOException", e.fillInStackTrace());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return result.toString();
	}



	/**
	 * 获取IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	
	public static Long timeLong(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2.getTime();
	}
	
	//test请求
	public static void main(String[] args) {
		
		System.out.println(getHttpRequestResults("http://localhost:8080/servletCourse","POST",null));
	}
}

	/**
	 * 线程类
	 * @author ycl
	 *
	 */
class MyThread implements Runnable{
    private  String name;
	public MyThread(String name){
		this.name=name;
	}
	public MyThread(){
	
	}
	@Override
	public void run() {
		HttpHelper.getHttpRequestResults("http://localhost:8080/tempms/guest","POST",null);
	}
	
}
