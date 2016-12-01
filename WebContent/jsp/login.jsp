<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title></title>
<link href="${pageContext.request.contextPath}/css/login.css"
	rel="stylesheet" type="text/css" />


</head>

<body
	style="background-color:#1c77ac; background-image:url(${pageContext.request.contextPath}/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">



	<div id="mainBody">
		<div id="cloud1" class="cloud"></div>
		<div id="cloud2" class="cloud"></div>
	</div>


	<div class="logintop">
		<span>欢迎登录光电项目设计进度到款跟踪系统</span>
		<!-- <ul>
    <li><a href="#">回首页</a></li>
    <li><a href="#">帮助</a></li>
    <li><a href="#">关于</a></li>
    </ul> -->
	</div>

	<div class="loginbody">
		<span class="systemlogo"></span>
		<div class="loginbox">
			<form action="/CIMS/login/login.do" method="post" id="login-form">
				<ul>
					<li><input name="userName" type="text" class="loginuser"
						id="userName" placeholder="用户名" /><label
						style="visibility: hidden" id="userError" class="error-label">用户不存在</label></li>
					<li><input name="password" type="password" class="loginpwd"
						id="password" placeholder="密码" /><label
						style="visibility: hidden" id="pwdError" class="error-label">密码错误</label></li>
					<li><input type="button" class="loginbtn" value="登录"
						id="login-btn" /><input style="margin-left: 24px" type="reset"
						class="loginbtn" value="重置" /></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="loginbm">西北勘测设计院光伏电设计分院</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/lib/jquery-1.9.1.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/lib/cloud.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/app/loginCheck.js"></script>

	<script type="text/javascript">
		$(function() {
			$('.loginbox').css({
				'position' : 'absolute',
				'left' : ($(window).width() - 692) / 2
			});
			$(window).resize(function() {
				$('.loginbox').css({
					'position' : 'absolute',
					'left' : ($(window).width() - 692) / 2
				});
			});
		});
	</script>
</body>
</html>
