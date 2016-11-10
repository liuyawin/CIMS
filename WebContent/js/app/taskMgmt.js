var app = angular
		.module(
				'taskMgmt',
				[ 'ngRoute' ],
				function($httpProvider) {// ngRoute引入路由依赖
					$httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

					// Override $http service's default transformRequest
					$httpProvider.defaults.transformRequest = [ function(data) {
						/**
						 * The workhorse; converts an object to
						 * x-www-form-urlencoded serialization.
						 * 
						 * @param {Object}
						 *            obj
						 * @return {String}
						 */
						var param = function(obj) {
							var query = '';
							var name, value, fullSubName, subName, subValue, innerObj, i;

							for (name in obj) {
								value = obj[name];

								if (value instanceof Array) {
									for (i = 0; i < value.length; ++i) {
										subValue = value[i];
										fullSubName = name + '[' + i + ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value instanceof Object) {
									for (subName in value) {
										subValue = value[subName];
										fullSubName = name + '[' + subName
												+ ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value !== undefined
										&& value !== null) {
									query += encodeURIComponent(name) + '='
											+ encodeURIComponent(value) + '&';
								}
							}

							return query.length ? query.substr(0,
									query.length - 1) : query;
						};

						return angular.isObject(data)
								&& String(data) !== '[object File]' ? param(data)
								: data;
					} ];
				});
// 获取权限列表
var permissionList;
angular.element(document).ready(function() {
	console.log("获取权限列表！");
	$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data; // 
		console.log("身份是：" + permissionList);
		angular.bootstrap($("#ng-section"), [ 'taskMgmt' ]); // 手动加载angular模块
	});
});
app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/receiveTask', {
		templateUrl : '/CIMS/jsp/taskInformation/taskList.html',
		controller : 'TaskController'
	}).when('/sendTask', {
		templateUrl : '/CIMS/jsp/taskInformation/taskList.html',
		controller : 'TaskController'
	}).when('/invoiceTask', {
		templateUrl : '/CIMS/jsp/taskInformation/taskList.html',
		controller : 'TaskController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	// zq获取任务列表
	services.getTaskList = function(data) {
		console.log("发送请求获取合同信息data" + data);
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByState.do',
			data : data
		});
	};

	// zq获取任务列表
	services.redirectUrl = function() {
		console.log("发送请求获取合同信息data" + data);
		return $http({
			method : 'post',
			url : baseUrl + 'task/toZhurenContractPage.do',
		});
	};

	// zq通过内容查找任务
	services.getTaskByKeys = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByKeys.do',
			data : data
		});
	};
	// zq选择所有用户
	services.selectAllUsers = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getAllUserList.do',
			data : data
		});
	};
	// zq添加任务
	services.addTask = function(data) {
		console.log("发送请求添加任务");
		return $http({
			method : 'post',
			url : baseUrl + 'task/addTask.do',
			data : data,
		});
	};
	// zq删除任务
	services.deleteTask = function(data) {
		console.log("发送请求删除任务");
		return $http({
			method : 'post',
			url : baseUrl + 'task/deleteTask.do',
			data : data,
		});
	};
	// zq查看普通和监管任务
	services.checkTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskById.do',
			data : data,
		});
	};
	// zq完成任务
	services.finishTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'task/finishTask.do',
			data : data
		});
	};
	// 查看子任务
	services.selectSubTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'subTask/selectSubTask.do',
			data : data
		});
	};
	// 文书1完成子任务
	services.finishTask1 = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'subTask/updateState.do',
			data : data
		});
	};

	return services;
} ]);

app
		.controller(
				'TaskController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							// zq合同
							var taskHtml = $scope;
							var tState;
							var sendOrReceive;
							var taskPage = 1;
							var searchKey = null;
							// zq跳转合同页面，将合同ID存入sessionStorage中
							taskHtml.getConId = function(cont_id) {
								sessionStorage.setItem('conId', cont_id); // 存入合同ID
							};

							// zq将任务ID放到sessionStorage中
							taskHtml.getTaskId = function(task_id) {
								sessionStorage.setItem('taskId', task_id); // 存入一个值
							};
							taskHtml.task = {};
							// zq根据内容查询任务列表
							taskHtml.getTaskByKeys = function() {
								tState = taskHtml.tState;
								searchKey = null;
								services.getTaskByKeys({
									context : searchKey,
									page : 1,
									taskState : tState,
									sendOrReceive : sendOrReceive
								}).success(
										function(data) {
											console.log("根据内容获取任务列表成功！");
											taskHtml.tasks = data.list;
											pageTurnByContent(tState,
													data.totalPage, 1)
										});
							};

							// zq添加任务
							taskHtml.addTask = function() {
								selectAllUsers();
								$("#tipAdd").fadeIn(200);
								$(".overlayer").fadeIn(200);
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								taskHtml.task = {
									task_stime : timeNow,
									task_etime : timeNow
								};

							};
							taskHtml.addOneTask = function() {
								var taskFormData = JSON
										.stringify(taskHtml.task);
								var taskType = 0;
								console.log(taskFormData.task_type);
								services.addTask({
									task : taskFormData,
									conId : "",
									taskType : taskType
								}).success(function(data) {
									$("#tipAdd").fadeOut(100);
									$(".overlayer").fadeOut(200);
									alert("添加成功！");
									getTaskListByContent(tState, 1);
									taskHtml.task = "";
								});
							}

							$("#cancelAdd").click(function() {
								$("#tipAdd").fadeOut(100);
								$(".overlayer").fadeOut(200);
								taskHtml.task = ""

							});
							// 查看任务
							taskHtml.checkTask = function() {
								var taskId = this.t.task_id;
								var taskType = this.t.task_type;
								if (taskType == 0 || taskType == 2) {
									services
											.checkTask({
												ID : taskId
											})
											.success(
													function(data) {

														taskHtml.task = data.task;

														if (data.task.task_stime != null) {
															taskHtml.task.task_stime = changeDateType(data.task.task_stime.time);
														}
														if (data.task.task_etime != null) {
															taskHtml.task.task_etime = changeDateType(data.task.task_etime.time);
														}

														$(".overlayer").fadeIn(
																200);
														$("#tipCheck").fadeIn(
																200);
														getTaskListByContent(
																tState,
																taskPage);

													});
									$("#cancelCheck").click(function() {
										$("#tipCheck").fadeOut(100);
										$(".overlayer").fadeOut(200);
										taskHtml.task = "";
									});
								} else if (taskType == 1) {
									services
											.checkTask({
												ID : taskId
											})
											.success(
													function(data) {

														taskHtml.task = data.task;

														if (data.task.task_stime != null) {
															taskHtml.task.task_stime = changeDateType(data.task.task_stime.time);
														}
														if (data.task.task_etime != null) {
															taskHtml.task.task_etime = changeDateType(data.task.task_etime.time);
														}
													});
									services
											.selectSubTask({
												taskId : taskId
											})
											.success(
													function(data) {

														initState();
														getTaskListByContent(
																tState,
																taskPage);
														$scope.fchat.taskId = taskId;
														taskHtml.subTasks = data.list;
														for (var i = 0; i < data.list.length; i++) {

															switch (data.list[i].suta_content) {
															case 'print':
																if (data.list[i].suta_state == '1') {
																	$scope.fchat.task1.print = "true";// 选中已经完成的任务
																	taskHtml.disPrint = true;
																}

																$scope.fchat.subTaskId.print = data.list[i].suta_id;
																taskHtml.taskPrint = true;// 显示小任务
																break;
															case 'sign':
																if (data.list[i].suta_state == '1') {
																	$scope.fchat.task1.sign = "true";
																	taskHtml.disSign = true;

																}

																$scope.fchat.subTaskId.sign = data.list[i].suta_id;
																taskHtml.taskSign = true;
																break;
															case 'seal':
																if (data.list[i].suta_state == '1') {
																	$scope.fchat.task1.seal = "true";
																	taskHtml.disSeal = true;
																}

																$scope.fchat.subTaskId.seal = data.list[i].suta_id;
																taskHtml.taskSeal = true;
																break;
															case 'post':
																if (data.list[i].suta_state == '1') {
																	$scope.fchat.task1.post = "true";
																	taskHtml.disPost = true;
																}

																$scope.fchat.subTaskId.post = data.list[i].suta_id;
																taskHtml.taskPost = true;
																break;
															case 'file':
																if (data.list[i].suta_state == '1') {
																	$scope.fchat.task1.file = "true";
																	taskHtml.disFile = true;
																}

																$scope.fchat.subTaskId.file = data.list[i].suta_id;
																taskHtml.taskFile = true;
																break;
															}
														}
													});
									// 这里添加一个方法修改标志位，将该合同的任务由新接收任务改为未完成任务
									$(".overlayer").fadeIn(200);
									$(".tip1").fadeIn(200);
									$(".tiptop a").click(function() {
										$(".overlayer").fadeOut(200);
										$(".tip1").fadeOut(200);
									});

									$(".sure").click(function() {

										$(".overlayer").fadeOut(100);
										$(".tip1").fadeOut(100);
									});

									$(".cancel").click(function() {
										/*
										 * sessionStorage.setItem("contractId",
										 * "");
										 */
										$(".overlayer").fadeOut(100);
										$(".tip1").fadeOut(100);
									});
								}

							};
							// 文书1完成子任务
							taskHtml.finishTask1 = function() {

								var task = JSON.stringify($scope.fchat);
								console.log(task);
								services.finishTask1({
									task : task
								}).success(function(data) {
									alert("操作成功！");
								});
							}
							// 显示提示框及删除功能的实现 删除任务
							taskHtml.delTask = function() {
								var taskId = this.t.task_id;
								sessionStorage.setItem("taskId", taskId);
								$("#tipDel").fadeIn(200);
								$(".overlayer").fadeIn(200);

							};
							$("#sureDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
								services.deleteTask({
									taskId : sessionStorage.getItem('taskId')
								}).success(function(data) {
									console.log("根据内容获取任务列表成功！");
									alert("删除成功！");
									getTaskListByContent(tState, taskPage);

								});
							});

							$("#cancelDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});

							// zq完成任务确认
							taskHtml.finishTask = function() {
								var taskId = this.t.task_id;
								sessionStorage.setItem("taskId", taskId);
								$("#tipFinish").fadeIn(200);
								$(".overlayer").fadeIn(200);
							};
							$("#sureFinishTask").click(function() {
								$("#tipFinish").fadeOut(100);
								$(".overlayer").fadeOut(200);
								services.finishTask({
									taskId : sessionStorage.getItem("taskId")
								}).success(function(data) {
									alert("任务完成!");
									getTaskListByContent(tState, taskPage);
								});

							});

							$("#cancelFinishTask").click(function() {
								$("#tipFinish").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});

							// 更改任务时间的格式
							function changeDateType(time) {

								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
							}
							// zq获取所有用户
							function selectAllUsers() {
								services.selectAllUsers({}).success(
										function(data) {
											console.log("获取用户列表成功！");
											taskHtml.users = data;
										});
							}

							/*
							 * // zq获取所有任务列表 function getTaskList(taskState,
							 * page) { services.getTaskList({ taskState :
							 * taskState, page : page, sendOrReceive :
							 * sendOrReceive }).success(function(data) {
							 * taskHtml.tasks = data.list; }); } // zq所有任务换页
							 * function pageTurn(taskState, totalPage, page) {
							 * 
							 * var $pages = $(".tcdPageCode");
							 * console.log($pages.length); if ($pages.length !=
							 * 0) { $(".tcdPageCode").createPage({ pageCount :
							 * totalPage, current : page, backFn : function(p) {
							 * taskPage = p; getTaskList(taskState, p) } }); } }
							 */

							// zq 获取任务列表按照内容翻页查找函数
							function getTaskListByContent(taskState, page) {
								services.getTaskByKeys({
									context : searchKey,
									page : page,
									taskState : taskState,
									sendOrReceive : sendOrReceive
								}).success(function(data) {
									console.log("根据内容获取任务列表成功！");
									taskHtml.tasks = data.list;

								});
							}

							// zq按查询内容获取任务列表的换页
							function pageTurnByContent(taskState, totalPage,
									page) {
								var $pages = $(".tcdPageCode");
								console.log($pages.length);
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											taskPage = p;
											getTaskListByContent(taskState, p);
										}
									});
								}
							}
							/*
							 * $scope.$on('hideElement', function(hideElement) {
							 * if (tState != 1) { $(".finishLabel").hide(); } if
							 * (sendOrReceive == 1) { $(".delLabel").hide(); }
							 * 
							 * });
							 */
							// 李文书新添
							function initState() {
								$scope.fchat = new Object();

								$scope.fchat.task1 = {
									"print" : "",
									"sign" : "",
									"file" : "",
									"seal" : "",
									"post" : ""
								}
								$scope.fchat.subTaskId = {
									"print" : "",
									"sign" : "",
									"file" : "",
									"seal" : "",
									"post" : ""
								}
								$scope.fchat.taskId = "";
								taskHtml.disPrint = false;
								taskHtml.disSign = false;
								taskHtml.disSeal = false;
								taskHtml.disPost = false;
								taskHtml.disFile = false;

								taskHtml.taskPrint = false;
								taskHtml.taskSign = false;
								taskHtml.taskSeal = false;
								taskHtml.taskPost = false;
								taskHtml.taskFile = false;
							}
							function findRoleFromCookie() {
								var cookie = {};

								var cookies = document.cookie;
								if (cookies === "")
									return cookie;
								var list = cookies.split(";");
								for (var i = 0; i < list.length; i++) {
									var cookieString = list[i];
									/* console.log("cookie内容" + cookieString); */
									var p = cookieString.indexOf("=");
									var name = cookieString.substring(0, p);
									var value = cookieString.substring(p + 1,
											cookieString.length);
									console.log(name);
									cookie[name.trim()] = value;
									console.log("进来了,已经赋值" + name);
									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
									}

								}
							}
							// zq初始化
							function initData() {
								$(".tiptop a").click(function() {
									sessionStorage.setItem("contractId", "");
									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});
								console.log("初始化页面信息");
								if ($location.path().indexOf('/receiveTask') == 0) {
									tState = "-1";
									taskHtml.tState = "-1";
									sendOrReceive = 1;
									searchKey = null;
									sessionStorage
											.setItem("sendOrReceive", "1");
									services.getTaskByKeys({
										taskState : tState,
										page : 1,
										sendOrReceive : sendOrReceive,
										context : searchKey
									}).success(
											function(data) {
												taskHtml.tasks = data.list;
												pageTurnByContent(tState,
														data.totalPage, 1)
											});

								} else if ($location.path()
										.indexOf('/sendTask') == 0) {
									tState = "-1";
									taskHtml.tState = "-1";
									sendOrReceive = 0;
									searchKey = null;
									sessionStorage
											.setItem("sendOrReceive", "0");
									services.getTaskByKeys({
										taskState : tState,
										page : 1,
										sendOrReceive : sendOrReceive,
										context : searchKey
									}).success(
											function(data) {
												taskHtml.tasks = data.list;
												pageTurnByContent(tState,
														data.totalPage, 1);
											});
								}
							}
							initData();
							findRoleFromCookie();
							var $dateFormat = $(".dateFormat");
							var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
							$(".dateFormat").blur(
									function() {
										if (!dateRegexp.test(this.value)) {
											$(this).parent().children("span")
													.css('display', 'inline');
										}
									});
							$(".dateFormat").click(
									function() {
										$(this).parent().children("span").css(
												'display', 'none');
									});

						} ]);

// zq合同状态过滤器
app.filter('taskType', function() {
	return function(input) {
		var type = "";
		/*
		 * switch(input){ case "0":state="在建"; break; case "1":state="竣工";
		 * break; case "2":state="停建"; break; }
		 */
		if (input == "0")
			type = "普通任务";
		else if (input == "1")
			type = "文书任务";
		else if (input == "2")
			type = "补录合同任务";
		else if (input == "3")
			type = "其他";
		return type;
	}
});
//zq合同状态过滤器
app.filter('taskState', function() {
	return function(input) {
		var type = "";
		/*
		 * switch(input){ case "0":state="在建"; break; case "1":state="竣工";
		 * break; case "2":state="停建"; break; }
		 */
		if (input == "0")
			type = "待接收";
		else if (input == "1")
			type = "执行中";
		else if (input == "2")
			type = "已完成";
		else if (!input)
			type = "";
		return type;
	}
});

// zq自定义表单验证日期格式
app.directive("dateFormat", function() {
	return {
		restrict : 'A',
		require : 'ngModel',
		scope : true,
		link : function(scope, elem, attrs, controller) {
			var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;

			// Model变化时执行
			// 初始化指令时BU执行
			scope.$watch(attrs.ngModel, function(val) {
				if (!val) {
					return;
				}
				if (!dateRegexp.test(val)) {
					controller.$setValidity('dateformat', false);
				} else {
					controller.$setValidity('dateformat', true);
				}
			});
		}
	}
});

// 截取任务内容
app.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input != "") {
			var shortInput = input.substr(0, 8);
			content = shortInput + "……";
		} else {
			content = "无";
		}

		return content;
	}
});
/*
 * app.directive('onFinishRenderFilters', function($timeout) { return { restrict :
 * 'A', link : function(scope, element, attr) { if (scope.$last === true) {
 * $timeout(function() { scope.$emit('hideElement'); }); } } };
 * 
 * });
 */
// 时间的格式化的判断
app.filter('dateType', function() {
	return function(input) {
		var type = "";
		if (input != null) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		}

		return type;
	}
});
app.directive('taskFinish', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			// 任务状态,0：未接收，1：执行中，2：已完成
			// 任务类型, 0：普通任务，1：文书任务，2补录合同任务，3、其他
			// 任务:1表示接受的任务，0表示发出的任务
			var key = attr.taskFinish.trim(); // 获取页面上的权限值
			var strs = new Array(); // 定义一数组
			var sendOrReceive = sessionStorage.getItem("sendOrReceive");
			var state = null;
			var type = null;
			strs = key.split(","); // 字符分割
			state = strs[0];
			type = strs[1];
			if (sendOrReceive == "1") {
				if (type == "1") {
					element.css("display", "none");
				} else {
					if (state == "2") {
						element.css("display", "none");
					}
				}
			} else if (sendOrReceive == "0") {
				element.css("display", "none");
			}

		}
	};

});
app.directive('taskDelete', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			// 任务状态,0：未接收，1：执行中，2：已完成
			// 任务类型, 0：普通任务，1：文书任务，2补录合同任务，3、其他
			// 任务:1表示接受的任务，0表示发出的任务

			var sendOrReceive = sessionStorage.getItem("sendOrReceive");
			if (sendOrReceive == "1") {
				element.css("display", "none");
			}
		}
	};

});
app
		.directive(
				'hasPermission',
				function($timeout) {
					return {
						restrict : 'A',
						link : function(scope, element, attr) {

							var key = attr.hasPermission.trim(); // 获取页面上的权限值
							console.log("获取页面上的权限值" + key);
							/* console.log("cookie内容" + JSON.stringify(cookie)); */
							/*
							 * if (sessionStorage.getItem('userRole').trim() ==
							 * "3") { element.css("display", "none"); }
							 */
							switch (sessionStorage.getItem('userRole').trim()) {
							case "1":
								var keys1 = " cBodyEdit cPsAdd cPsEdit cPsDel cRnAdd cRnEdit cRnDel bReceAdd tContCollect tInvoFinish bInvoAdd cAdd cHeadEdit cDel cTaskAdd tInvoAudit tContDetail ";
								var regStr1 = "\\s" + key + "\\s";
								var reg1 = new RegExp(regStr1);
								if (keys1.search(reg1) < 0) {
									element.css("display", "none");
								}
								break;
							case "2":
								var keys2 = " tContDetail ";
								var regStr2 = "\\s" + key + "\\s";
								var reg2 = new RegExp(regStr2);
								if (keys2.search(reg2) < 0) {
									element.css("display", "none");
								}
								break;
							case "3":
								var keys3 = " cBodyEdit cPsAdd cPsEdit cPsDel cRnAdd cRnEdit cRnDel bReceAdd tContCollect tInvoFinish ";
								var regStr3 = "\\s" + key + "\\s";
								var reg3 = new RegExp(regStr3);
								if (keys3.search(reg3) < 0) {
									element.css("display", "none");
								}
								break;
							case "4":
								var keys4 = " bInvoAdd tContDetail ";
								var regStr4 = "\\s" + key + "\\s";
								var reg4 = new RegExp(regStr4);
								if (keys4.search(reg4) < 0) {
									element.css("display", "none");
								}
								break;
							case "5":
								var keys5 = " cAdd cHeadEdit cDel cTaskAdd tInvoAudit tContDetail ";
								var regStr5 = "\\s" + key + "\\s";
								var reg5 = new RegExp(regStr5);
								if (keys5.search(reg5) < 0) {
									element.css("display", "none");
								}
								break;
							}
						}
					};

				});
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */