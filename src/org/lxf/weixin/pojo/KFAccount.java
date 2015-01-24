package org.lxf.weixin.pojo;

/**
 * 客服
 * @description
 */
public class KFAccount {
    private String kf_account;
    private String nickname;
//    private String password;
    
    
	public KFAccount(String kf_account, String nickname) {
		super();
		this.kf_account = kf_account;
		this.nickname = nickname;
	}
	public KFAccount() {
		super();
	}
	public String getKf_account() {
		return kf_account;
	}
	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Override
	public String toString() {
		return "KFAccount [kf_account=" + kf_account + ", nickname=" + nickname
				+ "]";
	}
	
    
    
}
