package org.lxf.weixin.pojo;

import java.util.Arrays;

/**
 * 复杂按钮（父按钮）
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class ComplexButton extends Button {
	//父类中的name
	private Button[] sub_button;//这里的sub_button可以赋值CommonButton或者CommonViewButton的实例

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}

	@Override
	public String toString() {
		return "ComplexButton [sub_button=" + Arrays.toString(sub_button) + "]";
	}
	
}