<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<section class="leftbar">
	<dl class="leftmenu">
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
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/roleList">角色列表</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/userList">用户列表</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarmLevel/toAlarmSetPage.do#/alarmSet">报警设置</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>票据管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="${ctx}/contract/toBillMngContractPage.do#/contractList">票据管理</a><i></i></li>
			</ul>

		</dd>

	</dl>	
</section>
