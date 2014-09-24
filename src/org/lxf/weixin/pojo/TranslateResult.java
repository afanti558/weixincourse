package org.lxf.weixin.pojo;

import java.util.List;

public class TranslateResult {
//	注意：这里的类名可以任意取，但是成员变量的名字应于翻译API返回的JSON字符串中的属性名保持一致，否则将JSON转换成TranslateResult对象时会报错。
		private String from ;
		
		private String to ;
		
		private List<Trans_result> trans_result ;
		
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public List<Trans_result> getTrans_result() {
			return trans_result;
		}
		public void setList(List<Trans_result> trans_result) {
			this.trans_result = trans_result;
		}
		
}
