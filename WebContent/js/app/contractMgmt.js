var app = angular
		.module(
				'contract',
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
	$routeProvider.when('/test', {
		templateUrl : '/CIMS/jsp/contractInformation/test.html',
		controller : 'ContractController'
	}).when('/another', {
		templateUrl : '/CIMS/jsp/contractInformation/another.html',
		controller : 'ContractController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getConInfo = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getConInfo.do',
			data : data
		});
	};

	services.getStaffInfo = function(data) {
		console.log("发送请求获取员工信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getStaffInfo.do',
			data : data
		});
	};

	services.getConByName = function(data) {
		console.log("发送请求根据名字查找合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getConByName.do',
			data : data
		});
	}

	return services;
} ]);

app.controller('ContractController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;

			contract.getStaff = function() {
				console.log("getStaff进来了！");
				services.getStaffInfo({
					name : $("#name").val(),
					pwd : $("#pwd").val(),
					age : $("#email").val()
				}).success(function(data) {
					alert("Success");
					console.log(data.user2);
					contract.h_name = data.now;
					contract.h_password = data.he;
					console.log(contract.h_name);
					console.log(contract.h_password);
				});
			};

			contract.getConByName = function() {
				console.log("getConByName进来了！");

				services.getConByName({
					cName : $("#cName").val()
				}).success(function(data) {
					contract.contracts = data;
					console.log(contract.contracts);
				});

			};

			function initData() {
				console.log("执行ContractController");
				if ($location.path().indexOf('/test') == 0) {
					//在这里初始化页面
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