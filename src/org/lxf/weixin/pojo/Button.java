package org.lxf.weixin.pojo;

/**
 * 按钮的基类
 * 抽取button和sub_button的共有属性
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class Button {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Button [name=" + name + "]";
	}
	
}