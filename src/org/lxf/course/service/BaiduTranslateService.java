package org.lxf.course.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.lxf.weixin.pojo.TranslateResult;

import com.google.gson.Gson;

/**
 * 
 * @author liufeng
 * @date 2013-10-21
 */
public class BaiduTranslateService {
	/**
	 * 发起http请求获取返回结果
	 * 
	 * @param requestUrl 请求地址
	 * @return
	 */
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");//字节流转换为字符流
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
			httpUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * utf编码
	 * 
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 翻译（中->英 英->中 日->中 ）
	 * 
	 * @param source
	 * @return
	 */
	public static String translate(String source) {
		String det = null;

		// 组装查询地址
		String requestUrl = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=jrTeYw2hwh7ZSzuPuw0wg92G&q={keyWord}&from=auto&to=auto";
		// 对参数q的值进行urlEncode utf-8编码
		requestUrl = requestUrl.replace("{keyWord}", urlEncodeUTF8(source));
		// 查询并解析结果
		try {
			// 查询并获取返回结果
			String json = httpRequest(requestUrl);
			// 通过Gson工具将json转换成TranslateResult对象
			TranslateResult translateResult = new Gson().fromJson(json, TranslateResult.class);
			// 取出translateResult中的译文
			det = translateResult.getTrans_result().get(0).getDst();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == det)
			det = "翻译系统异常，请稍候尝试！";
		return det;
	}

	/**
	 * 对我接口
	 * author linxiaofan
	 * @param src
	 * @return
	 */
	public static String getTranslateResult(String src){
		return translate(src);
	}
	public static void main(String[] args) {
//		 翻译结果：The network really powerful
//		System.out.println(translate("网络真强大"));
		System.out.println("翻译我爱你。".substring(2));
	}
}