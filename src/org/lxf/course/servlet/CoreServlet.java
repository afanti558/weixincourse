package org.lxf.course.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lxf.course.service.CoreServiceImpl;
import org.lxf.weixin.util.SignUtil;

/**
 * 核心请求处理类
 * @author lxf
 * @date 2013-05-18
 */
public class CoreServlet extends HttpServlet {

	private static final long serialVersionUID = 6255286459341988140L;
	private static final Logger LOG = LoggerFactory.getLogger(CoreServlet.class);
	
	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doGet确认请求来自微信服务器");
		System.out.println("doGet确认请求来自微信服务器");
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out.flush();
		out = null;
	}

	
	/**
	 * 接收、处理、响应微信服务器发送来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doPost接收、处理、响应微信服务器发送来的消息");
		System.out.println("doPost接收、处理、响应微信服务器发送来的消息");
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
        // 调用核心业务类接收消息、处理消息  
        String respMessage = new CoreServiceImpl().processRequest(request);  
        // 响应消息  
        PrintWriter out = response.getWriter();  
        out.print(respMessage); 
        out.flush();
        out.close();  
    }  
	
}
