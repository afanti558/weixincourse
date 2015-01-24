package org.lxf.course;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.util.WeixinConstant;
import org.lxf.weixin.util.WeixinUtil;

import sun.java2d.pipe.hw.AccelDeviceEventListener;

import com.alibaba.fastjson.JSONObject;

public class MediaUtil {

	public static void main(String[] args) {
    AccessToken accessToken =  WeixinUtil.getAccessToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
    String access_token = accessToken.getAccess_token();
    String mediaId = "D09hjO_vJrzJ9jUnQ83v5nJdhYIYycAmGHBj2zm_ij6gJqytF4R82oAJ2dWWlqII";
    String savePath = "H:\\jpg";
    
		System.out.println(downloadMedia(access_token,mediaId,savePath));
	}
	
	
	
	/**
	   * 获取媒体文件
	   * @param accessToken 接口访问凭证
	   * @param media_id 媒体文件id
	   * @param savePath 文件在服务器上的存储路径
	   * */
	  public static String downloadMedia(String accessToken, String mediaId, String savePath) {
	    String filePath = null;
	    // 拼接请求地址
	    String requestUrl = WeixinConstant.MEDIA_DOWNLOAD_URL;
	    requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
	    System.out.println("请求接口requestUrl:" + requestUrl);
	    try {
	      URL url = new URL(requestUrl);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setDoInput(true);
	      conn.setRequestMethod("GET");

	      if (!savePath.endsWith("/")) {
	        savePath += "/";
	      }
	      // 根据内容类型获取扩展名
//	      String fileExt = WeixinUtil.getFileEndWitsh(conn.getHeaderField("Content-Type"));
	      String fileExt = ".jpg";
	      // 将mediaId作为文件名
	      filePath = savePath + mediaId + fileExt;

	      BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	      FileOutputStream fos = new FileOutputStream(new File(filePath));
	      byte[] buf = new byte[8096];
	      int size = 0;
	      while ((size = bis.read(buf)) != -1)
	        fos.write(buf, 0, size);
	      fos.close();
	      bis.close();

	      conn.disconnect();
	      String info = String.format("下载媒体文件成功，filePath=" + filePath);
	      System.out.println(info);
	    } catch (Exception e) {
	      filePath = null;
	      String error = String.format("下载媒体文件失败：%s", e);
	      System.out.println(error);
	    }
	    return filePath;
	  }




}