var receiveMoneyApp = angular
		.module(
				'receiveMoney',
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
receiveMoneyApp.run([
		'$rootScope',
		'$location',
		function($rootScope, $location) {
			$rootScope.$on('$routeChangeSuccess',
					function(evt, next, previous) {
						console.log('路由跳转成功');
						/* $rootScope.$broadcast('reGetData'); */
					});
		} ]);

// 路由配置
receiveMoneyApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/receiveMoneyList', {
		templateUrl : '/CIMS/jsp/billInformation/receiveMoneyList.html',
		controller : 'ReceiveMoneyController'
	}).when('/receiveMoneyTaskList', {
		templateUrl : '/CIMS/jsp/billInformation/receiveMoneyList.html',
		controller : 'ReceiveMoneyController'
	})
} ]);
receiveMoneyApp.constant('baseUrl', '/CIMS/');
receiveMoneyApp.factory('receivemoneyservices', [
		'$http',
		'baseUrl',
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
			// zq根据合同ID获取获取已到款钱数
			services.countReceiveMoneyByContId = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receiveMoney/receiveMoneyByContId.do',
					data : data
				});
			};
			// 根据ID获取到款某条记录
			services.selectReceiveMoneyById = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receiveMoney/selectReceiveMoneyById.do',
					data : data
				})
			};
			// 根据合同ID获取该合同的所有到款记录
			services.selectReceiveMoneysByContId = function(data) {
				return $http({
					method : 'post',
					url : baseUrl
							+ 'receiveMoney/selectReceiveMoneysByContId.do',
					data : data
				})
			};
			// 获取到款任务
			services.selectRemoTasksByState = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receiveMoney/selectRemoTasksByState.do',
					data : data
				})
			};
			services.auditReceiveMoney = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receiveMoney/auditReceiveMoney.do',
					data : data
				})
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
			// 删除到款
			services.delRemo = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + "receiveMoney/deleteReceMoney.do",
					data : data
				});
			};
			// 10.27zq添加到款任务{修改}
			services.addReMoneyTask = function(data) {
				return $http({
					method : 'post',
					url : baseUrl + 'receiveMoney/addReMoneyTask.do',
					data : data
				});
			};
			return services;
		} ]);

receiveMoneyApp
		.controller(
				'ReceiveMoneyController',
				[
						'$scope',
						'receivemoneyservices',
						'$location',
						function($scope, services, $location) {
							// 合同
							var reMoney = $scope;
							var role;
							var remoState = null;
							var remoListType = null;// 根据类型的不同区分是查找任务列表还是普通的到款列表
							var remoPage = 1;
							// 查看到款记录
							reMoney.checkRemo = function() {
								var remoId = this.remo.remo_id;
								selectAllUsers();
								selectReceiveMoneyById(remoId);
							
								$(".overlayer").fadeIn(200);
								$("#tipCheckRemo").fadeIn(200);

							}

							// 审核到款记录
							reMoney.auditRemo = function() {

								var remoId = this.remo.remo_id;
								
								if (this.remo.remo_state == "0") {
									sessionStorage.setItem("remoId", remoId);
									selectAllUsers();
									selectReceiveMoneyById(remoId);
									$(".overlayer").fadeIn(200);
									$("#tipRemoAudit").fadeIn(200);
								} else {
									alert("已核对，不能重复操作！");
								}
							};
							$("#cancelRemoAudit").click(function() {
								$("#tipRemoAudit").fadeOut(100);
								$(".overlayer").fadeOut(200);
								reMoney.receiveMoney = "";
							});
							$("#sureRemoAudit")
									.click(
											function() {
												services
														.auditReceiveMoney(
																{
																	remoId : sessionStorage
																			.getItem("remoId"),
																	remoAmoney : reMoney.receiveMoney.remo_amoney
																})
														.success(
																function(data) {
																	alert("操作成功！");
																	$(
																			"#tipRemoAudit")
																			.fadeOut(
																					100);
																	$(
																			".overlayer")
																			.fadeOut(
																					200);
																	selectContractById();
																	countReceiveMoneyByContId();
																	findReceiveMoneys(remoPage);

																});

											});
							// 根据到款ID查找到款单条记录
							function selectReceiveMoneyById(remoId) {
								services
										.selectReceiveMoneyById({
											remoId : remoId
										})
										.success(
												function(data) {
													reMoney.receiveMoney = data.receiveMoney;
													if (data.receiveMoney.remo_time != null) {
														reMoney.receiveMoney.remo_time = changeDateType(data.receiveMoney.remo_time.time);
													} else {
														reMoney.receiveMoney.remo_time = "";
													}

												});
							}
							// zq：读取合同的信息
							function selectContractById() {
								var contId = sessionStorage.getItem('conId');
								services.selectContractById({
									cont_id : contId
								}).success(function(data) {
									reMoney.cont = data.contract;
								});
							}

							// zq：根据合同ID计算该合同目前共到款多少钱
							function countReceiveMoneyByContId() {
								var contId = sessionStorage.getItem('conId');
								services.countReceiveMoneyByContId({
									contId : contId
								}).success(function(data) {
									reMoney.totalMoney = data.totalMoney;
								});
							}
							// 更改任务时间的格式
							function changeDateType(time) {

								newDate = new Date(time).toLocaleDateString()
										.replace(/\//g, '-');
								return newDate;
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
											remoPage = p;
											findReceiveMoneys(p);
										}
									});
								}
							}
							// 用于翻页时调用查找函数
							function findReceiveMoneys(p) {

								var remoListType = sessionStorage
										.getItem("remoListType");
								if (remoListType == "REMO") {
									services.selectReceiveMoneysByContId(
											{
												contId : sessionStorage
														.getItem("conId"),
												page : p,
												remoState : remoState
											}).success(function(data) {

										reMoney.remos = data.list;
										countReceiveMoneyByContId();
									});
								} else if (remoListType == "REMOTASK") {
									services.selectRemoTasksByState({
										page : p,
										remoState : remoState
									}).success(function(data) {
										reMoney.remos = data.list;
									});
								}

							}
							// 用于前台的查找
							reMoney.selectReceiveMoneysByContId = function() {
								remoState = $("#remoState").val();
								var remoListType = sessionStorage
										.getItem("remoListType");
								if (remoListType == "REMO") {
									services.selectReceiveMoneysByContId(
											{
												contId : sessionStorage
														.getItem("conId"),
												page : 1,
												remoState : remoState
											}).success(function(data) {
										reMoney.remos = data.list;
										pageTurn(data.totalPage, 1);
									});
								} else if (remoListType == "REMOTASK") {
									services.selectRemoTasksByState({
										page : 1,
										remoState : remoState
									}).success(function(data) {
										reMoney.remos = data.list;
										pageTurn(data.totalPage, 1);
									});
								}

							};
							// zq获取所有用户
							function selectAllUsers() {
								services.selectAllUsers({}).success(
										function(data) {
											console.log("获取用户列表成功！");
											reMoney.users = data;

										});
							}
							// zq查看合同ID，并记入sessionStorage
							contract.getConId = function(conId) {
								sessionStorage.setItem('conId', conId);
							};

							// 删除到款
							reMoney.delRemo = function() {
								var remoId = this.remo.remo_id;
								if (this.remo.remo_state == "0") {
									sessionStorage.setItem("remoId", remoId);
									$("#tipDel").fadeIn(200);
									$(".overlayer").fadeIn(200);
								} else {
									alert("已核对，不能删除！");
								}
							}
							$("#sureDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
								services.delRemo({
									remoId : sessionStorage.getItem("remoId")
								}).success(function(data) {
									alert("操作成功！");
									findReceiveMoneys(remoPage);
									countReceiveMoneyByContId();
								});
							});

							$("#cancelDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 修改到款
							reMoney.editRemo = function() {
								var remoId = this.remo.remo_id;
								if (this.remo.remo_state == "0") {
									selectAllUsers();
									selectReceiveMoneyById(remoId);
									$(".overlayer").fadeIn(200);
									$("#tipRemo").fadeIn(200);
									$("#sureRemoEdit").show();
									$("#cancelRemoEdit").show();
									$("#remoAmoney").hide();
								} else {
									alert("已核对，不能修改！");
								}

							}
							reMoney.editReceiveMoneyTask = function() {
								var taskFormData = JSON
										.stringify(reMoney.receiveMoney);
								services.addReMoneyTask({
									receiveMoney : taskFormData,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									$("#tipRemo").fadeOut(100);
									$(".overlayer").fadeOut(200);
									contract.receiveMoney = "";
									alert("操作成功！");
									findReceiveMoneys(remoPage);
									countReceiveMoneyByContId();
								});
							}
							$("#cancelRemoEdit").click(function() {
								$("#tipRemo").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							/*
							 * // 删除修改之后的页面刷新函数 function refreshRemoList() { var
							 * remoListType = sessionStorage
							 * .getItem("remoListType"); if (remoListType ==
							 * "REMO") { services.selectReceiveMoneysByContId( {
							 * contId : sessionStorage .getItem("conId"), page :
							 * 1, remoState : remoState
							 * }).success(function(data) { reMoney.remos =
							 * data.list; pageTurn(data.totalPage, 1); }); }
							 * else if (remoListType == "REMOTASK") {
							 * services.selectRemoTasksByState({ page : 1,
							 * remoState : remoState }).success(function(data) {
							 * reMoney.remos = data.list;
							 * pageTurn(data.totalPage, 1); }); } }
							 */
							// zq初始化页面信息
							function initData() {
								$(".tiptop a").click(function() {

									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});
								$("#receiveMoney").show();
								$("#invoice").hide();
								$("#receipt").hide();
								$("#contract").hide();
								console.log("初始化页面信息");
								if ($location.path().indexOf(
										'/receiveMoneyList') == 0) {// 如果是合同列表页
									remoListType = "REMO";
									sessionStorage.setItem("remoListType",
											"REMO");
									remoState = "-1";
									reMoney.remoState = "-1";
									selectContractById();
									countReceiveMoneyByContId();
									services.selectReceiveMoneysByContId(
											{
												contId : sessionStorage
														.getItem("conId"),
												page : 1,
												remoState : remoState
											}).success(function(data) {
										reMoney.remos = data.list;
										pageTurn(data.totalPage, 1);
									});
								} else if ($location.path().indexOf(
										'/receiveMoneyTaskList') == 0) {
									remoListType = "REMOTASK";
									sessionStorage.setItem("remoListType",
											"REMOTASK");
									remoState = "-1";
									reMoney.remoState = "-1";
									services.selectRemoTasksByState({
										page : 1,
										remoState : remoState
									}).success(function(data) {
										reMoney.remos = data.list;
										pageTurn(data.totalPage, 1);
									});
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
							findRoleFromCookie();
							initData();// 初始化

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
receiveMoneyApp.filter('invoFloat', function() {
	return function(input) {
		if (!input) {
			var money = parseFloat('0').toFixed(2);
		} else {
			var money = parseFloat(input).toFixed(2);
		}
		return money;
	}
});

// 时间的格式化的判断
receiveMoneyApp.filter('dateType', function() {
	return function(input) {
		var type = "";
		if (input != null) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		}

		return type;
	}
});

// 小数过滤器
receiveMoneyApp.filter('receFloat', function() {
	return function(input) {
		var money = parseFloat(input).toFixed(2);
		return money;
	}
});
// 自定义表单验证日期格式
receiveMoneyApp.directive("dateFormat", function() {
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
receiveMoneyApp.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input != "") {
			var shortInput = input.substr(0, 8);
			content = shortInput + "……";
		}

		return content;
	}
});
// 判断发票状态
receiveMoneyApp.filter('remoState', function() {
	return function(input) {
		var state = "";
		if (input == "0") {
			state = "待核对";
		}
		if (input == "1") {
			state = "已核对";
		}

		return state;
	}
});
// 是否显示总金额
receiveMoneyApp.directive('isShow', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			var type = sessionStorage.getItem("remoListType");
			if (type == "REMO") {
				element.css("display", "inline");
			} else if (type == "REMOTASK") {
				element.css("display", "none");
			}
		}
	};

});

receiveMoneyApp.directive('hasPermission', function($timeout) {
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