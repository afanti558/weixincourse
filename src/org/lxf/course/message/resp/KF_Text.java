package org.lxf.course.message.resp;

import java.io.Serializable;


public class KF_Text implements Serializable{

	private static final long serialVersionUID = 57558516883047993L;

	private String content;

	public KF_Text() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
