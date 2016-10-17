var app = angular
		.module(
				'index',
				[ 'ngRoute' ],
				function($httpProvider) {// ngRoute引入路由依赖
					$httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

					// Override $http service's default transformRequest
					$httpProvider.defaults.transformRequest = [ function(data) {					
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

// 路由配置
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/contractList',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
										controller : 'ContractController'
									})							
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getContractList = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getContractList.do',
			data : data
		});
	};

	return services;
} ]);

app.controller('IndexController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;
			// 获取合同列表
			contract.getContractList = function(page) {
				services.getContractList({
					page : page
				}).success(function(data) {
					contract.contracts = data.list;
					contract.totalPage = data.totalPage;
				});
			};
			
			// 初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/contractList') == 0) {// 如果是合同列表页
					contract.flag = 1;// 标志位，用于控制按钮是否显示
					services.getContractList({
						page : 1
					}).success(function(data) {
						// 合同列表分页
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						/* console.log(contract.totalPage); */
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getContractList(p);// 点击页码时获取第p页的数据
								}
							});
						}
						// 点击创建任务时弹出模态框
						contract.newTask = function(obj) {
							var conId = this.con.cont_id;
							services.getAllUsers().success(function(data) {
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
							if (contract.task.task_type == "1") {
								var task1 = JSON.stringify(contract.task1);
								console.log(task1);
								services.addTask({
									task : task1,
									taskType : "1",// 1代表文书任务
									conId : conId
								}).success(function(data) {
									alert("添加文书任务成功！");
								});
							} else if (contract.task.task_type == "0") {
								var task2 = JSON.stringify(contract.task2);
								console.log(task2);
								services.addTask({
									task : task2,
									taskType : "2",// 2代表执行管控任务
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
						});

						$(".taskType").change(function() {
							if (contract.task.task_type == "1") {
								$("#addTask1-form").slideDown(200);
								$("#addTask2-form").hide();
							} else if (contract.task.task_type == "0") {
								$("#addTask1-form").hide();
								$("#addTask2-form").slideDown(200);
							}
						});
					});

				} 
			}

			initData();
		} ]);
