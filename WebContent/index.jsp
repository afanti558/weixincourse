<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>微信测试</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
</head>
<body>
	<h1 style="align:center;">功能导航页</h1>
	<h2>js接口调试</h2>
	<form action="MyServlet" method="post">
		<input type="hidden" name="type" value="1">
		<input type="submit" value="提交">
    </form>
    <h2>授权调试</h2>
    <form action="MyServlet" method="post">
    	<input type="hidden" name="type" value="2">
		<input type="submit" value="提交">
    </form>
	
	<!-- 以下button直接页面跳转 -->
	<!-- <button class="btn btn_primary" id="scanQRCode" onclick="window.location.href='views/scanQRCode.jsp'">扫一扫接口</button> -->
</body>
</html>