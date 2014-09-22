package org.lxf.weixin.pojo;

import java.util.Arrays;

/**
 * 菜单
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class Menu {
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}

	@Override
	public String toString() {
		return "Menu [button=" + Arrays.toString(button) + "]";
	}
	
}