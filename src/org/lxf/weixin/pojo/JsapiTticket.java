package org.lxf.weixin.pojo;

/**
 * 微信网页调用js-sdk获取config中的签名使用的参数
 * 
 * @author xflin
 * @date 2013-08-08
 */
public class JsapiTticket {
	// 获取到的凭证
	private String ticket;
	// 凭证有效时间，单位：秒
	private int expires_in;
	// 状态码  0表示成功
	private int errcode;
	// 错误信息  ok表示成功
	private String errmsg;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}