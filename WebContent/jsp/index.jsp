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
    <!-- <a href="#">帐号设置</a> -->
    </div>
 
    <div class="xline"></div>
    <br>
    <br>
    <div class="welinfo">
    <span><img src="${ctx}/images/dp.png" alt="提醒" /></span>
    <b>待处理任务</b>
    </div>
    
    <ul class="infolist">
    <li><span>待审核发票任务：</span><a href="#" class="">12</a></li>
    <li><span>文书任务：</span><a href="#" class="">12</a></li>
    <li><span>执行管控任务：</span><a href="#" class="">12</a></li>
    <li><span>发票任务：</span><a href="#" class="">12</a></li>
    <li><span>其他任务：</span><a href="#" class="">12</a></li>
    </ul>
    
    <div class="welinfo">
    <span><img src="${ctx}/images/dp.png" alt="提醒" /></span>
    <b>报警信息</b>
    </div>
    
    <ul class="infolist">
    <li><span>收款超时：</span><a href="#" class="">12</a></li>
    <li><span>工程逾期：</span><a href="#" class="">13</a></li>
    <li><span>任务超时：</span><a href="#" class="">32</a></li>
    </ul>
    </div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/index.js"></script>
</body>
</html>