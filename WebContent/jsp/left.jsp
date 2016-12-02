<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<section class="leftbar">
	<dl class="leftmenu">
		<!-- 合同信息管理 -->
		<dd id="contract" class="contManager" style="display: none">
			<div class="title ">
				<span><img src="${ctx}/images/leftico01.png" /></span>合同管理
			</div>
			<ul id="contract-ul" class="menuson">
				<li id="debtContract"><cite></cite> <a
					href="${ctx}/contract/toContractPage.do#/debtContract">欠款合同</a><i></i></li>
				<li id="overdueContract"><cite></cite> <a
					href="${ctx}/contract/toContractPage.do#/overdueContract">逾期合同</a><i></i></li>
				<li id="finishedContract"><cite></cite> <a
					href="${ctx}/contract/toContractPage.do#/finishedContract">竣工合同</a><i></i></li>
				<li id="buildContract"><cite></cite> <a
					href="${ctx}/contract/toContractPage.do#/contractList">在建合同</a><i></i></li>
				<li id="stopedContract"><cite></cite> <a
					href="${ctx}/contract/toContractPage.do#/stopedContract">停建合同</a><i></i></li>
			</ul>
		</dd>
		<!-- 任务管理 -->
		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico01.png" /></span>任务管理
			</div>
			<ul id="task-ul" class="menuson">
				<li id="receiveTask"><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/receiveTask">接收的任务</a><i></i></li>
				<li id="sendTask"><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/sendTask">发出的任务</a><i></i></li>

			</ul>
		</dd>

		<!-- 票据管理 -->
		<dd id="billInformation" class="billManager" style="display: none">
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>票据管理
			</div>
			<ul id="bill-ul" class="menuson">
				<li id="billMgmt"><cite></cite> <a
					href="${ctx}/contract/toBillMngContractPage.do#/contractList">票据管理</a><i></i></li>
				<li id="invoiceTask" class="invoiceTask" style="display: none"><cite></cite>
					<a href="${ctx}/contract/toBillMngContractPage.do#/invoiceTaskList">发票任务</a><i></i></li>
				<li id="receiveMoneyTask" class="remoTask" style="display: none"><cite></cite>
					<a
					href="${ctx}/contract/toBillMngContractPage.do#/receiveMoneyTaskList">到款任务</a><i></i></li>
			</ul>

		</dd>
		<!-- 报警信息-->
		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
			</div>
			<ul id="alarm-ul" class="menuson">
				<li id="debtAlarm" class="remoAlarm" style="display: none"><cite></cite>
					<a href="${ctx}/alarm/toAlarmPage.do#/debtAlarmList">收款相关</a><i></i></li>
				<li id="overdueAlarm" class="psAlarm" style="display: none"><cite></cite>
					<a href="${ctx}/alarm/toAlarmPage.do#/overdueAlarmList">工期相关</a><i></i></li>
				<li id="overtimeAlarm"><cite></cite> <a
					href="${ctx}/alarm/toAlarmPage.do#/taskAlarmList">任务超时</a><i></i></li>
			</ul>
		</dd>
		<!-- 基础信息管理 -->
		<dd id="userManagement" class="userManager" style="display: none">
			<div class="title">
				<span><img src="${ctx}/images/leftico02.png" /></span>用户管理
			</div>
			<ul id="system-ul" class="menuson">
				<li id="roleList"><cite></cite> <a
					href="${ctx}/role/toUserManagePage.do#/roleList">角色管理</a><i></i></li>
				<li id="userList"><cite></cite> <a
					href="${ctx}/role/toUserManagePage.do#/userList">用户管理</a><i></i></li>
				<li id="alarmSet"><cite></cite> <a
					href="${ctx}/alarmLevel/toAlarmSetPage.do#/alarmSet">报警设置</a><i></i></li>
			</ul>
		</dd>
		<!-- 报表统计 -->
		<dd id="reportForm" class="reportForm" style="display: none">
			<div class="title">
				<span><img src="${ctx}/images/leftico02.png" /></span>报表统计
			</div>
			<ul id="report-ul" class="menuson">
				<li id="remoAnalyzeList"><cite></cite> <a
					href="${ctx}/reportForm/toReportFormPage.do#/remoAnalyzeList">合同额到款分析表</a><i></i></li>
				<li id="paymentPlanList"><cite></cite> <a
					href="${ctx}/reportForm/toReportFormPage.do#/paymentPlanList">收款计划明细表</a><i></i></li>
				<li id="projectList"><cite></cite> <a
					href="${ctx}/reportForm/toReportFormPage.do#/projectList">分类明细表</a><i></i></li>
				<li id="unGetContList"><cite></cite> <a
					href="${ctx}/reportForm/toReportFormPage.do#/unGetContList">未返合同明细表</a><i></i></li>
				<li id="summarySheet"><cite></cite> <a
					href="${ctx}/reportForm/toReportFormPage.do#/summarySheet">项目明细表</a><i></i></li>
			</ul>
		</dd>
	</dl>
</section>
<script>
	$(document)
			.ready(
					function() {
						//根据权限显示左侧栏相关条目
						$
								.get(
										"/CIMS/login/getLeftbarPermission.do",
										function(data) {
											console.log("左侧栏权限：" + data);
											var leftbarPermission = data
													.substring(1,
															data.length - 2)
													.split(",");
											for (var i = 0, len = leftbarPermission.length; i < len; i++) {
												if (leftbarPermission[i].trim()) {
													var $temp = $('.'
															+ leftbarPermission[i]
																	.trim());
													if ($temp) {
														$temp.css('display',
																'block');
													}
												}

											}
										});
						//点击li时将当前页面的信息存入sessionStorage
						var $li = $('.leftmenu li');
						$li.click(function() {
							sessionStorage.setItem("currentPage", $(this).attr(
									'id'));
						});
					});
</script>
