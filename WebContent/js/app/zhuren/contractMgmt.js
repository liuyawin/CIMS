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
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/contractList',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/debtContract',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/overdueContract',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/finishedContract',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/contractAdd',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractAdd.html',
										controller : 'ContractController'
									})
							.when(
									'/contractDetail',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractDetail.html',
										controller : 'ContractController'
									})
							.when(
									'/contractInfo',
									{
										templateUrl : '/CIMS/jsp/zhuren/contractInformation/contractInfo.html',
										controller : 'ContractController'
									})
							.when(
									'/prstInfo',
									{
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractInfo.html',
										controller : 'ContractController'
									})
							.when(
									'/renoInfo',
									{
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractInfo.html',
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
	
	services.getFinishedContract = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectContract.do',
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

	// 获取所有用户
	services.getAllUsers = function() {
		return $http({
			method : 'post',
			url : baseUrl + 'user/getAllUserList.do',
		});
	};

	services.addContract = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/addContract.do',
			data : data
		});
	};

	services.deleteContract = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/deleteContract.do',
			data : data
		});
	};

	services.addTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'task/addTask.do',
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

	return services;
} ]);

app.controller('ContractController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;
			contract.flag = 0;//标志位，用于控制按钮是否显示
			// 获取合同列表
			contract.getContractList = function(page) {
				services.getContractList({
					page : page
				}).success(function(data) {
					contract.contracts = data.list;
					contract.totalPage = data.totalPage;
				});
			};
			// 获取欠款合同
			contract.getDebtContract = function() {
				services.getDebtContract({}).success(function(data) {
					console.log("获取欠款合同成功！");
					contract.contracts = data;
				});
			};
			// 获取逾期合同
			contract.getOverdueContract = function() {
				services.getOverdueContract({}).success(function(data) {
					console.log("获取逾期合同成功！");
					contract.contracts = data.list;
				});
			};
			// 获取终结合同
			contract.getFinishedContract = function() {
				services.getFinishedContract({
					findType:"4",
					contName:""
				}).success(function(data) {
					console.log("获取逾期合同成功！");
					contract.contracts = data.list;
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
			// 添加合同
			contract.addContract = function() {
				var conFormData = JSON.stringify(contract.contract);

				services.addContract({
					contract : conFormData
				}).success(function(data) {
					sessionStorage.setItem("contractId", data);
					alert("创建合同成功！");
				});
			};
			// zq查看合同ID，并记入sessione
			contract.getConId = function(conId) {
				sessionStorage.setItem('conId', conId);
			};
			// 删除合同
			contract.deleteContract = function(obj) {
				var conId = this.con.cont_id;
				var msg = "确认删除该合同？";
				if (confirm(msg) == true) {
					services.deleteContract({
						conId : conId
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						alert("删除成功！");
					});
				} else {
					return false;
				}
			}
			// 添加文书任务
			contract.addTask1 = function() {
				var conId = sessionStorage.getItem("contractId");
				console.log(conId);
				if (conId.trim() == "") {
					alert("请先录入合同信息！");
					return false;
				}
				var task1 = JSON.stringify(contract.task1);
				console.log(task1);
				services.addTask({
					task : task1,
					taskType : "1",// 1代表文书任务
					conId : conId
				}).success(function(data) {
					alert("添加文书任务成功！");
				});
			};
			// 添加执行管控任务
			contract.addTask2 = function() {
				var conId = sessionStorage.getItem("contractId");
				console.log(conId);
				if (conId.trim() == "") {
					alert("请先录入合同信息！");
					return false;
				}
				var task2 = JSON.stringify(contract.task2);
				console.log(task2);
				services.addTask({
					task : task2,
					taskType : "2",// 2代表执行管控任务
					conId : conId
				}).success(function(data) {
					alert("添加执行管控任务成功！");
				});
			};

			// zq：读取合同的信息
			function selectContractById() {
				var cont_id = sessionStorage.getItem('conId');

				services.selectContractById({
					cont_id : cont_id
				}).success(function(data) {
					contract.cont = data;

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

			// 初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/contractList') == 0) {// 如果是合同列表页
					contract.flag = 1;//标志位，用于控制按钮是否显示
					services.getContractList({
						page : 1
					}).success(function(data) {
						// 合同列表分页
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getContractList(p);// 点击页码时获取第p页的数据
								}
							});
						}
						// 点击创建任务时弹出模态框
						contract.newTask = function(obj) {
							var conId = this.con.cont_id;
							services.getAllUsers().success(function(data) {
								contract.users = data;
								console.log(conId);
								sessionStorage.setItem("contractId", conId);
							});
							$(".overlayer").fadeIn(200);
							$(".tip").fadeIn(200);
							return false;
						};

						$(".tiptop a").click(function() {
							sessionStorage.setItem("contractId", "");
							$(".overlayer").fadeOut(200);
							$(".tip").fadeOut(200);
						});

						$(".sure").click(function() {
							var conId = sessionStorage.getItem("contractId");
							if (contract.task.task_type == "1") {
								var task1 = JSON.stringify(contract.task1);
								console.log(task1);
								services.addTask({
									task : task1,
									taskType : "1",// 1代表文书任务
									conId : conId
								}).success(function(data) {
									alert("添加文书任务成功！");
								});
							} else if (contract.task.task_type == "0") {
								var task2 = JSON.stringify(contract.task2);
								console.log(task2);
								services.addTask({
									task : task2,
									taskType : "2",// 2代表执行管控任务
									conId : conId
								}).success(function(data) {
									alert("添加执行管控任务成功！");
								});
							}
							$(".overlayer").fadeOut(100);
							$(".tip").fadeOut(100);
						});

						$(".cancel").click(function() {
							sessionStorage.setItem("contractId", "");
							$(".overlayer").fadeOut(100);
							$(".tip").fadeOut(100);
						});

						$(".taskType").change(function() {
							if (contract.task.task_type == "1") {
								$("#addTask1-form").slideDown(200);
								$("#addTask2-form").hide();
							} else if (contract.task.task_type == "0") {
								$("#addTask1-form").hide();
								$("#addTask2-form").slideDown(200);
							}
						});
					});

				} else if ($location.path().indexOf('/debtContract') == 0) {
					contract.flag = 1;//标志位，用于控制按钮是否显示
					// contract.getDebtContract();
					services.getDebtContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getDebtContract(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});
				} else if ($location.path().indexOf('/overdueContract') == 0) {
					contract.flag = 1;//标志位，用于控制按钮是否显示
					// contract.getOverdueContract();
					services.getOverdueContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getOverdueContract(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});
				}else if ($location.path().indexOf('/finishedContract') == 0) {//获取终结合同信息
					contract.flag = 0;//标志位，用于控制按钮是否显示
					services.getFinishedContract({
						page : 1,
						findType:"4",
						contName:""
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						console.log(contract.totalPage);
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getFinishedContract(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});
				} else if ($location.path().indexOf('/contractAdd') == 0) {
					// 这里先获取人员列表
					services.getAllUsers().success(function(data) {
						contract.users = data;
						sessionStorage.setItem("contractId", "");
					});
					/*
					 * var $select = $("select"); for (var i = 0; i <
					 * $select.length; i++) { $select[i].options[0].selected =
					 * true; } $('select').prop('selectedIndex', 1);
					 */
				} else if ($location.path().indexOf('/prstInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同ID获取该合同的工期阶段
					selectRenoByContId();// 根据合同ID获取该合同的收款节点
					$("#renoInformation").hide();
					$("#contInformation").hide();
				} else if ($location.path().indexOf('/renoInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同ID获取该合同的工期阶段
					selectRenoByContId();// 根据合同ID获取该合同的收款节点
					$("#contInformation").hide();
					$("#prstInformation").hide();
				} else if ($location.path().indexOf('/contractInfo') == 0) {
					// zq添加查找合同详情
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同ID获取该合同的工期阶段
					selectRenoByContId();// 根据合同ID获取该合同的收款节点
					$("#renoInformation").hide();
					$("#prstInformation").hide();
				}
			}

			initData();
			// 验证日期输入格式
			var $dateFormat = $(".dateFormat");
			var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
			$(".dateFormat").blur(function() {
				if (!dateRegexp.test(this.value)) {
					$(this).parent().children("span").css('display', 'inline');
				}
			});
			$(".dateFormat").click(function() {
				$(this).parent().children("span").css('display', 'none');
			});
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
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */