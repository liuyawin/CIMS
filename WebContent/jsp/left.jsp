<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<<<<<<< HEAD
<section class="leftbar">
	<dl class="leftmenu">
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
<<<<<<< HEAD
				<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="alarmFirst"
					href="${ctx}/alarm/toZhurenAlarmPage.do#/newAlarmList">待处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarm/toZhurenAlarmPage.do#/doneAlarmList">已处理</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>接收的任务
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="taskFirst"
					href="${ctx}/task/toZhurenTaskList.do#/newTask">新消息</a><i></i></li>
				<li><cite></cite> <a id="assistant2First"
					href="${ctx}/task/toZhurenTaskList.do#/unfinishTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toZhurenTaskList.do#/finishTask">已处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/invoice/toZhurenInvoicePage.do#/unInvoiceTaskList">待审核发票任务</a><i></i></li>

			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<a class="noColor"><span><img
						src="${ctx}/images/leftico02.png" /></span>发出的任务</a>
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/task/toZhurenTaskList.do#/newSendTask">未处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toZhurenTaskList.do#/unfinishSendTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toZhurenTaskList.do#/finishSendTask">已处理</a><i></i></li>

			</ul>
		</dd>
		<dd>

			<%-- <dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico02.png" /></span>执行过程管控
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="#">新建任务</a><i></i></li>
				<li><cite></cite> <a href="#">任务监督</a><i></i></li>
				<li><cite></cite> <a href="#">修改任务</a><i></i></li>
				<li><cite></cite> <a href="#">删除任务</a><i></i></li>
				<li><cite></cite> <a href="#">任务查询</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico03.png" /></span>合同执行记录管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="#">自定义</a><i></i></li>
				<li><cite></cite> <a href="#">常用资料</a><i></i></li>
				<li><cite></cite> <a href="#">信息列表</a><i></i></li>
				<li><cite></cite> <a href="#">其他</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>报表统计
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="#">自定义</a><i></i></li>
				<li><cite></cite> <a href="#">常用资料</a><i></i></li>
				<li><cite></cite> <a href="#">信息列表</a><i></i></li>
				<li><cite></cite> <a href="#">其他</a><i></i></li>
			</ul>

		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>系统权限管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="#">自定义</a><i></i></li>
				<li><cite></cite> <a href="#">常用资料</a><i></i></li>
				<li><cite></cite> <a href="#">信息列表</a><i></i></li>
				<li><cite></cite> <a href="#">其他</a><i></i></li>
			</ul>

		</dd>--%>
	</dl>

	<!-- 文书1导航 -->
	<dl class="leftmenu hideE" id="assistant1">
		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>李文书的任务边栏
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="assistant1First"
					href="${ctx}/task/toAssistant1TaskList.do#/newTaskList">新消息</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toAssistant1TaskList.do#/unfinishTaskList">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toAssistant1TaskList.do#/finishTaskList">已处理</a><i></i></li>
			</ul>
		</dd>
	</dl>

	<!-- 文书1导航完 -->

	<!-- 文书2的左侧列表始 -->
	<dl class="leftmenu hideE" id="assistant2">

		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>接收的任务
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="taskFirst"
					href="${ctx}/task/toTaskPage.do#/newTask">新消息</a><i></i></li>
				<li><cite></cite> <a id="assistant2First"
					href="${ctx}/task/toTaskPage.do#/unfinishTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/finishTask">已处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/invoice/toAssistant2InvoicePage.do#/unInvoiceTaskList">待处理发票任务</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/invoice/toAssistant2InvoicePage.do#/invoiceTaskList">已完成发票任务</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<a class="noColor"><span><img
						src="${ctx}/images/leftico02.png" /></span>发出的任务</a>
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/newSendTask">未处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/unfinishSendTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toTaskPage.do#/finishSendTask">已处理</a><i></i></li>

			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="alarmFirst"
					href="${ctx}/alarm/toAssistant2AlarmPage.do#/newAlarmList">待处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarm/toAssistant2AlarmPage.do#/doneAlarmList">已处理</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico04.png" /></span>合同信息管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="contractFirst"
					href="${ctx}/contract/toAssistant2ContractPage.do#/contractList">合同信息管理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toAssistant2ContractPage.do#/debtContract">欠款合同信息</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toAssistant2ContractPage.do#/overdueContract">工程逾期合同</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toAssistant2ContractPage.do#/test">合同处理记录</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toAssistant2ContractPage.do#/doneContract">终结合同信息</a><i></i></li>
				<li>
=======
				<span><img src="${ctx}/images/leftico01.png" /></span>任务管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="taskFirst"
					href="${ctx}/task/toTaskPage.do#/receiveTask">接收的任务</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/task/toTaskPage.do#/sendTask">发出的任务</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/invoice/toBillMngInvoicePage.do#/invoiceTaskList">发票任务</a><i></i></li>
>>>>>>> 9bf826fe5b107b5a9e22479ecc6b7d8ac1f1bfdc
			</ul>
		</dd>
		
	</dl>
	<dl class="leftmenu" id="admin">
		<dd>
			<div class="title">
<<<<<<< HEAD
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="alarmFirst"
					href="${ctx}/alarm/toManagerAlarmPage.do#/newAlarmList">待处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarm/toManagerAlarmPage.do#/doneAlarmList">已处理</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico04.png" /></span>合同信息管理
=======
				<span><img src="${ctx}/images/leftico02.png" /></span>用户管理
>>>>>>> 9bf826fe5b107b5a9e22479ecc6b7d8ac1f1bfdc
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
=======

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

>>>>>>> da472ba68fd16e2cfbfae0b01d2aa5a5dd45fea4
</section>
