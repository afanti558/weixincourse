<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>微信测试</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
</head>
<body>
	<h1>授权页面</h1>
	<form action="OAutherizeServlet" method="post">
		请输入要提交的内容：<input type="text" name="content" id="content" value="">
		<input type="hidden" name="openId" id="openId" value="">
		<input type="submit" value="发送" onclick="return closePage();">
	</form>
</body>
<script type="text/javascript">
	var openId = <%= request.getAttribute("openId") %>;
	document.getElementById("openId").value=openId;
	function closePage(){
		WeixinJSBridge.call('closeWindow');
		alert("关闭窗口");
		return true;
	}
</script>
</html>