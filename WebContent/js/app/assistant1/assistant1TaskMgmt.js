var app = angular
		.module(
				'assistant1TaskMgmt',
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
									'/newTaskList',
									{
										templateUrl : '/CIMS/jsp/assistant1/taskInformation/taskList.html',
										controller : 'TaskController'
									})
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

	
	return services;
} ]);

app.controller('TaskController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var taskHtml = $scope;
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
			// 初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/newTaskList') == 0) {// 如果是接受的新任务列表页
					tState = 0;
					services.getTaskList({
						taskState : 0,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
						//点击创建任务时弹出模态框
					/*taskHtml.newTask = function(obj) {
							var conId = this.con.cont_id;
							services.getAllUsers().success(function(data){
								contract.users = data;								
								console.log(conId);
								sessionStorage.setItem("contractId", conId);
							});	
							$(".overlayer").fadeIn(200);
							$(".tip").fadeIn(200);
							return false;
						};

						$(".tiptop a").click(function() {
							sessionStorage.setItem("contractId", "");
							$(".overlayer").fadeOut(200);
							$(".tip").fadeOut(200);
						});

						$(".sure").click(function() {
							var conId = sessionStorage.getItem("contractId");
							if(contract.task.task_type=="1"){
								var task1 = JSON.stringify(contract.task1);
								console.log(task1);
								services.addTask({
									task : task1,
									taskType : "1",//1代表文书任务
									conId : conId
								}).success(function(data) {
									alert("添加文书任务成功！");
								});
							}else if(contract.task.task_type=="0"){
								var task2 = JSON.stringify(contract.task2);
								console.log(task2);
								services.addTask({
									task : task2,
									taskType : "2",//2代表执行管控任务
									conId : conId
								}).success(function(data) {
									alert("添加执行管控任务成功！");
								});
							}
							$(".overlayer").fadeOut(100);
							$(".tip").fadeOut(100);
						});

						$(".cancel").click(function() {
							sessionStorage.setItem("contractId", "");
							$(".overlayer").fadeOut(100);
							$(".tip").fadeOut(100);
						});*/
						
						

				} else if ($location.path().indexOf('/unfinishTaskList') == 0) {//未完成任务
					// contract.getDebtContract();
					/*services.getDebtContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getDebtContract(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});*/
				} else if ($location.path().indexOf('/finishTaskList') == 0) {//已完成任务
					// contract.getOverdueContract();
					/*services.getOverdueContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getOverdueContract(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});*/
				} else if ($location.path().indexOf('/contractAdd') == 0) {
					// 这里先获取人员列表
					/*services.getAllUsers().success(function(data){
						contract.users = data;
						sessionStorage.setItem("contractId", "");
					});					
					var $select = $("select");
					for (var i = 0; i < $select.length; i++) {
						$select[i].options[0].selected = true;
					}
					$('select').prop('selectedIndex', 1);*/
				}
				
			}

			initData();
			// 验证日期输入格式
			/*var $dateFormat = $(".dateFormat");
			var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
			$(".dateFormat").blur(function() {
				if (!dateRegexp.test(this.value)) {
					$(this).parent().children("span").css('display', 'inline');
				}
			});
			$(".dateFormat").click(function() {
				$(this).parent().children("span").css('display', 'none');
			});*/
		} ]);
// 合同状态过滤器
/*app.filter('conState', function() {
	return function(input) {
		var state = "";
		
		 * switch(input){ case "0":state="在建"; break; case "1":state="竣工";
		 * break; case "2":state="停建"; break; }
		 
		if (input == "0")
			state = "在建";
		else if (input == "1")
			state = "竣工";
		else if (input == "2")
			state = "停建";
		return state;
	}
});
// 自定义表单验证日期格式
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
});*/
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */