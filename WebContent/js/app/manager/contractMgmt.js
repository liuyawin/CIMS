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
										templateUrl : '/CIMS/jsp/manager/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/debtContract',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/overdueContract',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/doneContract',
									{
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/contractAdd',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/contractAdd.html',
										controller : 'ContractController'
									})
							.when(
									'/projectStageInfo',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/projectStageInfo.html',
										controller : 'ContractController'
									})
							.when(
									'/contractInfo',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/contractInfo.html',
										controller : 'ContractController'
									})
							.when(
									'/invoiceTask',
									{
										templateUrl : '/CIMS/jsp/manager/contractInformation/invoiceTask.html',
										controller : 'ContractController'
									})
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getContractList = function(data) {
		console.log("发送请求获取合同信息");
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectContract.do',
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

	services.addContract = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/addContract.do',
			data : data
		});
	};
	// zq补录合同信息
	services.repeatAddContract = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/repeatAddContract.do',
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
			url : baseUrl + 'receiveNode/selectRenoByContId.do',
			data : data
		});
	};
	// 工期完成
	services.finishPrst = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/finishPrst.do',
			data : data
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
	// zq添加任务
	services.addInvoiceTask = function(data) {

		return $http({
			method : 'post',
			url : baseUrl + 'invoice/addInvoiceTask.do',
			data : data,
		});
	};
	return services;
} ]);

app.controller('ContractController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;
			var findType;
			// 获取合同列表
			contract.getContractList = function(page) {
				services.getContractList({
					page : page
				}).success(function(data) {
					contract.contracts = data.list;
					contract.totalPage = data.totalPage;
				});
			};
			/*
			 * // 获取欠款合同 contract.getDebtContract = function() {
			 * services.getDebtContract({}).success(function(data) {
			 * console.log("获取欠款合同成功！"); contract.contracts = data; }); }; //
			 * 获取逾期合同 contract.getOverdueContract = function() {
			 * services.getOverdueContract({}).success(function(data) {
			 * console.log("获取逾期合同成功！"); contract.contracts = data.list; }); };
			 */
			// 通过合同名获取合同信息
			contract.selectConByName = function() {
				services.selectConByName({
					contName : $("#cName").val(),
					page : 1,
					findType : findType
				}).success(function(data) {
					console.log("选择合同成功！");
					contract.contracts = data.list;
					contract.totalPage = data.totalPage;
					pageTurnByName(contract.totalPage, 1, findType)
				});
			};

			function getContByName(page, findType) {
				services.selectConByName({
					contName : $("#cName").val(),
					page : page,
					findType : findType
				}).success(function(data) {
					console.log("选择合同成功！");
					contract.contracts = data.list;
					contract.totalPage = data.totalPage;

				});
			}
			// zq按查询内容获取任务列表的换页
			function pageTurnByName(totalPage, page, findType) {

				var $pages = $(".tcdPageCode");
				console.log($pages.length);
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							getContByName(p, findType);
						}
					});
				}
			}
			// 添加文书任务
			contract.addTask1 = function() {
				services.addTask({
					task : contract.task1,
					taskType : "task1",
					conId : sessionStorage.getItem(contractId)
				}).success(function(data) {
					alert("添加文书任务成功！");
				});
			};
			// 添加执行管控任务
			contract.addTask2 = function() {
				services.addTask({
					task : contract.task2,
					taskType : "task2",
					conId : sessionStorage.getItem(contractId)
				}).success(function(data) {
					alert("添加执行管控任务成功！");
				});
			};
			// zq查看合同ID，并记入sessione
			contract.getContId = function(contId) {

				sessionStorage.setItem('contId', contId);

			};
			// zq：添加合同
			contract.repeatAddContract = function() {
				console.log(contract.contract);
				var conFormData = JSON.stringify(contract.contract);
				console.log(conFormData);
				services.repeatAddContract({
					contract : conFormData,
					cont_id : sessionStorage.getItem('contId')
				}).success(function(data) {
					/* window.sessionStorage.setItem("contractId",); */
					alert("添加合同成功！");
				});
			};

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
			// 确认完工
			contract.finishPrst = function(prstId) {
				services.finishPrst({
					prstId : prstId
				}).success(function(data) {
					alert("确认完工成功！");
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同查看工期阶段
					selectRenoByContId();// 根据合同ID查看收款节点
				});
			}
			// zq添加任务
			contract.addInvoiceTask = function() {
				selectAllUsers();
				var contId = this.con.cont_id;
				$("#tipAdd").fadeIn(200);
				$(".overlayer").fadeIn(200);
				$("#sureAdd").click(function() {

					var taskFormData = JSON.stringify(contract.invoice);
					services.addInvoiceTask({
						invoice : taskFormData,
						contId : contId
					}).success(function(data) {

						$("#tipAdd").fadeOut(100);
						$(".overlayer").fadeOut(200);
						contract.invoice="";
						alert("添加成功！");

					});
				});

				$("#cancelAdd").click(function() {
					$("#tipAdd").fadeOut(100);
					$(".overlayer").fadeOut(200);
					contract.invoice="";

				});

			};
			// zq：读取合同的信息
			function selectContractById() {
				var cont_id = sessionStorage.getItem('contId');
				services.selectContractById({
					cont_id : cont_id
				}).success(function(data) {
					contract.cont = data;
				});
			}
			// zq：根据合同ID查询工期阶段的内容
			function selectPrstByContId() {
				var cont_id = sessionStorage.getItem('contId');
				services.selectPrstByContId({
					cont_id : cont_id
				}).success(function(data) {
					contract.prst = data.list;
				});
			}
			// zq：根据合同ID查询收款节点的内容
			function selectRenoByContId() {
				var cont_id = sessionStorage.getItem('contId');
				services.selectRenoByContId({
					cont_id : cont_id
				}).success(function(data) {
					contract.reno = data.list;
				});
			}
			// zq：从设计部查找人员
			function selectUsersFromDesign() {
				services.selectUsersFromDesign({}).success(function(data) {
					contract.userDepts = data;
				});
			}
			// zq获取所有用户
			function selectAllUsers() {
				services.selectAllUsers({}).success(function(data) {
					console.log("获取用户列表成功！");
					contract.users = data;

				});
			}
			$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
				// 下面是在table render完成后执行的js
				$('.invoMoney').hide();
			});
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/contractList') == 0) {// 如果是合同列表页
					findType = 1;
					services.getContractList({
						page : 1,
						findType : findType
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
									contract.getContractList(p, findType);// 点击页码时获取第p页的数据
								}
							});
						}
					});

				} else if ($location.path().indexOf('/debtContract') == 0) {
					// contract.getDebtContract();
					findType = 2;
					services.getContractList({
						page : 1,
						findType : findType
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
									contract.getContractList(p, findType);// 点击页码时获取第p页的数据
								}
							});
						}
					});

				} else if ($location.path().indexOf('/overdueContract') == 0) {
					// contract.getOverdueContract();
					findType = 3;
					services.getContractList({
						page : 1,
						findType : findType
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
									contract.getContractList(p, findType);// 点击页码时获取第p页的数据
								}
							});
						}
					});

				} else if ($location.path().indexOf('/doneContract') == 0) {
					// contract.getOverdueContract();
					findType = 4;
					services.getContractList({
						page : 1,
						findType : findType
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
									contract.getContractList(p, findType);// 点击页码时获取第p页的数据
								}
							});
						}
					});

				} else if ($location.path().indexOf('/projectStageInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同查看工期阶段
					selectRenoByContId();// 根据合同ID查看收款节点
				} else if ($location.path().indexOf('/contractInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId();// 根据合同ID获取该合同的工期阶段
					selectRenoByContId();// 根据合同ID获取该合同的收款节点
				} else if ($location.path().indexOf('/invoiceTask') == 0) {
					selectAllUsers();
					selectContractById();

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
			var shortInput = input.substr(0, 8);
			content = shortInput + "……";
		}

		return content;
	}
});

app.directive('onFinishRenderFilters', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished');
				});
			}
		}
	};
});

// 收款节点的状态的判断
app.filter('renoType', function() {
	return function(input) {
		var type = "";
		if (input == "0")
			type = "未收款";
		else if (input == "1")
			type = "已收款";
		else if (input == "2")
			type = "未付全款";
		else if (input == "3")
			type = "提前到款";
		return type;
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