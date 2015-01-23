package org.lxf.course.service;

import javax.servlet.http.HttpServletRequest;

public interface ICoreService {
	
	/**
	 * 处理微信发来的各类请求（被动回复）
	 * @param request
	 */
	public String processRequest(HttpServletRequest request);
	
	/**
	 * 客服向指定用户发起消息
	 * @return String 组装好的消息json格式
	 * @param touser 推送消息的用户openID
	 */
	public void processRequest_kfaccount(String touser,String content);
	
	
}
