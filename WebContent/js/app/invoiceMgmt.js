var invoiceApp = angular
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

invoiceApp.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		/* $rootScope.$broadcast('reGetData'); */
	});
} ]);

// 路由配置
invoiceApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/invoiceDetail', {
		templateUrl : '/CIMS/jsp/billInformation/invoiceDetail.html',
		controller : 'InvoiceController'
	}).when('/invoiceList', {
		templateUrl : '/CIMS/jsp/billInformation/invoiceList.html',
		controller : 'InvoiceController'
	}).when('/invoiceTaskList', {
		templateUrl : '/CIMS/jsp/billInformation/invoiceList.html',
		controller : 'InvoiceController'
	})
} ]);
invoiceApp.constant('baseUrl', '/CIMS/');
invoiceApp.factory('invoiceservices', [ '$http', 'baseUrl',
		function($http, baseUrl) {
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
			// 获取所有用户
			services.getAllUsers = function() {
				return $http({
					method : 'post',
					url : baseUrl + 'user/getAllUserList.do',
				});
			};
			services.updateInvoice = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'invoice/updateInvoice.do',
					data : data
				});
			}
			
			// zq查询待审核的发票任务
			services.getInvoiceList = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'invoice/getInvoiceList.do',
					data : data
				});
			};
			
			// zq查询待处理的发票任务
			services.getInvoTaskList = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'invoice/getInvoTaskList.do',
					data : data
				});
			};
			// zq完成发票任务
			services.updateInvoiceState = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'invoice/updateInvoiceState.do',
					data : data
				});
			};
			return services;
		} ]);

invoiceApp
		.controller(
				'InvoiceController',
				[
						'$scope',
						'invoiceservices',
						'$location',
						function($scope, services, $location) {
							// 合同
							var invoice = $scope;
							var role;
							var invoState;
							// zq查看合同ID，并记入sessione
							invoice.getContId = function(contId) {
								sessionStorage.setItem('conId', contId);
							};
							invoice.getInvoId = function(invoId) {
								sessionStorage.setItem('invoId', invoId);
							};
							// zq：读取合同的信息
							function selectContractById() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectContractById({
									cont_id : cont_id
								}).success(function(data) {
									invoice.cont = data;
								});
							}

							// zq：根据合同ID计算该合同目前开了多少钱的发票
							function countInvoiceMoneyByContId() {
								var contId = sessionStorage.getItem('conId');
								services.countInvoiceMoneyByContId({
									contId : contId
								}).success(function(data) {
									invoice.totalMoney = data.totalMoney;
								});
							}
							// 更改任务时间的格式
							function changeDateType(time) {

								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
							}
							// 根据发票ID查找发票
							function selectInvoiceById(invoId) {
								/*
								 * var invoId =
								 * sessionStorage.getItem('invoId');
								 */
								services.selectInvoiceById({
									invoiceId : invoId
								}).success(function(data) {
									invoice.invoice = data.invoice;
								});
							}
							// 根据发票ID查找发票
							invoice.selectInvoiceById = function() {
								var invoId = this.invo.invo_id;
								services
										.selectInvoiceById({
											invoiceId : invoId
										})
										.success(
												function(data) {
													invoice.invoice = data.invoice;
													if (data.invoice.invo_time != null) {
														invoice.invoice.invo_time = changeDateType(data.invoice.invo_time.time);
													} else {
														invoice.invoice.invo_time = "";
													}
													$(".overlayer").fadeIn(200);
													$("#tipCheck").fadeIn(200);
												});
								$("#cancelCheck").click(function() {
									$("#tipCheck").fadeOut(100);
									$(".overlayer").fadeOut(200);
									invoice.task = "";
								});
							}
							// 审核发票任务
							invoice.invoiceInfo = function() {
								var invoId = this.invo.invo_id;
								services.getAllUsers().success(function(data) {
									invoice.users = data;
								});
								selectInvoiceById(invoId);
								invoice.invoiceId = invoId;
								$(".auditInfo").show();

								$(".finishInfo").hide();
								$(".overlayer").fadeIn(200);
								$("#sendInvoTask").fadeIn(200);
								return false;
							};

							$(".tiptop a").click(function() {
								$(".overlayer").fadeOut(200);
								$("#sendInvoTask").fadeOut(200);
							});

							$("#sureSend").click(function() {
								services.updateInvoice({
									invoId : invoice.invoiceId,
									invoEtime : invoice.invoEtime,
									receiverId : invoice.receiverId
								}).success(function(data) {
									alert("操作成功！");
									getZRInvoice(1, invoState);
								});
								$(".overlayer").fadeOut(100);
								$("#sendInvoTask").fadeOut(100);
							});

							$("#cancelSend").click(function() {
								sessionStorage.setItem("contractId", "");
								$(".overlayer").fadeOut(100);
								$("#sendInvoTask").fadeOut(100);
							});

							// 完成发票任务
							invoice.updateInvoiceState = function() {
								var invoId = this.invo.invo_id;
								invoice.invoiceId = invoId;
								$(".auditInfo").hide();
								$(".finishInfo").show();
								$(".overlayer").fadeIn(200);
								$("#sendInvoTask").fadeIn(200);
								return false;
							}

							$("#sureFinishSend").click(function() {

								services.updateInvoiceState({
									invoiceId : invoice.invoiceId,
									invoTime : invoice.invoTime
								}).success(function(data) {
									alert("操作成功！");
									getWaitingDealInvoice(1, invoState);
								});
								$(".overlayer").fadeOut(100);
								$("#sendInvoTask").fadeOut(100);
							});
							function findRoleFromCookie() {
								var cookie = {};
								var cookies = document.cookie;
								if (cookies === "")
									return cookie;
								var list = cookies.split(";");
								for (var i = 0; i < list.length; i++) {
									var cookieString = list[i];
									/* console.log("cookie内容" + cookieString); */
									var p = cookieString.indexOf("=");
									var name = cookieString.substring(0, p);
									var value = cookieString.substring(p + 1,
											cookieString.length);

									cookie[name.trim()] = value;

									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
										role = value;
									}

								}
							}
							// zq换页
							function pageTurn(totalPage, page) {
								var $pages = $(".tcdPageCode");
								console.log($pages.length);
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											findInvoices(p);
										}
									});
								}
							}
							//为换页提供查找函数
							function findInvoices() {
								var invoListType = sessionStorage
										.getItem("invoListType");
								if (invoListType == "INVO") {
									services.getInvoiceList(
											{
												contId : sessionStorage
														.getItem("conId"),
												page : p,
												invoState : invoState
											}).success(function() {
										invoice.invoices = data.list;
										
									});
								} else if (invoListType == "INVOTASK") {
									services.getInvoTaskList({
										page : p,
										invoState : invoState
									}).success(function() {
										invoice.invoices = data.list;
										
									});
								}

							}
							//页面的查找函数
							invoice.selectInvoicesByState = function() {
								invoState = $("invoState").val();
								var invoListType = sessionStorage
										.getItem("invoListType");
								if (invoListType == "INVO") {
									services.getInvoiceList(
											{
												contId : sessionStorage
														.getItem("conId"),
												page : 1,
												invoState : invoState
											}).success(function() {
										invoice.invoices = data.list;
										pageTurn(data.totalPage, 1);
									});
								} else if (invoListType == "INVOTASK") {
									services.getInvoTaskList({
										page : 1,
										invoState : invoState
									}).success(function() {
										invoice.invoices = data.list;
										pageTurn(data.totalPage, 1);
									});
								}
							}
							// zq初始化页面信息
							function initData() {
								$(".tiptop a").click(function() {

									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});
								$("#invoice").show();
								$("#receipt").hide();
								$("#contract").hide();
								$("#receiveMoney").hide();
								console.log("初始化页面信息");
								if ($location.path().indexOf('/invoiceList') == 0) {// 如果是合同列表页
									sessionStorage.setItem("invoListType",
											"INVO");
									invoState = "-1";
									invoice.invoState = "-1";
									selectContractById();
									countInvoiceMoneyByContId();
									services.getInvoiceList(
											{
												contId : sessionStorage
														.getItem('conId'),
												page : p,
												invoState : invoState
											}).success(function(data) {
										invoice.invoices = data.list;
										pageTurn(data.totalPage, 1);
									});

								} else if ($location.path().indexOf(
										'/invoiceDetail') == 0) {
									selectInvoiceById();
								} else if ($location.path().indexOf(
										'/invoiceTaskList') == 0) {
									sessionStorage.setItem("invoListType",
											"INVOTASK");
									invoState = "-1";
									invoice.invoState = "-1";
									services.getInvoTaskList({
										page : p,
										invoState : invoState
									}).success(function(data) {
										invoice.invoices = data.list;
										pageTurn(data.totalPage, 1);
									});

								}
							}
							function dateformat() {
								var $dateFormat = $(".dateFormat");
								var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
								$(".dateFormat").blur(
										function() {
											if (!dateRegexp.test(this.value)) {
												$(this).parent().children(
														"span").css('display',
														'inline');
											}
										});
								$(".dateFormat").click(
										function() {
											$(this).parent().children("span")
													.css('display', 'none');
										});
							}
							findRoleFromCookie();
							initData();// 初始化

							dateformat();// 格式化日期格式
						} ]);

// 小数过滤器
invoiceApp.filter('invoFloat', function() {
	return function(input) {
		if (input == null) {
			var money = parseFloat('0').toFixed(2);
		} else {
			var money = parseFloat(input).toFixed(2);
		}
		return money;
	}
});

// 时间的格式化的判断
invoiceApp.filter('dateType', function() {
	return function(input) {
		var type = "";
		if (input != null) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		}

		return type;
	}
});

// 小数过滤器
invoiceApp.filter('receFloat', function() {
	return function(input) {
		var money = parseFloat(input).toFixed(2);
		return money;
	}
});
// 自定义表单验证日期格式
invoiceApp.directive("dateFormat", function() {
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
invoiceApp.filter('cutString', function() {
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
invoiceApp.filter('invoState', function() {
	return function(input) {
		var state = "";
		if (input == "0") {
			state = "待审核";
		}
		if (input == "1") {
			state = "待处理";
		}
		if (input == "2") {
			state = "已完成";
		}
		return state;
	}
});

invoiceApp.directive('hasPermission', function($timeout) {
	return {
		restrict : 'ECMA',
		link : function(scope, element, attr) {
			var key = attr.hasPermission.trim(); // 获取页面上的权限值
			console.log("获取页面上的权限值" + key);
			var keys = permissionList;
			console.log("获取后台的权限值" + keys);
			var regStr = "\\s" + key + "\\s";
			var reg = new RegExp(regStr);
			if (keys.search(reg) < 0) {
				element.css("display", "none");
			}
		}
	};
});