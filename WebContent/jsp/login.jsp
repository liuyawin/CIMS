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
		<span>欢迎登录后台管理界面平台</span>
		<!-- <ul>
    <li><a href="#">回首页</a></li>
    <li><a href="#">帮助</a></li>
    <li><a href="#">关于</a></li>
    </ul> -->
	</div>

	<div class="loginbody">

		<span class="systemlogo"></span>

		<div class="loginbox">
			<form action="loginValidate">
				<ul>
					<li><input name="userName" type="text" class="loginuser" value="admin"
						onclick="JavaScript:this.value=''" /></li>
					<li><input name="password" type="text" class="loginpwd" value="密码"
						onclick="JavaScript:this.value=''" /></li>
					<li><input name="" type="button" class="loginbtn" value="登录"
						onclick="javascript:window.location='main.html'" /><label><input
							name="" type="checkbox" value="" checked="checked" />记住密码</label><label><a
							href="#">忘记密码？</a></label></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="loginbm">西安电子科技大学</div>
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
