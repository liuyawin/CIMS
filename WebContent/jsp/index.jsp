<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section class="main" ng-app="index">
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    </ul>
    </div>
    
    <div class="mainindex">    
    <div class="welinfo">
    <span><img src="${ctx}/images/sun.png" alt="天气" /></span>
    <b>Admin早上好，欢迎使用信息管理系统</b>
    <a href="#">帐号设置</a>
    </div>
 
    <div class="xline"></div>
    
    <ul class="iconlist">
    
    <li><img src="${ctx}/images/ico01.png" /><p><a href="#">管理设置</a></p></li>
    <li><img src="${ctx}/images/ico02.png" /><p><a href="#">发布文章</a></p></li>
    <li><img src="${ctx}/images/ico03.png" /><p><a href="#">数据统计</a></p></li>
    <li><img src="${ctx}/images/ico04.png" /><p><a href="#">文件上传</a></p></li>
    <li><img src="${ctx}/images/ico05.png" /><p><a href="#">目录管理</a></p></li>
    <li><img src="${ctx}/images/ico06.png" /><p><a href="#">查询</a></p></li> 
            
    </ul>
    
    <div class="ibox"><a class="ibtn"><img src="${ctx}/images/iadd.png" />添加新的快捷功能</a></div>
    
    <div class="xline"></div>
    <div class="box"></div>
    
    <div class="welinfo">
    <span><img src="${ctx}/images/dp.png" alt="提醒" /></span>
    <b>Uimaker信息管理系统使用指南</b>
    </div>
    
    <ul class="infolist">
    <li><span>您可以快速进行文章发布管理操作</span><a class="ibtn">发布或管理文章</a></li>
    <li><span>您可以快速发布产品</span><a class="ibtn">发布或管理产品</a></li>
    <li><span>您可以进行密码修改、账户设置等操作</span><a class="ibtn">账户管理</a></li>
    </ul>
    
    <div class="xline"></div>
    
    <div class="uimakerinfo"><b>查看Uimaker网站使用指南，您可以了解到多种风格的B/S后台管理界面,软件界面设计，图标设计，手机界面等相关信息</b>(<a href="http://www.5imoban.net" target="_blank">我爱模板网</a>)</div>
    
    <ul class="umlist">
    <li><a href="#">如何发布文章</a></li>
    <li><a href="#">如何访问网站</a></li>
    <li><a href="#">如何管理广告</a></li>
    <li><a href="#">后台用户设置(权限)</a></li>
    <li><a href="#">系统设置</a></li>
    </ul>
    
    
    </div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/index.js"></script>
</body>
</html>