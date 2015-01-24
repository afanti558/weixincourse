package org.lxf.weixin.util;

/**
 * 微信常量
 * @author xflin
 * @time 2015-1-21下午12:50:42
 */
public final class WeixinConstant {
	
	// 与接口配置信息中的Token要一致
	public static final String TOKEN = "weixinCourse";
	//访问微信js所在页面的url
	public static final String URL = "http://js.wwz114.cn/weixincourse/MyServlet";
	// 第三方用户唯一凭证
	public static final String APPID = "wx72e68934f563a474";
	// 第三方用户唯一凭证密钥
	public static final String APPSECRET = "b87b8acbdc68c3b6ba784bcea48d47fb";
	
	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 媒体文件上传（POST/FORM）
	public static final String MEDIA_UPLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	// 媒体文件下载（POST/FORM）
	public static final String MEDIA_DOWNLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	// 获取jsapi_ticket（GET）  不限次数
	public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	// 用户同意授权，获取code
	public static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	// 通过code换取的是一个特殊的网页授权access_token,如果网页授权的作用域为snsapi_base,
	// 本步骤中获取到网页授权access_token的同时，也获取到了openid   
	public static final String ACCESS_TOKEN_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	// 创建客服 (POST)
	public static final String KF_CREATE_URL = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";
	// 调用客服推送信息 (POST)
	public static final String KF_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";


}
