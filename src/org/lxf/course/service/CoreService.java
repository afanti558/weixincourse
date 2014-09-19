package org.lxf.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.lxf.course.message.resp.Article;
import org.lxf.course.message.resp.Music;
import org.lxf.course.message.resp.MusicMessage;
import org.lxf.course.message.resp.NewsMessage;
import org.lxf.course.message.resp.TextMessage;
import org.lxf.course.message.resp.VoiceMessage;
import org.lxf.weixin.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心服务类
 * 
 * @author lxf
 * @date 2013-05-20
 */
public class CoreService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容：菜单
			String respContent = getMainMenu();
			// xml请求解析，保存在map中以便后去取用
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			LOGGER.info("消息来自于："+fromUserName+"，消息发送给："+toUserName+"，用户发来消息，类型:"+msgType);

			//默认情况下返回的文本内容
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			textMessage.setContent("默认情况下返回的文本内容");
			respMessage = MessageUtil.textMessageToXml(textMessage);
			
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				//发送过来的文本内容
				String content = requestMap.get("Content");
				LOGGER.info("发送的文本消息的内容："+content);
				
				if(content.equals("菜单")){//返回导航菜单(以后补充菜单功能)
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}else if(content.equals("您好")){//原样返回发送过来的内容
					respContent = content;
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}else{//否则返回我想推送的图文消息
					NewsMessage news = new NewsMessage();
					news.setToUserName(fromUserName);
					news.setFromUserName(toUserName);
					news.setCreateTime(new Date().getTime());
					news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					news.setFuncFlag(0);
					List<Article> articleList = new ArrayList<Article>();
					Article article = new Article("图文消息之一，单图文","单图文可以显示出描述的内容",
							"http://img1.imgtn.bdimg.com/it/u=3565455087,2166573950&fm=21&gp=0.jpg",
							"http://blog.csdn.net/lyq8479/article/details/9393195");
					articleList.add(article);
					news.setArticleCount(articleList.size());
					news.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(news); 
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				//语音消息媒体id，可以调用多媒体文件下载接口拉取数据
				String mediaId = requestMap.get("MediaId");
				//语音格式，如amr，speex等
				String format = requestMap.get("Format");
				//消息id，64位整型
				String msgID = requestMap.get("MsgID");
				//识别之后的内容
				String recognition = requestMap.get("Recognition");
				LOGGER.info("发送的语音消息的媒体id:"+mediaId+"，识别之后的内容为："+recognition);
				
				TextMessage textMessage1 = new TextMessage();
				textMessage1.setToUserName(fromUserName);
				textMessage1.setFromUserName(toUserName);
				textMessage1.setCreateTime(new Date().getTime());
				textMessage1.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage1.setFuncFlag(0);
				textMessage1.setContent(recognition);
				respMessage = MessageUtil.textMessageToXml(textMessage1);
//				VoiceMessage voiceMessage = new VoiceMessage();
//				voiceMessage.setToUserName(fromUserName);
//				voiceMessage.setFromUserName(toUserName);
//				voiceMessage.setCreateTime(new Date().getTime());
//				voiceMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_VOICE);
//				voiceMessage.setFuncFlag(0);
//				voiceMessage.setMediaId(mediaId);//通过上传多媒体文件得到的id
//				respMessage = MessageUtil.voiceMessageToXml(voiceMessage);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型，则带有阐述Event,再次判断具体事件
				// 事件类型  
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "谢谢您的关注！";  
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
                    } else if (eventKey.equals("14")) {  
                        respContent = "历史上的今天菜单项被点击！";  
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
                }  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}
	
	/**
	 * 主菜单
	 * 
	 * @return
	 */
	public static String getMainMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，我是小q，请回复数字选择服务：").append("\n\n");
		buffer.append("1  天气预报").append("\n");
		buffer.append("2  公交查询").append("\n");
		buffer.append("3  周边搜索").append("\n");
		buffer.append("4  歌曲点播").append("\n");
		buffer.append("5  经典游戏").append("\n");
		buffer.append("6  美女电台").append("\n");
		buffer.append("7  人脸识别").append("\n");
		buffer.append("8  聊天唠嗑").append("\n\n");
		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}
	
	/** 
     * emoji表情转换(hex -> utf-16) 
     *  
     * @param hexEmoji 
     * @return 
     */  
    public static String emoji(int hexEmoji) {  
        return String.valueOf(Character.toChars(hexEmoji));  
    }
}
