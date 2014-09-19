package org.lxf.weixin.pojo;

/**
 * 普通按钮（子按钮）
 * click类型的子按钮
 * @author liufeng
 * @date 2013-08-08
 */
public class CommonButton extends Button {
	//父类中name
	private String type;
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "CommonButton [type=" + type + ", key=" + key + "]";
	}
	
}