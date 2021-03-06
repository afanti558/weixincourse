package org.lxf.weixin.pojo;

/**
 * 普通按钮（子按钮）
 * view类型的子按钮
 * @author liufeng
 * @date 2013-08-08
 */
public class CommonViewButton extends Button {
	//父类中name
	private String type;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "CommonViewButton [type=" + type + ", url=" + url + "]";
	}
	
}




