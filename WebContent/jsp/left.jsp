<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<section class="leftbar">
	<dl class="leftmenu">
		<!-- 合同信息管理 -->
  		<dd>
  			<div class="title ">
  			<span><img src="${ctx}/images/leftico01.png" /></span>合同信息管理
  			</div>
  			<ul class="menuson">				
  				<li><cite></cite> <a href="${ctx}/contract/toContractPage.do#/debtContract">欠款合同</a><i></i></li>
  				<li><cite></cite> <a href="${ctx}/contract/toContractPage.do#/overdueContract">逾期合同</a><i></i></li>
  				<li><cite></cite> <a href="${ctx}/contract/toContractPage.do#/finishedContract">终结合同</a><i></i></li>
  				<li><cite></cite> <a href="${ctx}/contract/toContractPage.do#/contractList">所有合同</a><i></i></li>
			</ul>
		</dd>
  		<!-- 合同信息管理完 -->
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>任务管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="taskFirst"
					href="${ctx}/task/toTaskPage.do#/receiveTask">接收的任务</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/task/toTaskPage.do#/sendTask">发出的任务</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/invoice/toBillMngInvoicePage.do#/invoiceTaskList">发票任务</a><i></i></li>
			</ul>
		</dd>

	</dl>
	<dl class="leftmenu" id="admin">
		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico02.png" /></span>用户管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="${ctx}/role/toUserManagePage.do#/roleList">角色列表</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/role/toUserManagePage.do#/userList">用户列表</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarmLevel/toAlarmSetPage.do#/alarmSet">报警设置</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>票据管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/contract/toBillMngContractPage.do#/contractList">票据管理</a><i></i></li>
			</ul>

		</dd>

	</dl>

	<section class="leftbar">

		<!-- 报警列表信息栏-->
		<dl class="leftmenu">
			<dd>
				<div class="title">
					<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
				</div>
				<ul class="menuson">
					<li><cite></cite> <a
						href="${ctx}/alarm/toAlarmPage.do#/debtAlarmList">收款超时</a><i></i></li>
					<li><cite></cite> <a
						href="${ctx}/alarm/toAlarmPage.do#/overdueAlarmList">工程逾期</a><i></i></li>
					<li><cite></cite> <a
						href="${ctx}/alarm/toAlarmPage.do#/taskAlarmList">任务超时</a><i></i></li>
				</ul>
			</dd>

		</dl>


	</section>