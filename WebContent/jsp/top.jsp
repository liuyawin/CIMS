<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
<title>无标题文档</title>
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/admin.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/zhou.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/li.css" rel="stylesheet" type="text/css" />
</head>

<body style="background:url(${ctx}/images/topbg.gif) repeat-x;">
	<header>
		<div class="topleft">
			<a href="main.html" target="_parent"><img class="img-logo"
				src="${ctx}/images/logo1.png" title="系统首页" /></a>
		</div>

		<div class="topright">
			<ul>
				<%-- <li><span><img src="${ctx}/images/help.png" title="帮助"  class="helpimg"/></span><a href="#">帮助</a></li> --%>
				<!-- <li><a href="#">关于</a></li> -->
				<li><a href="/CIMS/login/logout.do">安全退出</a></li>
			</ul>

			<div class="user">
				<span id="userNum"></span> <i>报警</i><a
					href="/CIMS/alarm/toAlarmListPage.do"><b id="newsNum">12</b></a>
			</div>
		</div>
	</header>
	<section class="containner">
		<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				var cookie = {};
				var cookies = document.cookie;
				if(cookies === "") return cookie;
				var list = cookies.split(";");
				for(var i = 0; i < list.length; i++) {
					var cookieString = list[i];
					var p = cookieString.indexOf("=");
					var name = cookieString.substring(0, p);
					var value = cookieString.substring(p+1, cookieString.length);
					cookie[name.trim()] = value;
				}
				$('#userNum').html(cookie.userNum);
				
				/* $.getJSON("/CIMS/alarm/selectAlarms.do", {
					isRemove : '0'					
				}, function(data) {
					$("#newsNum").text(data.totalRow);
				});*/
			});  
		</script>