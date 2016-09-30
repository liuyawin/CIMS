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
		/* $rootScope.$broadcast('reGetData'); */
	});
} ]);

// 路由配置
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/invoiceDetail',
									{
										templateUrl : '/CIMS/jsp/assistant2/invoiceInformation/invoiceDetail.html',
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
	// zq根据ID查找合同信息
	services.selectContractById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectContractById.do',
			data : data
		});
	};
	// zq根据合同ID获取发票已经开了多少钱的
	services.countInvoiceMoneyByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'invoice/countInvoiceMoneyByContId.do',
			data : data
		});
	};
	services.selectInvoiceById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'invoice/selectInvoiceById.do',
			data : data
		})
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
			invoice.getInvoId = function(invoId) {
				sessionStorage.setItem('invoId', invoId);
			};
			// zq：根据合同ID查找所有的发票信息
			function selectInvoiceByContId() {
				var contId = sessionStorage.getItem('contId');
				services.selectInvoiceByContId({
					page : 1,
					contId : contId
				}).success(function(data) {
					invoice.invoices = data.list;
					invoice.totalRow = data.totalRow;
				});
			}
			// zq：读取合同的信息
			function selectContractById() {
				var cont_id = sessionStorage.getItem('contId');
				services.selectContractById({
					cont_id : cont_id
				}).success(function(data) {
					invoice.cont = data;
				});
			}

			// zq：根据合同ID计算该合同目前开了多少钱的发票
			function countInvoiceMoneyByContId() {
				var contId = sessionStorage.getItem('contId');
				services.countInvoiceMoneyByContId({
					contId : contId
				}).success(function(data) {
					invoice.totalMoney = data.totalMoney;
				});
			}
			// 根据发票ID查找发票
			function selectInvoiceById() {
				var invoId = sessionStorage.getItem('invoId');
				services.selectInvoiceById({
					invoiceId : invoId
				}).success(function(data) {
					invoice.invoice = data.invoice;
				});
			}

			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/invoiceList') == 0) {// 如果是合同列表页

					selectContractById();
					countInvoiceMoneyByContId();
					selectInvoiceByContId();

				} else if ($location.path().indexOf('/invoiceDetail') == 0) {

					selectInvoiceById();
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

// 小数过滤器
app.filter('invoFloat', function() {
	return function(input) {
		if (input == null) {
			var money=parseFloat('0').toFixed(2);
		} else{
			var money = parseFloat(input).toFixed(2);
		}
		return money;
	}
});

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

// 小数过滤器
app.filter('receFloat', function() {
	return function(input) {
		var money = parseFloat(input).toFixed(2);
		return money;
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
// 截取任务内容
app.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input != "") {
			var shortInput = input.substr(0, 6);
			content = shortInput + "……";
		}

		return content;
	}
});
// 判断发票状态
app.filter('invoState', function() {
	return function(input) {
		var state = "";
		if (input == "0") {
			state="待审核";
		}
		if (input == "1") {
			state="待处理";
		}
		if (input == "2") {
			state="已完成";
		}
		return state;
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