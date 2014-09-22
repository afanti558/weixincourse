package org.lxf.course.message.req;

/**
 * 接收的图片消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class PicMessage extends BaseMessage {
	
	// 图片链接
	private String PicUrl;
	// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
	private String MediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}


}
