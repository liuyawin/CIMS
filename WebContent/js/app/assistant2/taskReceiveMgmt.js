var app = angular
		.module(
				'taskReceiveMgmt',
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
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/newTask',
									{
										templateUrl : '/CIMS/jsp/assistant2/taskReceiveInformation/taskList.html',
										controller : 'TaskController'
									})
							.when(
									'/unfinishTask',
									{
										templateUrl : '/CIMS/jsp/assistant2/taskReceiveInformation/taskList.html',
										controller : 'TaskController'
									})
							.when(
									'/finishTask',
									{
										templateUrl : '/CIMS/jsp/assistant2/taskReceiveInformation/taskList.html',
										controller : 'TaskController'
									})
							.when(
									'/taskAdd',
									{
										templateUrl : '/CIMS/jsp/assistant2/taskReceiveInformation/taskAdd.html',
										controller : 'TaskController'
									})
							.when(
									'/taskCheck',
									{
										templateUrl : '/CIMS/jsp/assistant2/taskReceiveInformation/taskCheck.html',
										controller : 'TaskController'
									});
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getTaskList = function(data) {
		console.log("发送请求获取合同信息data" + data);
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByState.do',
			data : data
		});
	};

	services.getTaskByContext = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByContext.do',
			data : data
		});
	};

	services.selectAllUsers = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectAllUsers.do',
			data : data
		});
	};

	services.addTask = function(data) {
		console.log("发送请求添加任务");
		return $http({
			method : 'post',
			url : baseUrl + 'task/createNormalTask.do',
			data : data,
		});
	};

	services.deleteTask = function(data) {
		console.log("发送请求删除任务");
		return $http({
			method : 'post',
			url : baseUrl + 'task/deleteTask.do',
			data : data,
		});
	};

	services.checkTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskById.do',
			data : data,
		});
	};
	return services;
} ]);

app.controller('TaskController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var taskHtml = $scope;
			var tState;

			taskHtml.task = {};
			// 根据内容查询任务列表
			taskHtml.getTaskByContext = function() {
				services.getTaskByContext({
					context : $("#tContent").val(),
					page : 1,
					taskState : tState,
					sendOrReceive : 1
				}).success(function(data) {
					console.log("根据内容获取任务列表成功！");
					taskHtml.tasks = data.list;
				});
			};

			// 添加任务
			taskHtml.addTask = function() {
				services.addTask({
					user_id : $scope.task.receiverId,
					task_content : $scope.task.task_content,
					task_type : $scope.task.task_type,
					task_stime : $scope.task.task_stime,
					task_etime : $scope.task.task_etime,
					cont_id : "",
					task_remark : $scope.task.task_content
				}).success(function(data) {
					console.log("根据内容获取任务列表成功！");
					if (data.result == "true") {
						alert("添加成功！");
					} else {
						alert("添加失败！");
					}

				});
			};

			// 将任务ID放到sessionStorage中
			taskHtml.getTaskId = function(task_id) {
				sessionStorage.setItem('taskId', task_id); // 存入一个值

			};

			// 显示提示框及删除功能的实现
			taskHtml.show = function(task_id) {

				$(".tip").fadeIn(200);

				$(".sure").click(function() {
					services.deleteTask({
						taskId : task_id
					}).success(function(data) {
						console.log("根据内容获取任务列表成功！");
						if (data == "true") {
							alert("删除成功！");
							$("#" + task_id + "").hide();
						} else {
							alert("删除失败！");
						}
						$(".tip").fadeOut(100);
					});
				});

				$(".cancel").click(function() {
					$(".tip").fadeOut(100);
				});

			};
			// 查看任务
			function checkTask() {
				services.checkTask({
					ID : sessionStorage.getItem('taskId')
				}).success(function(data) {
					taskHtml.task = data.task;
				});
			}

			// 获取所有用户
			function selectAllUsers() {
				services.selectAllUsers({}).success(function(data) {
					console.log("获取用户列表成功！");
					taskHtml.users = data;
				});
			}

			// 获取任务列表
			function getTaskList(taskState, page) {
				services.getTaskList({
					taskState : taskState,
					page : page,
					sendOrReceive : 1
				}).success(function(data) {
					taskHtml.tasks = data.list;
				});
			}
			// 换页
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

			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/newTask') == 0) {
					// contract.getContractList();
					tState = 0;
					services.getTaskList({
						taskState : 0,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});

				} else if ($location.path().indexOf('/unfinishTask') == 0) {
					// contract.getDebtContract();
					tState = 1;
					services.getTaskList({
						taskState : 1,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
				} else if ($location.path().indexOf('/finishTask') == 0) {
					// contract.getOverdueContract();
					tState = 2;
					services.getTaskList({
						taskState : 2,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
				} else if ($location.path().indexOf('/taskAdd') == 0) {
					selectAllUsers();
				} else if ($location.path().indexOf('/taskCheck') == 0) {
					checkTask();
				}
			}
			initData();
		} ]);

/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */