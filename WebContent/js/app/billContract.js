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
// 获取权限列表
var permissionList;
angular.element(document).ready(function() {
	console.log("获取权限列表！");
	$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data; // 
		console.log("身份是：" + permissionList);
		angular.bootstrap($("#contract"), [ 'contract' ]); // 手动加载angular模块
	});
});

app.directive('hasPermission', function($timeout) {
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

app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);
app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/contractList', {
		templateUrl : '/CIMS/jsp/billInformation/contractList.html',
		controller : 'ContractController'
	}).when('/contractInfo', {
		templateUrl : '/CIMS/jsp/billInformation/contractInfo.html',
		controller : 'ContractController'
	// }).when('/invoiceList', {
	// templateUrl : '/CIMS/jsp/billInformation/invoiceList.html',
	// controller : 'ContractController'
	// }).when('/receiptList', {
	// templateUrl : '/CIMS/jsp/billInformation/receiptList.html',
	// controller : 'ContractController'
	})
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
	// zq根据合同ID获取收款节点
	services.selectRenoByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/ selectRenoByContId.do',
			data : data
		});
	};
	services.selectConByName = function(data) {
		console.log("按名字查找合同");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectConByName.do',
			data : data
		});
	};
	// 分页获取合同数据
	services.selectConByPage = function(data) {
		console.log("按页码查找合同");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectConByPage.do',
			data : data
		});
	};
	// zq添加发票任务
	services.addInvoiceTask = function(data) {

		return $http({
			method : 'post',
			url : baseUrl + 'invoice/addInvoiceTask.do',
			data : data,
		});
	};
	// zq选择所有用户
	services.selectAllUsers = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getAllUserList.do',
			data : data
		});
	};
	// 10.27zq添加到款任务
	services.addReMoneyTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveMoney/addReMoneyTask.do',
			data : data
		});
	};
	// lwt:开收据
	services.addReceipt = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receipt/createReceipt.do',
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
	// zq根据合同ID获取获取已到款钱数
	services.countReceiveMoneyByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveMoney/receiveMoneyByContId.do',
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
	return services;
} ]);

app
		.controller(
				'ContractController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							// 合同
							var contract = $scope;
							contract.flag = 0;// 标志位，用于控制按钮是否显示
							// 获取合同列表
							contract.getContractList = function(page) {
								services.getContractList({
									page : page
								}).success(function(data) {
									contract.contracts = data.list;
									contract.totalPage = data.totalPage;
								});
							};

							// 通过合同名获取合同信息
							contract.selectConByName = function() {
								services.selectConByName({
									contName : $("#cName").val(),
									page : 1
								}).success(function(data) {
									console.log("选择合同成功！");
									contract.contracts = data.list;
									contract.totalPage = data.totalPage;
								});
							};

							// zq查看合同ID，并记入sessione
							contract.getConId = function(conId) {
								sessionStorage.setItem('conId', conId);
							};
							// zq：读取合同的信息
							function selectContractById() {
								var cont_id = sessionStorage.getItem('conId');

								services
										.selectContractById({
											cont_id : cont_id
										})
										.success(
												function(data) {
													contract.cont = data.contract;
													if (data.contract.cont_stime) {
														contract.cont.cont_stime = changeDateType(data.contract.cont_stime.time);
													}
												});
							}
							// zq：根据合同ID查询工期阶段的内容
							function selectPrstByContId() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectPrstByContId({
									cont_id : cont_id
								}).success(function(data) {
									contract.prst = data.list;
								});
							}
							// zq：根据合同ID查询收款节点的内容
							function selectRenoByContId() {
								var cont_id = sessionStorage.getItem('conId');
								services.selectRenoByContId({
									cont_id : cont_id
								}).success(function(data) {
									contract.reno = data.list;
								});
							}

							// 合同，收款节点，工期阶段的详情
							contract.showContInfo = function() {
								$('#contInformation').show();
								$('#contShow').hide();
								$('#contHide').show();
							}
							contract.hideContInfo = function() {

								$('#contInformation').hide();
								$('#contShow').show();
								$('#contHide').hide();
							}
							contract.showPrstInfo = function() {
								$('#prstInformation').show();
								$('#prstShow').hide();
								$('#prstHide').show();
							}
							contract.hidePrstInfo = function() {

								$('#prstInformation').hide();
								$('#prstShow').show();
								$('#prstHide').hide();
							}
							contract.showRenoInfo = function() {
								$('#renoInformation').show();
								$('#renoShow').hide();
								$('#renoHide').show();
							}
							contract.hideRenoInfo = function() {

								$('#renoInformation').hide();
								$('#renoShow').show();
								$('#renoHide').hide();
							}
							// zq添加发票任务
							contract.addInvoiceTask = function(contId) {
								var FIRM = this.con.cont_client;
								var regStr = "\\s" + 'tInvoAudit' + "\\s";
								var reg = new RegExp(regStr);
								if (permissionList.search(reg) < 0) {
									$("#selectAudit").show();
									$("#selectReceiver").hide();
								} else {
									$("#selectAudit").hide();
									$("#selectReceiver").show();
								}
								selectAllUsers();
								/* var contId = this.con.cont_id; */
								sessionStorage.setItem("conId", contId);
								countInvoiceMoneyByContId();
								selectContractById();
								$("#tipAdd").fadeIn(200);
								$(".overlayer").fadeIn(200);
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								contract.invoice = {
									invo_stime : timeNow,
									invo_etime : timeNow,
									invo_firm : FIRM
								};

							};
							contract.addNewInvoiceTask = function() {
								var taskFormData = JSON
										.stringify(contract.invoice);
								services.addInvoiceTask({
									invoice : taskFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									$("#tipAdd").fadeOut(100);
									$(".overlayer").fadeOut(200);
									contract.invoice = "";
									alert("添加成功！");
								});
							}

							$("#cancelAdd").click(function() {
								$("#tipAdd").fadeOut(100);
								$(".overlayer").fadeOut(200);
								contract.invoice = "";

							});

							// zq获取所有用户
							function selectAllUsers() {
								services.selectAllUsers({}).success(
										function(data) {
											console.log("获取用户列表成功！");
											contract.users = data;

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
									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
										role = value;
										switch (sessionStorage.getItem(
												'userRole').trim()) {
										case "1":
											invoice.invoAddShow = true;
											invoice.invoAuditShow = true;
											invoice.invoFinishShow = true;
											break;
										case "2":
											invoice.invoAddShow = false;
											invoice.invoAuditShow = false;
											invoice.invoFinishShow = false;

											break;
										case "3":
											invoice.invoAddShow = false;
											invoice.invoAuditShow = false;
											invoice.invoFinishShow = true;

											break;
										case "4":
											invoice.invoAddShow = true;
											invoice.invoAuditShow = false;
											invoice.invoFinishShow = false;
											break;
										case "5":
											invoice.invoAddShow = false;
											invoice.invoAuditShow = true;
											invoice.invoFinishShow = false;
											break;
										}
									}

								}
							}
							// 10.27zq添加到款任务
							contract.addReMoneyTask = function() {
								selectAllUsers();
								/* var contId = this.con.cont_id; */
								sessionStorage.setItem("conId",
										this.con.cont_id);
								selectContractById();
								countReceiveMoneyByContId();
								$("#tipRemoAdd").fadeIn(200);
								$(".overlayer").fadeIn(200);
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								contract.receiveMoney = {
									remo_time : timeNow
								};

							};
							contract.addRemoTask = function() {
								var taskFormData = JSON
										.stringify(contract.receiveMoney);
								console.log(taskFormData);
								services.addReMoneyTask({
									receiveMoney : taskFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									$("#tipRemoAdd").fadeOut(100);
									$(".overlayer").fadeOut(200);
									contract.receiveMoney = "";
									alert("添加成功！");

								});
							}

							$("#cancelRemoAdd").click(function() {
								$("#tipRemoAdd").fadeOut(100);
								$(".overlayer").fadeOut(200);
								contract.receiveMoney = "";

							});
							// lwt:开收据
							contract.addReceipt = function(contId) {
								var FIRM = this.con.cont_client;
								sessionStorage.setItem("conId", contId);
								countReceiptMoneyByContId();
								selectContractById();
								$(".overlayer").fadeIn(200);
								$("#tipAddReceipt").fadeIn(200);
								// 输入时间的input默认值设置为当前时间
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								contract.receipt = {
									rece_atime : timeNow,
									rece_firm : FIRM
								};

							};
							contract.addRece = function() {
								var receFormData = JSON
										.stringify(contract.receipt);
								services.addReceipt({
									receipt : receFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {

									$("#tipAddReceipt").fadeOut(100);
									$(".overlayer").fadeOut(200);
									alert("收据添加成功！");
									contract.receipt = "";
								});
							}

							$("#cancelAddReceipt").click(function() {
								$("#tipAddReceipt").fadeOut(100);
								$(".overlayer").fadeOut(200);
								contract.receipt = "";
							});
							// 更改任务时间的格式
							function changeDateType(time) {

								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
							}
							// zq：根据合同ID计算该合同目前开了多少钱的发票
							function countInvoiceMoneyByContId() {
								var contId = sessionStorage.getItem('conId');
								services.countInvoiceMoneyByContId({
									contId : contId
								}).success(function(data) {
									contract.invoTotalMoney = data.totalMoney;
									contract.invoTotalRow = data.totalRow;
								});
							}
							// zq：根据合同ID计算该合同目前共到款多少钱
							function countReceiveMoneyByContId() {
								var contId = sessionStorage.getItem('conId');
								services.countReceiveMoneyByContId({
									contId : contId
								}).success(function(data) {
									contract.remoTotalMoney = data.totalMoney;
								});
							}
							// zq：根据合同ID计算该合同目前收据共多少钱
							function countReceiptMoneyByContId() {
								var contId = sessionStorage.getItem('conId');

								services.countReceiptMoneyByContId({
									contId : contId
								}).success(function(data) {

									contract.receTotalMoney = data.totalMoney;

								});
							}
							// 初始化页面信息
							function initData() {
								console.log("初始化页面信息");
								$("#invoice").hide();
								$("#receipt").hide();
								$("#contract").show();
								$("#receiveMoney").hide();
								$(".tiptop a").click(function() {
									/* sessionStorage.setItem("conId", ""); */
									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});
								if ($location.path().indexOf('/contractList') == 0) {// 如果是合同列表页
									contract.flag = 1;// 标志位，用于控制按钮是否显示
									services
											.getContractList({
												page : 1
											})
											.success(
													function(data) {

														// 合同列表分页
														contract.contracts = data.list;
														contract.totalPage = data.totalPage;
														var $pages = $(".tcdPageCode");
														console
																.log(contract.totalPage);
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getContractList(p);// 点击页码时获取第p页的数据
																		}
																	});
														}
													});

								} else if ($location.path().indexOf(
										'/contractInfo') == 0) {
									// zq添加查找合同详情
									selectContractById(); // 根据ID获取合同信息
									selectPrstByContId();// 根据合同ID获取该合同的工期阶段
									selectRenoByContId();// 根据合同ID获取该合同的收款节点
									$("#contInformation").hide();
									$("#renoInformation").hide();
									$("#prstInformation").hide();
								}
							}
							findRoleFromCookie();
							initData();
							// 验证日期输入格式
							var $dateFormat = $(".dateFormat");
							var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
							$(".dateFormat").blur(
									function() {
										if (!dateRegexp.test(this.value)) {
											$(this).parent().children("span")
													.css('display', 'inline');
										}
									});
							$(".dateFormat").click(
									function() {
										$(this).parent().children("span").css(
												'display', 'none');
									});

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
app.filter('receFloat', function() {
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
app.filter('conState', function() {
	return function(input) {
		var state = "";
		if (input == "0")
			state = "在建";
		else if (input == "1")
			state = "竣工";
		else if (input == "2")
			state = "停建";
		else if (!input)
			state = "";
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
		else if (!input)
			initiation = "";

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
		else if (!input)
			hasproxy = "";

		return hasproxy;
	}
});
// 合同一般纳税人判断
app.filter('conAvetaxpayer', function() {
	return function(input) {
		var avetaxpayer = "";
		if (input == "0")
			avetaxpayer = "一般纳税人";
		else if (input == "1")
			avetaxpayer = "小规模纳税人";
		else if (!input)
			avetaxpayer = "";

		return avetaxpayer;
	}
});

// 发票类型的判断的判断
app.filter('conInvoiceType', function() {
	return function(input) {
		var avetaxpayer = "";
		if (input == "0")
			avetaxpayer = "增值税专用发票";
		else if (input == "1")
			avetaxpayer = "增值税普通发票";
		else if (!input)
			avetaxpayer = "";
		return avetaxpayer;
	}
});
// 合同类型的判断
app.filter('conType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "传统光伏项目";
		else if (input == "1")
			type = "分布式";
		else if (input == "2")
			type = "光热";
		else if (input == "3")
			type = "其他";
		else if (!input)
			type = "";
		return type;
	}
});

// 合同项目阶段的判断
app.filter('conProStage', function() {
	return function(input) {
		var type = "";
		if (input) {
			console.log(input);
			strs = input.split(","); // 字符分割
			console.log("项目阶段" + strs);
			for (i = 0; i < strs.length; i++) {
				var j = i + 1;
				switch (strs[i]) {
				case "0":
					type += "  " + j + "、规划  ;  ";
					break;
				case "1":
					type += "  " + j + "、预可研  ;      ";
					break;
				case "2":
					type += "  " + j + "、可研  ;      ";
					break;
				case "3":
					type += "  " + j + "、项目建议书  ;      ";
					break;
				case "4":
					type += "  " + j + "、初步设计  ;      ";
					break;
				case "5":
					type += "  " + j + "、发包、招标设计  ;      ";
					break;
				case "6":
					type += "  " + j + "、施工详图  ;      ";
					break;
				case "7":
					type += "  " + j + "、竣工图  ;       ";
					break;
				case "8":
					type += "  " + j + "、其他   ;   ";
					break;
				default:
					type += " ";
					break;
				}
			}
			return type;
		}
	}
});

// 合同项目阶段的判断
app.filter('conCompanyType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "国有企业";
		else if (input == "1")
			type = "事业单位";
		else if (input == "2")
			type = "民营企业";
		else if (input == "3")
			type = "国外企业";
		else if (input == "4")
			type = "政府机关";
		else if (input == "5")
			type = "其他";
		else if (!input)
			type = "";
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
		else if (!input)
			type = "";
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
		else if (!input)
			type = "";
		return type;
	}
});
// 时间的格式化的判断
app.filter('dateType', function() {
	return function(input) {
		var type = "";
		if (input) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		} else {
			type = "";
		}

		return type;
	}
});
// 截取任务内容
app.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input) {
			var shortInput = input.substr(0, 8);
			content = shortInput + "……";
		} else {
			content = "";
		}
		return content;
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
		else if (!input)
			rank = "";
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