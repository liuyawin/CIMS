var app = angular
		.module(
				'receipt',
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
										controller : 'ReceiptController'
									})
							.when(
									'/receiptList',
									{
										templateUrl : '/CIMS/jsp/assistant2/receiptInformation/receiptList.html',
										controller : 'ReceiptController'
									})
							.when(
									'/receiptAdd',
									{
										templateUrl : '/CIMS/jsp/assistant2/receiptInformation/receiptAdd.html',
										controller : 'ReceiptController'
									})
							.when(
									'/receiptDetail',
									{
										templateUrl : '/CIMS/jsp/assistant2/receiptInformation/receiptDetail.html',
										controller : 'ReceiptController'
									})
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	// zq根据ID查找合同信息
	services.selectContractById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectContractById.do',
			data : data
		});
	};

	// zq根据合同ID获取工期阶段
	services.selectPrstByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/selectPrstByContId.do',
			data : data
		});
	};
	// zq根据合同ID获取工期阶段
	services.selectRenoByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/selectRenoByContId.do',
			data : data
		});
	};
	// zq根据合同ID获取收据列表
	services.selectReceiptByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receipt/selectReceiptByContId.do',
			data : data
		});
	};
	// zq根据合同ID获取收据总金额
	services.countReceiptMoneyByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receipt/countReceiptMoneyByContId.do',
			data : data
		});
	};
	// zq根据合同和收款节点添加收据
	services.addReceipt = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receipt/createReceipt.do',
			data : data
		});
	};
	// zq根据收据ID查找详情
	services.selectReceiptByReceId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receipt/findByReceiptId.do',
			data : data
		});
	};

	return services;
} ]);

app
		.controller(
				'ReceiptController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							// 合同
							var receipt = $scope;

							// zq查看合同ID，并记入sessione
							receipt.getContId = function(contId) {
								sessionStorage.setItem('contId', contId);
							};
							// zq查看合同ID，并记入sessione
							receipt.getRenoId = function(renoId, contId) {
								sessionStorage.setItem('renoId', renoId);
								sessionStorage.setItem('contId', contId);
							};
							// zq查看合同ID，并记入sessione
							receipt.getReceId = function(receId) {
								sessionStorage.setItem('receId', receId);

							};

							receipt.showContInfo = function() {
								$('#contInformation').show();
								$('#contShow').hide();
								$('#contHide').show();
							}
							receipt.hideContInfo = function() {

								$('#contInformation').hide();
								$('#contShow').show();
								$('#contHide').hide();
							}
							receipt.showPrstInfo = function() {
								$('#prstInformation').show();
								$('#prstShow').hide();
								$('#prstHide').show();
							}
							receipt.hidePrstInfo = function() {

								$('#prstInformation').hide();
								$('#prstShow').show();
								$('#prstHide').hide();
							}
							// zq查看合同ID，并记入sessione
							receipt.addReceipt = function() {
								var renoId = this.node.reno_id;
								var contId = this.node.contract.cont_id;
								$(".overlayer").fadeIn(200);
								$("#tipAdd").fadeIn(200);
								$("#sureAdd")
										.click(
												function() {
													var receFormData = JSON
															.stringify(receipt.receipt);
													services
															.addReceipt(
																	{
																		receipt : receFormData,
																		renoId : renoId,
																		contId : contId
																	})
															.success(
																	function(
																			data) {

																		$(
																				"#tipAdd")
																				.fadeOut(
																						100);
																		$(
																				".overlayer")
																				.fadeOut(
																						200);
																		selectRenoByContId();
																		receipt.receipt = "";
																		alert("收据添加成功！");

																	});
												});

								$("#cancelAdd").click(function() {
									$("#tipAdd").fadeOut(100);
									$(".overlayer").fadeOut(200);
									receipt.receipt = "";
								});

							};
							// 根据收据ID查看收据的详情
							receipt.selectReceiptByReceId = function() {
								var receId = this.rece.rece_id;
								services
										.selectReceiptByReceId({
											receId : receId
										})
										.success(
												function(data) {
													receipt.receipt = data.receipt;
													if (data.receipt.rece_atime != null) {
														receipt.receipt.rece_atime = changeDateType(data.receipt.rece_atime.time);
													}
													$(".overlayer").fadeIn(200);
													$("#tipCheck").fadeIn(200);
												});
								$("#cancelCheck").click(function() {
									$("#tipCheck").fadeOut(100);
									$(".overlayer").fadeOut(100);

								});
							}
							// zq：读取合同的信息
							function selectContractById() {
								var cont_id = sessionStorage.getItem('contId');
								services.selectContractById({
									cont_id : cont_id
								}).success(function(data) {
									receipt.cont = data;
								});
							}
							// zq：根据合同ID查询工期阶段的内容
							function selectPrstByContId() {
								var cont_id = sessionStorage.getItem('contId');
								services.selectPrstByContId({
									cont_id : cont_id
								}).success(function(data) {
									receipt.prst = data.list;
								});
							}
							// zq：根据合同ID查询收款节点的内容
							function selectRenoByContId() {
								var cont_id = sessionStorage.getItem('contId');
								services.selectRenoByContId({
									cont_id : cont_id
								}).success(function(data) {
									receipt.reno = data.list;
								});
							}

							// zq：根据合同ID查找所有的收据
							function selectReceiptByContId(page) {
								var contId = sessionStorage.getItem('contId');
								services.selectReceiptByContId({
									page : page,
									contId : contId
								}).success(function(data) {
									receipt.receipts = data.list;
									receipt.totalRow = data.totalRow;

								});
							}

							// zq：根据合同ID计算该合同目前收据共多少钱
							function countReceiptMoneyByContId() {
								var contId = sessionStorage.getItem('contId');

								services.countReceiptMoneyByContId({
									contId : contId
								}).success(function(data) {

									receipt.totalMoney = data.totalMoney;

								});
							}
							// 更改任务时间的格式
							function changeDateType(time) {
								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
							}
							// zq所有换页
							function pageTurn(totalPage, page) {
								var $pages = $(".tcdPageCode");
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											selectReceiptByContId(p);
										}
									});
								}
							}
							// zq初始化页面信息
							function initData() {
								console.log("初始化页面信息");
								if ($location.path().indexOf('/receiptList') == 0) {

									var contId = sessionStorage
											.getItem('contId');
									services.selectReceiptByContId({
										page : 1,
										contId : contId
									}).success(function(data) {
										receipt.receipts = data.list;
										receipt.totalRow = data.totalRow;
										pageTurn(data.totalPage, 1);
									});
									countReceiptMoneyByContId();// 根据合同ID查找该合同的所有收款金额
									selectContractById();// 根据ID查找合同详情

								} else if ($location.path().indexOf(
										'/receiptInfo') == 0) {
									selectContractById();// 根据合同ID查看合同信息
									selectPrstByContId();// 根据合同查看工期阶段
									selectRenoByContId();// 根据合同ID查看收款节点
								} else if ($location.path().indexOf(
										'/receiptDetail') == 0) {
									selectReceiptByReceId();
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
							initData();// 初始化
							dateformat();// 格式化日期格式

						} ]);

// 小数过滤器
app.filter('receFloat', function() {
	return function(input) {
		if (input == null) {
			var money = parseFloat('0').toFixed(2);
		} else {
			var money = parseFloat(input).toFixed(2);
		}

		return money;
	}
});
// 合同状态过滤器
app.filter('conState', function() {
	return function(input) {
		var state = "";
		if (input == "0")
			state = "在建";
		else if (input == "1")
			state = "竣工";
		else if (input == "2")
			state = "停建";
		return state;
	}
});
// 合同立项判断
app.filter('conInitiation', function() {
	return function(input) {
		var initiation = "";
		if (input == "0")
			initiation = "否";
		else if (input == "1")
			initiation = "是";

		return initiation;
	}
});
// 合同是否有委托书判断
app.filter('conHasproxy', function() {
	return function(input) {
		var hasproxy = "";
		if (input == "0")
			hasproxy = "否";
		else if (input == "1")
			hasproxy = "是";

		return hasproxy;
	}
});
// 合同一般纳税人判断
app.filter('conAvetaxpayer', function() {
	return function(input) {
		var avetaxpayer = "";
		if (input == "0")
			avetaxpayer = "否";
		else if (input == "1")
			avetaxpayer = "是";

		return avetaxpayer;
	}
});
// 合同类型的判断
app.filter('conType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "规划";
		else if (input == "1")
			type = "可行性";
		else if (input == "2")
			type = "施工图";
		else if (input == "3")
			type = "评估";
		else if (input == "4")
			type = "其他";
		return type;
	}
});
// 工期阶段的判断
app.filter('prstType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "未完成";
		else if (input == "1")
			type = "已完成";

		return type;
	}
});
// 收款节点的状态的判断
app.filter('renoType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "未收款";
		else if (input == "1")
			type = "未付全款";
		else if (input == "2")
			type = "已付全款";
		else if (input == "3")
			type = "提前到款";
		return type;
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
// 等级的判断
app.filter('conRank', function() {
	return function(input) {
		var rank = "";
		if (input == "0")
			rank = "重要";
		else if (input == "1")
			rank = "一般";
		return rank;
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
			var shortInput = input.substr(0, 5);
			content = shortInput + "……";
		}

		return content;
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