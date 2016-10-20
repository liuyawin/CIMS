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
					href="/CIMS/alarm/toAlarmPage.do#/debtAlarmList"><b
					id="newsNum"></b></a>
				<div class="tip" style="height: 320px">
					<div class="formbody">
						<form id="addAlarm-form" ng-submit="addAlarm()" class="hidden">
							<ul class="forminfo">
								<li><i>收款超时</i><a href=""><b id="RnAlarmCnt"></b></a></li>
								<li><i>工程超时</i><a href=""><b id="PsAlarmCnt"></b></a></li>
								<li><i>任务超时</i><a href=""><b id="TskAlarmCnt"></b></a></li>
								<li><i>新任务</i><a href=""><b id="taskCnt"></b></a></li>
							</ul>
						</form>

					</div>
				</div>

			</div>

		</div>
	</header>
	<section class="containner">
		<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
		<script type="text/javascript">
			$(document).ready(
					function() {
						var cookie = {};
						var cookies = document.cookie;
						if (cookies === "")
							return cookie;
						var list = cookies.split(";");
						for (var i = 0; i < list.length; i++) {
							var cookieString = list[i];
							var p = cookieString.indexOf("=");
							var name = cookieString.substring(0, p);
							var value = cookieString.substring(p + 1,
									cookieString.length);
							console.log(name);
							console.log(value);
							cookie[name.trim()] = value;
						}
						$('#userNum').html(cookie.userNum);

						//$.getJSON("/CIMS/alarm/AlarmsTotalNum.do", {},
						//		function(data) {
						//			$("#newsNum").text(data.totalRow);
						//		});

						var msgCnt;
						window.setInterval(showalert, 5000);
						function showalert() {
							var lastMsgCnt = sessionStorage.getItem("msgCnt");
							$.getJSON("/CIMS/login/getInitData.do", {},
									function(data) {
										$("#taskCnt").text(
												data.totalReceiveTaskNum);
										$("#RnAlarmCnt")
												.text(data.debtAlarmNum);
										$("#PsAlarmCnt").text(
												data.overdueAlarmNum);
										$("#TskAlarmCnt").text(
												data.taskAlarmNum);
										msgCnt = 0 + data.totalReceiveTaskNum
												+ data.debtAlarmNum
												+ data.overdueAlarmNum
												+ data.taskAlarmNum;

										if (msgCnt > lastMsgCnt) {
											sessionStorage.setItem("msgCnt",
													msgCnt);
											//todo--提示爆闪

											window.setTimeout(show, 3000);//取消爆闪
										}

									});

						}
						function show() {
							//取消爆闪
						}
					});
		</script>