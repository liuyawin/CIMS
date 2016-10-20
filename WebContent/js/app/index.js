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

// // 路由配置
// app.config([ '$routeProvider', function($routeProvider) {
// $routeProvider.when('/*', {
// controller : 'IndexController'
// }).otherwise({
// controller : 'IndexController'
// })
// } ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getInitData = function() {
		return $http({
			method : 'post',
			url : baseUrl + 'login/getInitData.do',
		});
	};
	return services;
} ]);

app.controller('IndexController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			var index = $scope;
			// 初始化页面信息
			function initData() {
				console.log("初始化页面！");
				services.getInitData().success(function(data) {
					index.waitAuditBillTaskNum = data.waitAuditBillTaskNum;
					index.assistantTaskNum = data.assistantTaskNum;
					index.managerControlTaskNum = data.managerControlTaskNum;
					index.billTaskNum = data.billTaskNum;
					index.otherTaskNum = data.otherTaskNum;
					index.debtAlarmNum = data.debtAlarmNum;
					index.overdueAlarmNum = data.overdueAlarmNum;
					index.taskAlarmNum = data.taskAlarmNum;
				});
			}
			initData();
		} ]);
