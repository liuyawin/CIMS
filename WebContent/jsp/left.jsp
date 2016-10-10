<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$.getJSON("/CIMS/login/getUserFromSession.do", function(data) {
			console.log(data);

			switch (data.user.role.role_name) {
			case '主任':
				$("#zhuren").show();
				break;
			case '李文书':
				$("#assistant1").show();
				break;
			case '周文书':
				$("#assistant2").show();
				break;
			case '设总':
				$("#manager").show();
				break;
			case 'admin':
				$("#admin").show();
				break;
			}
		});
	})
</script>
<section class="leftbar">
	<dl class="leftmenu hideE" id="zhuren">
		<dd>
			<div class="title ">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>合同信息管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="zhuRenFirst"
					href="${ctx}/contract/toZhurenContractPage.do#/contractList">合同信息管理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toZhurenContractPage.do#/debtContract">欠款合同信息</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toZhurenContractPage.do#/overdueContract">工程逾期合同</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toZhurenContractPage.do#/test">合同处理记录</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toZhurenContractPage.do#/test">终结合同信息</a><i></i></li>
				<li>
			</ul>
		</dd>
		<dd>
			<div class="title hideE">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
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

		</dd>
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
					href="${ctx}/invoice/toAssistant2InvoicePage.do#/invoiceTaskList">发票任务</a><i></i></li>
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
				<li><cite></cite> <a
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
			</ul>
		</dd>
	</dl>

	<!-- 文书2的左侧列表终 -->
	<!-- //设总 -->
	<dl class="leftmenu hideE" id="manager">

		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>接收的任务
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/task/toManagerTaskPage.do#/newTask">新消息</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toManagerTaskPage.do#/unfinishTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toManagerTaskPage.do#/finishTask">已处理</a><i></i></li>
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
					href="${ctx}/task/toManagerTaskPage.do#/newSendTask">未处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toManagerTaskPage.do#/unfinishSendTask">处理中</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/task/toManagerTaskPage.do#/finishSendTask">已处理</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico03.png" /></span>报警信息
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/alarm/toManagerAlarmPage.do#/newAlarmList">待处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarm/toManagerAlarmPage.do#/doneAlarmList">已处理</a><i></i></li>
			</ul>
		</dd>
		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico04.png" /></span>合同信息管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a id="mContractFirst"
					href="${ctx}/contract/toManagerContractPage.do#/contractList">合同信息管理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toManagerContractPage.do#/debtContract">欠款合同信息</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/contract/toManagerContractPage.do#/overdueContract">工程逾期合同</a><i></i></li>
				<%-- <li><cite></cite> <a href="${ctx}/contract/toManagerContractPage.do#/test">合同处理记录</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/contract/toManagerContractPage.do#/test">终结合同信息</a><i></i></li> --%>
				<li>
			</ul>
		</dd>
	</dl>

	<dl class="leftmenu hideE" id="admin">

		<dd>
			<div class="title">
				<!-- <a href="#/test"><span><img src="../../images/leftico01.png" /></span>合同信息管理</a> -->
				<span><img src="${ctx}/images/leftico01.png" /></span>部门管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/department/toDepartmentPage.do#/departmentList">部门列表</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/department/toDepartmentPage.do#/departmentAdd">添加部门</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/department/toDepartmentPage.do#/bulkImportStaff">批量导入员工</a><i></i></li>
				<li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico02.png" /></span>用户管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/roleList">角色列表</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/roleAdd">角色权限</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/userList">用户列表</a><i></i></li>
				<li><cite></cite> <a href="${ctx}/role/toRolePage.do#/userAdd">添加用户</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico03.png" /></span>报警设置
			</div>
			<ul class="menuson">
				<li><cite></cite> <a
					href="${ctx}/alarm/toAlarmPage.do#/newAlarmList">待处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarm/toAlarmPage.do#/doneAlarmList">已处理</a><i></i></li>
				<li><cite></cite> <a
					href="${ctx}/alarmLevel/toAlarmSetPage.do#/alarmSet">报警设置</a><i></i></li>
			</ul>
		</dd>

		<dd>
			<div class="title">
				<span><img src="${ctx}/images/leftico04.png" /></span>日志管理
			</div>
			<ul class="menuson">
				<li><cite></cite> <a href="#/journal">自定义</a><i></i></li>
			</ul>

		</dd>

	</dl>

</section>
