package org.lxf.weixin.menu;

import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.pojo.Button;
import org.lxf.weixin.pojo.CommonButton;
import org.lxf.weixin.pojo.CommonViewButton;
import org.lxf.weixin.pojo.ComplexButton;
import org.lxf.weixin.pojo.Menu;
import org.lxf.weixin.util.WeixinConstant;
import org.lxf.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单管理器类(创建)
 * @author lxf
 * @date 2013-08-08
 */
public class MenuManager {
	private static Logger LOG = LoggerFactory.getLogger(MenuManager.class);
	
		
	public static void main(String args[]){
		createMenu();
	}
	
	public static void createMenu() {
		AccessToken at = WeixinUtil.getAccessToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
		if (null != at) {
			// 调用接口创建菜单
			String result = WeixinUtil.createMenu(getMenu(), at.getAccess_token());
			// 判断菜单创建结果
			if (result != "")
				System.out.println("菜单创建成功！");
			else
				System.out.println("菜单创建失败:" + result);
		}
	}
	
	/**
	 * 组装菜单数据
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("天气预报");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("公交查询");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("周边搜索");
		btn13.setType("click");
		btn13.setKey("13");

		CommonButton btn14 = new CommonButton();
		btn14.setName("历史上的今天");
		btn14.setType("click");
		btn14.setKey("14");

		CommonButton btn21 = new CommonButton();
		btn21.setName("歌曲点播");
		btn21.setType("click");
		btn21.setKey("21");

		CommonButton btn22 = new CommonButton();
		btn22.setName("经典游戏");
		btn22.setType("click");
		btn22.setKey("22");

		CommonButton btn23 = new CommonButton();
		btn23.setName("美女电台");
		btn23.setType("click");
		btn23.setKey("23");

		CommonButton btn24 = new CommonButton();
		btn24.setName("人脸识别");
		btn24.setType("click");
		btn24.setKey("24");

		CommonButton btn25 = new CommonButton();
		btn25.setName("聊天唠嗑");
		btn25.setType("click");
		btn25.setKey("25");

		CommonButton btn31 = new CommonButton();
		btn31.setName("Q友圈");
		btn31.setType("click");
		btn31.setKey("31");

		CommonButton btn32 = new CommonButton();
		btn32.setName("电影排行榜");
		btn32.setType("click");
		btn32.setKey("32");

		CommonButton btn33 = new CommonButton();
		btn33.setName("幽默笑话");
		btn33.setType("click");
		btn33.setKey("33");
		//lxf
		CommonViewButton btn34 = new CommonViewButton();
		btn34.setName("测试js页面");
		btn34.setType("view");
		btn34.setUrl("http://js.wwz114.cn/hotelms/");

		//三个一级菜单
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("生活助理");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("休闲驿站");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24, btn25 });
		
		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("更多体验");
		mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33, btn34 });//lxf

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		return menu;
	}
	
	 
	
}



