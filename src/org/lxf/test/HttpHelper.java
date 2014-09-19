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

	public static void main(String[] args) {
		System.out.println(getRequestResults("http://localhost:8080/servletCourse"));
	}

	/**
	 * 璇锋眰鏁版嵁
	 * 
	 * @param requrl
	 * @param params
	 * @return
	 */
	public static String getRequestResults(String requrl) {
		
		URL url = null;
		HttpURLConnection con = null;
		InputStream is = null;
		StringBuffer result = new StringBuffer();
		try {
			url = new URL(requrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(20 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");// 鎸佷箙閾炬帴
			con.setRequestProperty("Charset", "UTF-8");
			System.out.println(con);
			is = con.getInputStream();
			System.out.println(is);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line ="";
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			is.close();
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
	 * 璇锋眰鏁版嵁
	 * 
	 * @param requrl
	 * @param params
	 * @return
	 */
	public static String getRequestResults(String requrl, String params) {
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
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");// 鎸佷箙閾炬帴
			con.setRequestProperty("Charset", "UTF-8");
			dos = new DataOutputStream(con.getOutputStream());
			if (params != null) {
				String param = "param=" + params;
				dos.write(param.getBytes("UTF-8"));
				dos.flush();
			}
			is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
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
	 * 鑾峰彇杩滅▼璁块棶IP
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

	public static String getRequestResultWithGBK(String requrl) {
		URL url = null;
		HttpURLConnection con = null;
		InputStream is = null;
		StringBuffer result = new StringBuffer();
		try {
			url = new URL(requrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(20 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("GET");
			con.setRequestProperty("Connection", "Keep-Alive");// 鎸佷箙閾炬帴
			con.setRequestProperty("Charset", "GBK");
			is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			is.close();
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

}


class MyThread implements Runnable{
    private  String name;
	public MyThread(String name){
		this.name=name;
	}
	public MyThread(){
	
	}
	@Override
	public void run() {
		HttpHelper.getRequestResults("http://localhost:8080/tempms/guest");
	}
	
}
