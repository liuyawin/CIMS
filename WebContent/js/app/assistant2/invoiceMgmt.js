var app = angular
		.module(
				'invoice',
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
									'/receiptInfo',
									{
										templateUrl : '/CIMS/jsp/assistant2/receiptInformation/receiptInfo.html',
										controller : 'InvoiceController'
									})
							.when(
									'/invoiceList',
									{
										templateUrl : '/CIMS/jsp/assistant2/invoiceInformation/invoiceList.html',
										controller : 'InvoiceController'
									})
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};

	// zq根据合同ID获取发票列表
	services.selectInvoiceByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'invoice/selectInvoiceByContId.do',
			data : data
		});
	};
	// zq根据合同ID获取发票总金额
	services.countInvoiceMoneyByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'invoice/countInvoiceMoneyByContId.do',
			data : data
		});
	};

	return services;
} ]);

app.controller('InvoiceController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			// 合同
			var invoice = $scope;

			// zq查看合同ID，并记入sessione
			invoice.getContId = function(contId) {
				sessionStorage.setItem('contId', contId);
			};
			// zq：根据合同ID查找所有的发票信息
			function selectInvoiceByContId() {
				var contId = sessionStorage.getItem('contId');
				services.selectInvoiceByContId({
					page : 1,
					contId : contId
				}).success(function(data) {
					invoice.invoices = data.list;
				});
			}
			
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/invoiceList') == 0) {// 如果是合同列表页
					alert("成功了！");

				} else if ($location.path().indexOf('/receiptList') == 0) {
					
				}
			}
			function dateformat() {
				var $dateFormat = $(".dateFormat");
				var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
				$(".dateFormat").blur(
						function() {
							if (!dateRegexp.test(this.value)) {
								$(this).parent().children("span").css(
										'display', 'inline');
							}
						});
				$(".dateFormat").click(function() {
					$(this).parent().children("span").css('display', 'none');
				});
			}
			initData();// 初始化
			dateformat();// 格式化日期格式

		} ]);

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
});

/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */