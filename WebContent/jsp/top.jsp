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
    <li><a href="logout.do">安全退出</a></li>
    </ul>
     
    <div class="user">
    <span>${cookie.identify.value}</span>
    <i>消息</i>
    <b>5</b>
    </div>       
    </div>
	</header>
	<section class="containner"> 