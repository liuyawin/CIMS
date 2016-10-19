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
			url : baseUrl + 'task/selectTaskByContext.do',
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
								
								services.getTaskBykeys({
									context : $("#tContent").val(),
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
								$("#sureAdd")
										.click(
												function() {
													var taskFormData = JSON
															.stringify(taskHtml.task);
													var taskType = 0;

													console
															.log(taskFormData.task_type);
													services
															.addTask(
																	{
																		task : taskFormData,
																		conId : "",
																		taskType : taskType
																	})
															.success(
																	function(
																			data) {

																		alert("添加成功！");
																		$(
																				"#tipAdd")
																				.fadeOut(
																						100);
																		$(
																				".overlayer")
																				.fadeOut(
																						200);
																	});
												});

								$("#cancelAdd").click(function() {
									$("#tipAdd").fadeOut(100);
									$(".overlayer").fadeOut(200);

								});
							};

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
													});
									$("#cancelCheck").click(function() {
										$("#tipCheck").fadeOut(100);
										$(".overlayer").fadeOut(200);
										taskHtml.task = "";
									});
								} else if (taskType == 1) {
									services
											.selectSubTask({
												taskId : taskId
											})
											.success(
													function(data) {

														initState();
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
								$("#tipDel").fadeIn(200);
								$(".overlayer").fadeIn(200);
								$("#sureDel").click(function() {
									services.deleteTask({
										taskId : taskId
									}).success(function(data) {
										console.log("根据内容获取任务列表成功！");
										alert("删除成功！");
										/* $("#" + taskId + "").hide(); */
										services.getTaskList({
											taskState : tState,
											page : 1,
											sendOrReceive : sendOrReceive
										}).success(function(data) {
											taskHtml.tasks = data.list;
											pageTurn(tState, data.totalPage, 1)
										});
										$("#tipDel").fadeOut(100);
										$(".overlayer").fadeOut(200);
									});
								});

								$("#cancelDel").click(function() {
									$("#tipDel").fadeOut(100);
									$(".overlayer").fadeOut(200);
								});

							};
							// zq完成任务确认
							taskHtml.finishTask = function() {
								var taskId = this.t.task_id;
								services.finishTask({
									taskId : taskId
								}).success(function(data) {
									alert("任务完成!");
									services.getTaskList({
										taskState : tState,
										page : 1,
										sendOrReceive : sendOrReceive
									}).success(function(data) {
										taskHtml.tasks = data.list;
										pageTurn(tState, data.totalPage, 1)
									});
								});

							};
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

							// zq获取所有任务列表
							function getTaskList(taskState, page) {
								services.getTaskList({
									taskState : taskState,
									page : page,
									sendOrReceive : sendOrReceive
								}).success(function(data) {
									taskHtml.tasks = data.list;

								});
							}
							// zq所有任务换页
							function pageTurn(taskState, totalPage, page) {

								var $pages = $(".tcdPageCode");
								console.log($pages.length);
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											getTaskList(taskState, p)
										}
									});
								}
							}

							// zq 获取任务列表按照内容翻页查找函数
							function getTaskListByContent(taskState, page) {
								services.getTaskByKeys({
									context : $("#tContent").val(),
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

							// zq初始化
							function initData() {

								console.log("初始化页面信息");
								if ($location.path().indexOf('/receiveTask') == 0) {
									// contract.getContractList();
									tState = 0;
									taskHtml.tState = "0";
									sendOrReceive = 1;
									services.getTaskList({
										taskState : tState,
										page : 1,
										sendOrReceive : sendOrReceive
									}).success(function(data) {
										taskHtml.tasks = data.list;
										pageTurn(0, data.totalPage, 1)
									});

								} else if ($location.path()
										.indexOf('/sendTask') == 0) {
									// contract.getOverdueContract();
									tState = 0;
									taskHtml.tState = "0";
									sendOrReceive = 0;
									services.getTaskList({
										taskState : tState,
										page : 1,
										sendOrReceive : sendOrReceive
									}).success(function(data) {
										taskHtml.tasks = data.list;
										pageTurn(0, data.totalPage, 1);
									});
								}
							}
							initData();
							var $dateFormat = $(".dateFormat");
							var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
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
			type = "其他";
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
			var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;

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
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */