package org.lxf.course.message.resp;

import java.io.Serializable;

public class KF_TextMessage implements Serializable{

	private static long serialVersionUID = -7978213061625535388L;
	
	private String touser;
	private String msgtype;
	private KF_Text text;
	public KF_TextMessage() {
		super();
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public KF_Text getText() {
		return text;
	}
	public void setText(KF_Text text) {
		this.text = text;
	} 
	
	
}
