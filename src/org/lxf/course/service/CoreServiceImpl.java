package org.lxf.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.lxf.course.message.resp.Article;
import org.lxf.course.message.resp.KF_Text;
import org.lxf.course.message.resp.KF_TextMessage;
import org.lxf.course.message.resp.NewsMessage;
import org.lxf.course.message.resp.TextMessage;
import org.lxf.weixin.util.MessageUtil;
import org.lxf.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 核心服务类,处理微信发来的各类请求
 * @author lxf
 * @date 2013-05-20
 */
public class CoreServiceImpl implements ICoreService{
	private static final Logger LOG = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	/**
	 * 处理微信发来的各类请求
	 * @param request 发送过来的请求
	 * @return 
	 */
	public String processRequest(HttpServletRequest request) {
		System.out.println("处理微信消息");
		String respMessage = null;
		try {
			// 默认返回的文本消息内容：菜单
			String respContent = "默认返回的文本内容。";
			// xml请求解析，保存在map中以便后去取用
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息id，64位整型
			String msgId = requestMap.get("MsgId");
			System.out.println("消息来源："+fromUserName+"，消息去向："+toUserName+"，消息类型:"+msgType+"，消息id:"+msgId);

			//默认情况下返回的文本内容
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get("Content");
				System.out.println("发送过来的文本消息的内容："+content);
				
				if(content.equals("?")){//返回导航菜单
					textMessage.setContent(getMainMenu());
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}else{//否则返回我想推送的图文消息
					NewsMessage news = new NewsMessage();
					news.setToUserName(fromUserName);
					news.setFromUserName(toUserName);
					news.setCreateTime(new Date().getTime());
					news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					news.setFuncFlag(0);
					List<Article> articleList = new ArrayList<Article>();
					Article article1 = new Article("图文消息标题","多图文不可以显示出描述的内容",
							"http://img1.imgtn.bdimg.com/it/u=3565455087,2166573950&fm=21&gp=0.jpg",
							"http://js.wwz114.cn/weixincourse/");//跳转到我测试的页面
					articleList.add(article1);
					news.setArticleCount(articleList.size());//图文消息个数
					news.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(news); 
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {//发送的是图片消息
				//图片消息媒体id，可以调用多媒体文件下载接口拉取数据
				String mediaId = requestMap.get("MediaId");
				//图片链接
				String picUrl = requestMap.get("PicUrl");
				//消息id，64位整型
				String msgID = requestMap.get("MsgID");
				System.out.println("发送的图片消息链接(PicUrl)为:"+picUrl);
				
				respContent = "您发送的是图片消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {//发送的是地理位置消息
				//地理位置维度
				String location_x = requestMap.get("Location_X");
				//地理位置经度
				String location_y = requestMap.get("Location_Y");
				//地图缩放大小
				String scale = requestMap.get("Scale");
				//地理位置信息
				String label = requestMap.get("Label");
				//消息id，64位整型
				String msgID = requestMap.get("MsgID");
				System.out.println("发送的地理位置纬度(Location_X)为:"+location_x+"，发送的地理位置经度(Location_Y)为:"+location_y+"，地图缩放大小(Scale)为："+scale);
				
				respContent = "您发送的是地理位置消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {// 链接消息
				//消息标题
				String title = requestMap.get("Title");
				//消息描述
				String description = requestMap.get("Description");
				//消息链接
				String url = requestMap.get("Url");
				//消息id，64位整型
				String msgID = requestMap.get("MsgID");
				System.out.println("发送的链接消息的标题(Title)为:"+title+"，链接的描述(Description)为："+description+",链接的地址(Url)为："+url);
				
				respContent = "您发送的是链接消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {// 音频消息
				//语音消息媒体id，可以调用多媒体文件下载接口拉取数据
				String mediaId = requestMap.get("MediaId");
				//语音格式，如amr，speex等
				String format = requestMap.get("Format");
				//消息id，64位整型
				String msgID = requestMap.get("MsgID");
				//识别之后的内容(语音识别接口没有开通)
//				String recognition = requestMap.get("Recognition");
//				LOGGER.info("发送的语音消息的媒体id(MediaId)为:"+mediaId+"，识别之后的内容(Recognition)为："+recognition+",语音格式(format)为："+format);
				System.out.println("发送的语音消息的媒体id(MediaId)为:"+mediaId+",语音格式(format)为："+format);
				
//				textMessage.setContent(recognition);
				respContent = "您发送的是语音消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
//				VoiceMessage voiceMessage = new VoiceMessage();
//				voiceMessage.setToUserName(fromUserName);
//				voiceMessage.setFromUserName(toUserName);
//				voiceMessage.setCreateTime(new Date().getTime());
//				voiceMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_VOICE);
//				voiceMessage.setFuncFlag(0);
//				voiceMessage.setMediaId(mediaId);//通过上传多媒体文件得到的id
//				respMessage = MessageUtil.voiceMessageToXml(voiceMessage);
			}else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
				// 若事件类型event，则带有参数Eevent,再次判断具体事件类型
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                	//首次关注推送个单图文过去
                	NewsMessage news = new NewsMessage();
                	List<Article> articleList = new ArrayList<Article>();
                	Article article = new Article("欢迎关注","请发送'？'查看更多",
							"http://img1.imgtn.bdimg.com/it/u=3565455087,2166573950&fm=21&gp=0.jpg",
							"http://blog.csdn.net/lyq8479/article/details/9393195");
                	articleList.add(article);
                	news.setArticles(articleList);
                	news.setArticleCount(1);
                	respMessage = MessageUtil.newsMessageToXml(news);
                }  
                // 取消订阅  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    String eventKey = requestMap.get("EventKey");  
                    if (eventKey.equals("11")) {  
                        respContent = "天气预报菜单项被点击！";  
                    } else if (eventKey.equals("12")) {  
                        respContent = "公交查询菜单项被点击！";  
                    } else if (eventKey.equals("13")) {
                        respContent = "周边搜索菜单项被点击！";  
                    } else if (eventKey.equals("21")) { 
                       respContent = "歌曲点播菜单项被点击！";  
                    } else if (eventKey.equals("22")) {  
                        respContent = "经典游戏菜单项被点击！";  
                    } else if (eventKey.equals("23")) {  
                        respContent = "美女电台菜单项被点击！";  
                    } else if (eventKey.equals("24")) {  
                        respContent = "人脸识别菜单项被点击！";  
                    } else if (eventKey.equals("25")) {  
                        respContent = "聊天唠嗑菜单项被点击！";  
                    } else if (eventKey.equals("31")) {  
                        respContent = "Q友圈菜单项被点击！";  
                    } else if (eventKey.equals("32")) {  
                        respContent = "电影排行榜菜单项被点击！";  
                    } else if (eventKey.equals("33")) {  
                        respContent = "幽默笑话菜单项被点击！";  
                    } 
                    // 34为view类型的菜单，在创建菜单的时候已经定义好了点击动作(直接跳转)，这里不需要第三方再次进行响应
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                } 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}
	
	/**
	 * 当回复“？”时的提示菜单
	 * @return
	 */
	public static String getMainMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好,我是小q,请选择服务：").append("\n");
		buffer.append("1  天气预报").append("\n");
		buffer.append("2  公交查询").append("\n");
		buffer.append("3  周边搜索").append("\n");
		buffer.append("4  歌曲点播").append("\n");
		buffer.append("5  经典游戏").append("\n");
		buffer.append("6  美女电台").append("\n");
		buffer.append("7  人脸识别").append("\n");
		buffer.append("8  聊天唠嗑").append("\n");
		buffer.append("? 显示此帮助菜单");
		return buffer.toString();
	}

	@Override
	public void processRequest_kfaccount(String touser,String content) {
		String respMessage = "";
		//客服发送文本内容
		KF_Text kf_text = new KF_Text();
		kf_text.setContent(content);
		KF_TextMessage kf_textMessage = new KF_TextMessage();
		kf_textMessage.setTouser(touser);
		kf_textMessage.setText(kf_text);
		kf_textMessage.setMsgtype(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		respMessage = JSONObject.toJSONString(kf_textMessage);
		WeixinUtil.KF_MessageProcess(respMessage);
	}
	
	public static void main(String[] args){
//		String touser = "888888888888";
//		System.out.println(processRequest_kfaccount(touser)); 
	}
	
}
