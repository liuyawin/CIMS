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
	$routeProvider.when('/contractList', {
		templateUrl : '/CIMS/jsp/zhuren/contractInformation/contract.html',
		controller : 'ContractController'
	}).when('/debtContract', {
		templateUrl : '/CIMS/jsp/zhuren/contractInformation/contract.html',
		controller : 'ContractController'
	}).when('/overdueContract', {
		templateUrl : '/CIMS/jsp/zhuren/contractInformation/contract.html',
		controller : 'ContractController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getContractList = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getContractList.do',
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
				if ($location.path().indexOf('/contractList') == 0) {
					// contract.getContractList();
					contract.contracts = [ {
						name : "所有合同1",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "所有合同2",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "所有合同3",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "所有合同4",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					} ];
				} else if ($location.path().indexOf('/debtContract') == 0) {
					// contract.getDebtContract();
					contract.contracts = [ {
						name : "欠款合同1",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "欠款合同2",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "欠款合同3",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "欠款合同4",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					} ];
				} else if ($location.path().indexOf('/overdueContract') == 0) {
					// contract.getOverdueContract();
					contract.contracts = [ {
						name : "逾期合同1",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "逾期合同2",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "逾期合同3",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
					}, {
						name : "逾期合同4",
						number : "89757",
						state : "已完成",
						alertTimes : "5"
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