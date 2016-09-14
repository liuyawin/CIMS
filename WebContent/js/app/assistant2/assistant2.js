var app = angular
		.module(
				'assistant2',
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
	$routeProvider.when('/newTask', {
		templateUrl : '/CIMS/jsp/assistant2/taskInformation/taskList.html',
		controller : 'ContractController'
	}).when('/unfinishTask', {
		templateUrl : '/CIMS/jsp/assistant2/taskInformation/taskList.html',
		controller : 'ContractController'
	}).when('/finishTask', {
		templateUrl : '/CIMS/jsp/assistant2/taskInformation/taskList.html',
		controller : 'ContractController'
	}).when('/taskAdd', {
		templateUrl : '/CIMS/jsp/assistant2/taskInformation/taskAdd.html',
		controller : 'ContractController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getTaskList = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByState.do',
			data : data
		});
	};

	services.getDebtContract = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getDebtContract.do',
			data : data
		});
	};

	services.getOverdueContract = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getOverdueContract.do',
			data : data
		});
	};

	return services;
} ]);

app.controller('ContractController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;
			// 获取合同列表
			contract.getContractList = function() {
				services.getContractList({}).success(function(data) {
					console.log("获取合同列表成功！");
					contract.contracts = data;
				});
			};
			// 获取欠款合同
			contract.getDebtContract = function() {
				services.getDebtContract({}).success(function(data) {
					console.log("获取欠款合同成功！");
					contract.contracts = data;
				});
			}
			// 获取逾期合同
			contract.getOverdueContract = function() {
				services.getOverdueContract({}).success(function(data) {
					console.log("获取逾期合同成功！");
					contract.contracts = data;
				});
			};

			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/newTask') == 0) {
					// contract.getContractList();
					services.getTaskList({taskState: 0 }).success(function(data) {
						console.log("获取合同列表成功！");
						contract.tasks = data;
					});
					
					/*contract.tasks = [ {
						task_content : "新任务1...",
						task_type : "文书任务",
						task_state : "未接收",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同1"
					}, {
						task_content : "新任务2...",
						task_type : "文书任务",
						task_state : "未接收",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同1"
					}, {
						task_content : "新任务3...",
						task_type : "文书任务",
						task_state : "未接收",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同1"
					}, {
						task_content : "新任务4...",
						task_type : "文书任务",
						task_state : "未接收",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同1"
					} ];*/
				} else if ($location.path().indexOf('/unfinishTask') == 0) {
					// contract.getDebtContract();
					contract.tasks =[ {
						task_content : "待处理1...",
						task_type : "文书任务",
						task_state : "待处理",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同2"
					}, {
						task_content : "待处理2...",
						task_type : "文书任务",
						task_state : "待处理",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同2"
					}, {
						task_content : "待处理3...",
						task_type : "文书任务",
						task_state : "待处理",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同2"
					}, {
						task_content : "待处理4...",
						task_type : "文书任务",
						task_state : "待处理",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同2"
					} ];
				} else if ($location.path().indexOf('/finishTask') == 0) {
					// contract.getOverdueContract();
					contract.tasks = [ {
						task_content : "已完成1...",
						task_type : "文书任务",
						task_state : "已完成",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同3"
					}, {
						task_content : "已完成2...",
						task_type : "文书任务",
						task_state : "已完成",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同3"
					}, {
						task_content : "已完成3...",
						task_type : "文书任务",
						task_state : "已完成",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同3"
					}, {
						task_content : "已完成4...",
						task_type : "文书任务",
						task_state : "已完成",
						creator : "主任",
						task_alarmnum : "4",
						contract:"合同3"
					} ];
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