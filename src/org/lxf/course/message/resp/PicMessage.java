package org.lxf.course.message.resp;

/**
 * 返回的图片消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class PicMessage extends BaseMessage {
	
	// 通过上传多媒体文件，得到的id
	private String MediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

}
