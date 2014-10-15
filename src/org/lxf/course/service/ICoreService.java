package org.lxf.course.service;

import javax.servlet.http.HttpServletRequest;

public interface ICoreService {
	
	/**
	 * 处理微信发来的各类请求
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request);
	
	
}
