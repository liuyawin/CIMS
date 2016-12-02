var receiptapp = angular
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
receiptapp.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
receiptapp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/receiptInfo', {
		templateUrl : '/CIMS/jsp/billInformation/receiptInfo.html',
		controller : 'ReceiptController'
	}).when('/receiptList', {
		templateUrl : '/CIMS/jsp/billInformation/receiptList.html',
		controller : 'ReceiptController'
	}).when('/receiptAdd', {
		templateUrl : '/CIMS/jsp/billInformation/receiptAdd.html',
		controller : 'ReceiptController'
	}).when('/receiptDetail', {
		templateUrl : '/CIMS/jsp/billInformation/receiptDetail.html',
		controller : 'ReceiptController'
	})
} ]);
receiptapp.constant('baseUrl', '/CIMS/');
receiptapp.factory('receiptservices', [ '$http', 'baseUrl',
		function($http, baseUrl) {
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
			// lwt根据合同添加收据
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
			// 删除收据
			services.delRece = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receipt/deleteReceipt.do',
					data : data
				});
			};
			return services;
		} ]);

receiptapp
		.controller(
				'ReceiptController',
				[
						'$scope',
						'receiptservices',
						'$location',
						function($scope, services, $location) {
							// 合同
							var receipt = $scope;
							var recePage=1;

							// zq查看合同ID，并记入sessione
							receipt.getContId = function(contId) {
								sessionStorage.setItem('conId', contId);
							};
							// zq查看收据节点、合同ID，并记入sessione
							receipt.getRenoId = function(renoId, contId) {
								sessionStorage.setItem('renoId', renoId);
								sessionStorage.setItem('conId', contId);
							};
							// zq查看收据ID，并记入sessione
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
							// lwt:开收据
							receipt.addReceipt = function() {
								var FIRM=receipt.cont.cont_client;
								$(".overlayer").fadeIn(200);
								$("#tipAddReceipt").fadeIn(200);
								// 输入时间的input默认值设置为当前时间
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								receipt.receipt = {
									rece_atime : timeNow,
									rece_firm:FIRM
								};

							};
							$(".tiptop a").click(function() {
								$(".overlayer").fadeOut(200);
								$(".tip").fadeOut(200);
							});
							receipt.addRece = function() {
								var receFormData = JSON
										.stringify(receipt.receipt);
								services.addReceipt({
									receipt : receFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									$("#tipAddReceipt").fadeOut(100);
									$(".overlayer").fadeOut(200);
									alert("收据添加成功！");
									receipt.receipt = "";
									findReceipts(1);
									countReceiptMoneyByContId();
								});
							}

							$("#cancelAddReceipt").click(function() {
								$("#tipAddReceipt").fadeOut(100);
								$(".overlayer").fadeOut(200);
								receipt.receipt = "";
							});
							// zq查看合同ID，并记入sessione
							/*
							 * receipt.addReceipt = function(renoId, contId) {
							 * var renoId = sessionStorage.getItem('renoId');
							 * var contId = sessionStorage.getItem('conId'); var
							 * receFormData = JSON.stringify(receipt.receipt);
							 * services.addReceipt({ receipt : receFormData,
							 * renoId : renoId, contId : contId
							 * }).success(function(data) { alert("收据添加成功！");
							 * window.history.back(); }); };
							 */

							receipt.checkReceipt = function() {
								var receId = this.rece.rece_id;
								services.selectReceiptByReceId({
									receId : receId
								}).success(function(data) {
									receipt.receipt = data.receipt
								});
								$("#tipCheckReceipt").fadeIn(200);
								$(".overlayer").fadeIn(200);
							}

							// zq：读取合同的信息
							function selectContractById() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectContractById({
									cont_id : cont_id
								}).success(function(data) {
									receipt.cont = data.contract;
								});
							}
							// zq：根据合同ID查询工期阶段的内容
							function selectPrstByContId() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectPrstByContId({
									cont_id : cont_id
								}).success(function(data) {
									receipt.prst = data.list;
									
								});
							}
							// zq：根据合同ID查询收款节点的内容
							function selectRenoByContId() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectRenoByContId({
									cont_id : cont_id
								}).success(function(data) {
									receipt.reno = data.list;
								});
							}

							// zq：根据合同ID查找所有的收据
							function selectReceiptByContId() {
								var contId = sessionStorage.getItem('conId');
								services.selectReceiptByContId({
									page : 1,
									contId : contId
								}).success(function(data) {
									receipt.receipts = data.list;
									receipt.totalRow = data.totalRow;
									pageTurn(data.totalPage, 1);
								});
							}
							// 根据收据ID查看收据的详情
							/*
							 * function selectReceiptByReceId() { var receId =
							 * sessionStorage.getItem('receId');
							 * services.selectReceiptByReceId({ receId : receId
							 * }).success(function(data) { receipt.receipt =
							 * data.receipt }); }
							 */
							// zq：根据合同ID计算该合同目前收据共多少钱
							function countReceiptMoneyByContId() {
								var contId = sessionStorage.getItem('conId');

								services.countReceiptMoneyByContId({
									contId : contId
								}).success(function(data) {

									receipt.totalMoney = data.totalMoney;

								});
							}
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
									console.log(name);
									cookie[name.trim()] = value;
									console.log("进来了,已经赋值" + name);
									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
										switch (sessionStorage.getItem(
												'userRole').trim()) {
										case "1":
											receipt.receAddShow = true;

											break;
										case "2":
											receipt.receAddShow = false;

											break;
										case "3":
											receipt.receAddShow = true;

											break;
										case "4":
											receipt.receAddShow = false;

											break;
										case "5":
											receipt.receAddShow = false;

											break;
										}
									}

								}
							}
							// 删除收据
							receipt.delReceipt = function() {
								var receId = this.rece.rece_id;
								sessionStorage.setItem("receId", receId);
								$("#tipDel").fadeIn(200);
								$(".overlayer").fadeIn(200);
							}
							$("#sureDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
								services.delRece({
									receId : sessionStorage.getItem("receId")
								}).success(function(data) {
									alert("操作成功！");
									findReceipts(recePage);
									countReceiptMoneyByContId();
								});
							});

							$("#cancelDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 修改收据
							receipt.editReceipt = function() {
								var receId = this.rece.rece_id;
								services
										.selectReceiptByReceId({
											receId : receId
										})
										.success(
												function(data) {
													receipt.receipt = data.receipt;
													if (data.receipt.rece_atime) {
														receipt.receipt.rece_atime = changeDateType(data.receipt.rece_atime.time);
													} else {
														receipt.receipt.rece_atime = "";
													}

												});
								$(".overlayer").fadeIn(200);
								$("#tipEditReceipt").fadeIn(200);

							}
							receipt.editReceiptInfo = function() {
								var receFormData = JSON
										.stringify(receipt.receipt);
								services.addReceipt({
									receipt : receFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									$("#tipEditReceipt").fadeOut(100);
									$(".overlayer").fadeOut(200);
									alert("操作成功！");
									receipt.receipt = "";
									findReceipts(recePage);
									countReceiptMoneyByContId();
								});
							}
							$("#cancelEditReceipt").click(function() {
								$("#tipEditReceipt").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 更改任务时间的格式
							function changeDateType(time) {

								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
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
											recePage = p;
											findReceipts(p);
										}
									});
								}
							}
							// zq：根据合同ID查找所有的收据
							function findReceipts(p) {
								var contId = sessionStorage.getItem('conId');
								services.selectReceiptByContId({
									page : p,
									contId : contId
								}).success(function(data) {
									receipt.receipts = data.list;
									receipt.totalRow = data.totalRow;
								});
							}
							// zq初始化页面信息
							function initData() {
								$(".tiptop a").click(function() {
									/* sessionStorage.setItem("conId", ""); */
									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});
								$("#invoice").hide();
								$("#receipt").show();
								$("#contract").hide();
								$("#receiveMoney").hide();
								console.log("初始化页面信息");
								if ($location.path().indexOf('/receiptList') == 0) {
									selectReceiptByContId();// 根据合同ID查找所有收据
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
								var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
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
							findRoleFromCookie();
							dateformat();// 格式化日期格式
							// 验证金额输入格式
							var $numberFormat = $(".numberFormat");
							var numberRegexp = /^\d+(\.{0,1}\d+){0,1}$/;
							$(".numberFormat").blur(
									function() {
										if (!numberRegexp.test(this.value)) {
											$(this).parent().children("span")
													.css('display', 'inline');
										}
									});
							$(".numberFormat").click(
									function() {
										$(this).parent().children("span").css(
												'display', 'none');
									});
						} ]);

// 小数过滤器
receiptapp.filter('receFloat', function() {
	return function(input) {
		if (!input) {
			var money = parseFloat('0').toFixed(2);
		} else {
			var money = parseFloat(input).toFixed(2);
		}

		return money;
	}
});
// 合同状态过滤器
receiptapp.filter('conState', function() {
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
receiptapp.filter('conInitiation', function() {
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
receiptapp.filter('conHasproxy', function() {
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
receiptapp.filter('conAvetaxpayer', function() {
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
receiptapp.filter('conType', function() {
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
receiptapp.filter('prstType', function() {
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
receiptapp.filter('renoType', function() {
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
receiptapp.filter('dateType', function() {
	return function(input) {

		var type = "";
		if (input != null) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		}

		return type;
	}
});
// 等级的判断
receiptapp.filter('conRank', function() {
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
receiptapp.directive("dateFormat", function() {
	return {
		restrict : 'A',
		require : 'ngModel',
		scope : true,
		link : function(scope, elem, attrs, controller) {
			var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
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
receiptapp.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input != "") {
			var shortInput = input.substr(0, 8);
			content = shortInput + "……";
		}

		return content;
	}
});

receiptapp.directive('hasPermission', function($timeout) {
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