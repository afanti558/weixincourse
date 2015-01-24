package org.lxf.course.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lxf.course.service.CoreServiceImpl;
import org.lxf.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class OAutherize
 */
public class OAutherizeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CoreServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAutherizeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("OAutherizeServlet doGet");
		//code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
		String code = request.getParameter("code");
		String state = request.getParameter("state");//参数,非必须
		String openId = WeixinUtil.getOpenId(code);//通过code去取得网页授权access_token以及openId
		String forward_url = "/WEB-INF/views/authorize.jsp";
		LOG.info("openId:" + openId);
		request.setAttribute("openId", "'" + openId + "'");
		this.getServletContext().getRequestDispatcher(forward_url).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("OAutherizeServlet doPost");
		request.setCharacterEncoding("UTF-8");
		String touser = request.getParameter("openId");
		String content = request.getParameter("content");
		LOG.info("您要发送消息给：" + touser + ",消息的内容为：" + content);
		//推送消息
		new CoreServiceImpl().processRequest_kfaccount(touser,content);  
        // 响应消息  
//        PrintWriter out = response.getWriter();  
//        out.print(respMessage); 
//        out.flush();
//        out.close(); 
		
	}

}
