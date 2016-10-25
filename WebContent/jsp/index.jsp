<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section class="main" ng-app="index" ng-controller="IndexController">
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
		</ul>
	</div>

	<div class="mainindex">
		<div class="welinfo">
			<span><img src="${ctx}/images/sun.png" alt="天气" /></span> <b>欢迎使用光电项目设计进度到款跟踪系统</b>
			<!-- <a href="#">帐号设置</a> -->
		</div>

		<div class="xline"></div>
		<br> <br>
		<div class="welinfo">
			<span><img src="${ctx}/images/dp.png" alt="提醒" /></span> <b>待处理的新任务</b>
		</div>

		<ul class="infolist">
			<li has-permission='waitAuditBillTaskNum'><a
				href="${ctx}/invoice/toBillMngInvoicePage.do#/invoiceTaskList"
				class="">待审核发票任务：{{waitAuditBillTaskNum}}</a></li>
			<li has-permission='assistantTaskNum'><a href="${ctx}/task/toTaskPage.do#/receiveTask" class="">文书任务：{{assistantTaskNum}}</a></li>
			<li has-permission='managerControlTaskNum'><a href="${ctx}/task/toTaskPage.do#/receiveTask" class="">执行管控任务：{{managerControlTaskNum}}</a></li>
			<li has-permission='billTaskNum'><a
				href="${ctx}/invoice/toBillMngInvoicePage.do#/invoiceTaskList"
				class="">发票任务： {{billTaskNum}}</a></li>
			<li><a href="${ctx}/task/toTaskPage.do#/receiveTask" class="">普通任务：{{otherTaskNum}}</a></li>
		</ul>

		<div class="welinfo">
			<span><img src="${ctx}/images/dp.png" alt="提醒" /></span> <b>报警信息</b>
		</div>

		<ul class="infolist">
			<li has-permission='debtAlarmNum'><a href="${ctx}/alarm/toAlarmPage.do#/debtAlarmList"
				class="">收款超时：{{debtAlarmNum}}</a></li>
			<li has-permission='overdueAlarmNum'><a href="${ctx}/alarm/toAlarmPage.do#/overdueAlarmList"
				class="">工程逾期：{{overdueAlarmNum}}</a></li>
			<li><a href="${ctx}/alarm/toAlarmPage.do#/taskAlarmList"
				class="">任务超时：{{taskAlarmNum}}</a></li>
		</ul>
	</div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/index.js"></script>
</body>
</html>