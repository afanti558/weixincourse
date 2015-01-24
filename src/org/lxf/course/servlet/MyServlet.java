package org.lxf.course.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lxf.weixin.util.WeixinConstant;
import org.lxf.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6164424484412075120L;
	private static final Logger LOG = LoggerFactory.getLogger(MyServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOG.info("MyServlet doGet:" + new Date());
		System.out.println("serverId" + request.getParameter("serverId"));
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOG.info("MyServlet doPost:" + new Date());
		int type = Integer.valueOf(request.getParameter("type"));
		LOG.info("type:" + type);
		if(type == 1){
			Map<String, String> ret = WeixinUtil.getPageParm(WeixinConstant.URL);
			for (Map.Entry entry : ret.entrySet()) {
	//			System.out.println(entry.getKey() + ":" + entry.getValue());
				request.setAttribute(entry.getKey()+"","'"+entry.getValue()+"'");
			}
			this.getServletContext().getRequestDispatcher("/WEB-INF/views/scanQRCode.jsp").forward(request, response);
		}else if(type == 2){
			String redirect_url_temp = "http://js.wwz114.cn/weixincourse/OAutherizeServlet";
			String redirect_url = urlEncode(redirect_url_temp);
			String url = WeixinConstant.AUTHORIZE_URL.replace("APPID", WeixinConstant.APPID).replace("REDIRECT_URI", redirect_url).replace("SCOPE", "snsapi_base").replace("STATE", "123");
			System.out.println(url);
			response.sendRedirect(url);
		}
		
	}
	
	public static String urlEncode(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("java.io.UnsupportedEncodingException:",
					e.fillInStackTrace());
		}
		return result;
	}

}










