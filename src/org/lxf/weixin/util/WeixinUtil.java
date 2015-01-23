package org.lxf.weixin.util;

import java.util.Map;
import java.util.Map.Entry;

import org.lxf.course.service.Sign;
import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.pojo.JsapiTticket;
import org.lxf.weixin.pojo.KFAccount;
import org.lxf.weixin.pojo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 公众平台通用接口工具类
 * @author liuyq
 * @date 2013-08-09
 */
public class WeixinUtil {
	private static AccessToken ACCESSTOKEN = null;
	private static JsapiTticket JSAPITTICKET = null;
	private static Long TIME;
	private static Long TIME2;
	
	private static Logger LOG = LoggerFactory.getLogger(WeixinUtil.class);
	
	/**
	 * 创建菜单
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * 接口返回0表示成功，其他值表示失败
	 * @return result
	 */
	public static String createMenu(Menu menu, String accessToken) {
		String result = "";
		// 拼装创建菜单的url
		String url = WeixinConstant.MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.toJSONString(menu);
		LOG.info("组装菜单json格式字符串jsonMenu------->" + jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = HTTPUtil.httpsRequests(url, "POST", jsonMenu);
		LOG.info("创建菜单接口调用返回值jsonObject----->" + jsonObject);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				result = "创建菜单失败:" + jsonObject.getString("errcode") + "," + jsonObject.getString("errmsg");
				LOG.info("创建菜单失败:", jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
			}else{
				result = "创建菜单成功.";
			}
		}
		return result;
	}

	/**
	 * 创建客服
	  * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * 接口返回0表示成功，其他值表示失败
	 * @return result
	 */

	public static String createKFAcount(KFAccount kfAccount,String accessToken){
		String result = "";
		// 拼装创建客服的url
		String url = WeixinConstant.KF_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将客服对象转换成json字符串
		String jsonKF = JSONObject.toJSONString(kfAccount);
		LOG.info("组装客服json格式字符串jsonMenu------->" + jsonKF);
		// 调用接口创建客服
		JSONObject jsonObject = HTTPUtil.httpsRequests(url, "POST", jsonKF);
		LOG.info("创建菜单接口调用返回值jsonObject----->" + jsonObject);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				LOG.info("创建客服失败:", jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
			}else{
				result = "创建客服成功.";
			}
		}
		return result;
	}
	
	/**
	 * 获取access_token
	 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return AccessToken
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		//将ACCESSTOKEN保存为全局的，当无效的时候才再次发起请求，否自直接返回现有的提供使用
		if(ACCESSTOKEN == null || System.currentTimeMillis() - TIME > ACCESSTOKEN.getExpires_in()*1000 - 200*100){
			String requestUrl = WeixinConstant.ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET",appsecret);
			JSONObject jsonObject = HTTPUtil.httpsRequests(requestUrl, "GET", null);
			if (null != jsonObject) {
				if(jsonObject.getString("errcode") == null){//没有错误码返回说明返回成功
					ACCESSTOKEN = JSONObject.toJavaObject(jsonObject, AccessToken.class);
				}else{
					LOG.info("获取accessToken失败," + jsonObject.getString("errcode") + "," + jsonObject.getString("errmsg"));
				}
			}
            TIME = System.currentTimeMillis();
        }
		LOG.info("access_token:" + ACCESSTOKEN.getAccess_token());
		return ACCESSTOKEN;
	}
	/**
	 * 获取jsapi_ticket
	 * jsapi_ticket是公众号用于调用微信JS接口的临时票据
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return AccessToken
	 */
	public static JsapiTticket getJsapiTticket(String access_token) {
		//将JsapiTticket保存为全局的，当无效的时候才再次发起请求，否自直接返回现有的提供使用
		if(JSAPITTICKET == null || System.currentTimeMillis() - TIME2 > JSAPITTICKET.getExpires_in()*1000 - 200*100){
			String requestUrl = WeixinConstant.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
			JSONObject jsonObject = HTTPUtil.httpsRequests(requestUrl,"GET",null);
			if (null != jsonObject) {
				if("0".equals(jsonObject.getString("errcode"))){//"errcode":0 表示成功
					JSAPITTICKET = JSONObject.toJavaObject(jsonObject, JsapiTticket.class);
				}else{
					LOG.info("获取jsapi_ticket失败," + jsonObject.getString("errcode") + "," + jsonObject.getString("errmsg"));
				}
			}
            TIME2 = System.currentTimeMillis();
        }
		return JSAPITTICKET;
	}
	/**
	 * 获取页面所需参数
	 * @return String
	 * @param jsapi_ticket
	 * @param url 页面url
	 */
	public static Map<String, String> getPageParm(String url){
		AccessToken accessToken = getAccessToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
    	String access_token = accessToken.getAccess_token();
    	JsapiTticket jsapiTicket = getJsapiTticket(access_token);
    	
    	String jsapi_ticket = jsapiTicket.getTicket();
    	System.out.println("jsapi_ticket:" + jsapi_ticket);
    	Map<String, String> map = Sign.getSignature(jsapi_ticket, WeixinConstant.URL);
		return map;
	}
	/**
	 * 获取网页授权access_token
	 * @return AccessToken
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @param code 换取页面access_token的票据
	 * @return AccessToken
	 */
	public static AccessToken getAccessToken(String appid, String appsecret,String code){
		System.out.println("code:" + code);
		String requestUrl = WeixinConstant.ACCESS_TOKEN_OPENID_URL.replace("APPID", WeixinConstant.APPID).
				replace("SECRET",WeixinConstant.APPSECRET ).replace("CODE",code);
//		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx72e68934f563a474&secret=b87b8acbdc68c3b6ba784bcea48d47fb&code=0418ab4bd5dc2ae073d633de4a927613&grant_type=authorization_code";
		JSONObject jsonObject = HTTPUtil.httpsRequests(requestUrl, "GET", null);
		System.out.println(jsonObject);
		AccessToken accessToken= JSONObject.toJavaObject(jsonObject, AccessToken.class);
		return accessToken;
	}
    
	/**
	 * 取得用户openId
	 * @param code
	 */
	public static String getOpenId(String code){
		AccessToken accessToken = getAccessToken(WeixinConstant.APPID,WeixinConstant.APPSECRET,code);
		String openId = accessToken.getOpenid();
		return openId;
	}
	
	/**
	 * 客房发送消息
	 * @param respMessage 发送的消息内容
	 */
	public static String KF_MessageProcess(String respMessage){
		AccessToken accessToken = getAccessToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
		String access_token = accessToken.getAccess_token();
		String requestUrl = WeixinConstant.KF_MESSAGE_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = HTTPUtil.httpsRequests(requestUrl,"POST",respMessage);
		if("0".equals(jsonObject.getString("errcode"))){
			return "发送成功";
		}
		return "发送失败";
	}

	public static String downloadMedia(){
		
		
		return "";
	}
	
    /**
     * 测试
     * @throws Exception
     */
    public static void main(String args[]) throws Exception{
    	/*
    	Map<String, String> ret = getPageParm(WeixinConstant.URL);
        for (Entry<String, String> entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        */
        
        System.out.println(getOpenId(""));
    }
}
