package org.lxf.weixin.account;

import java.util.Date;

import org.lxf.weixin.pojo.AccessToken;
import org.lxf.weixin.pojo.KFAccount;
import org.lxf.weixin.util.WeixinConstant;
import org.lxf.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 客服管理器
 * @description
 * @author 
 * @time 2015-1-22上午10:17:05
 */
public class KFAccountManager {
	private static Logger LOG = LoggerFactory.getLogger(KFAccountManager.class);
	
	public static void createKFAccount() {
		LOG.info("createKFAccount:" + new Date());
		AccessToken at = WeixinUtil.getAccessToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
		KFAccount kfAccount = new KFAccount("ssheng@raipeng","客服1");
		if (null != at) {
			// 调用接口创建菜单
			String result = WeixinUtil.createKFAcount(kfAccount, at.getAccess_token());
			// 判断菜单创建结果
			if (result != "")
				System.out.println("客服创建成功！");
			else{
				System.out.println("客服创建失败:" + result);
			}
		}
	}
	
	public static void main(String args[]){
		createKFAccount();
	}
}
