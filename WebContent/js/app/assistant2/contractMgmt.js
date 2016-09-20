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
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractList.html',
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
									'/contractAdd',
									{
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractAdd.html',
										controller : 'ContractController'
									})
							.when(
									'/contractDetail',
									{
										templateUrl : '/CIMS/jsp/assistant2/contractInformation/contractDetail.html',
										controller : 'ContractController'
									})
							.when(
									'/contractInfo',
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

	// zq添加工期阶段
	services.addProjectStage = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/addProjectStage.do',
			data : data
		});
	};

	// zq添加收款节点
	services.addReceiveNode = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/addReceiveNode.do',
			data : data
		});
	};
	// zq从设计部取出项目经理人选
	services.selectUsersFromDesign = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUsersFromDesign.do',
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

	return services;
} ]);

app.controller('ContractController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			// 合同
			var contract = $scope;
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

			// zq：添加工期阶段的单项控件
			function addStage() {// 动态添加工期阶段
				$scope.fchat = new Object();
				$scope.fchat.stages = [ {
					key : 0,
					value : ""
				} ];
				// 初始化时由于只有1条回复，所以不允许删除
				$scope.fchat.canDescStage = false;
				// 增加回复数
				$scope.fchat.incrStage = function($index) {
					$scope.fchat.stages.splice($index + 1, 0, {
						key : new Date().getTime(),
						value : ""
					});
					// 用时间戳作为每个item的key
					// 增加新的回复后允许删除
					$scope.fchat.canDescStage = true;
					dateformat();
				} // 减少回复数
				$scope.fchat.decrStage = function($index) {

					// 如果回复数大于1，删除被点击回复
					if ($scope.fchat.stages.length > 1) {
						$scope.fchat.stages.splice($index, 1);
					}
					// 如果回复数为1，不允许删除
					if ($scope.fchat.stages.length == 1) {
						$scope.fchat.canDescStage = false;
					}
				}
				// 动态添加工期
				// 将字符串连接起来
				/*
				 * $scope.fchat.combineReplies = function() { var cr = ""; for
				 * (var i = 0; i < $scope.fchat.stages.length; i++) { cr += "#" +
				 * $scope.fchat.stages[i].value; }
				 * 
				 * return cr; }
				 */
			}
			// zq：添加工期阶段到数据库
			contract.addProjectStage = function() {
				var cont_id = sessionStorage.getItem("contId");
				var prstFormData = JSON.stringify($scope.fchat);
				services.addProjectStage({
					projectStage : prstFormData,
					cont_id : cont_id
				}).success(function(data) {
					alert("添加工期成功！");
					addNode();
					 selectPrstByContId();
					$("#addReceiveNode").show();
				});
			}

			// zq添加收款节点控件
			function addNode() {// 动态添加工期阶段
				$scope.rnchat = new Object();
				$scope.rnchat.nodes = [ {
					key : 0,
					value : ""
				} ];
				// 初始化时由于只有1条回复，所以不允许删除
				$scope.rnchat.canDescNode = false;
				// 增加回复数
				$scope.rnchat.incrNode = function($index) {

					$scope.rnchat.nodes.splice($index + 1, 0, {
						key : new Date().getTime(),
						value : ""
					});
					// 用时间戳作为每个item的key
					// 增加新的回复后允许删除
					$scope.rnchat.canDescNode = true;
					dateformat();
				} // 减少回复数
				$scope.rnchat.decrNode = function($index) {
					dateformat();
					// 如果回复数大于1，删除被点击回复
					if ($scope.rnchat.nodes.length > 1) {
						$scope.rnchat.nodes.splice($index, 1);
					}
					// 如果回复数为1，不允许删除
					if ($scope.rnchat.nodes.length == 1) {
						$scope.rnchat.canDescNode = false;
					}
				}
			}
			// zq：添加收款节点到数据库
			contract.addReceiveNode = function() {
				var cont_id = sessionStorage.getItem("contId");
				var renoFormData = JSON.stringify($scope.rnchat);
				console.log(renoFormData);
				services.addReceiveNode({
					receiveNode : renoFormData,
					cont_id : cont_id
				}).success(function(data) {
					alert("添加节点成功！");
				});
			}

			// ps显示图片
			contract.psShowDel = function(num) {
				$("#ps" + num + "").find("img").css('visibility', 'visible');
			}
			// ps显示图片
			contract.psHideDel = function(num) {
				$("#ps" + num + "").find("img").css('visibility', 'hidden');
			}
			// rn显示图片
			contract.rnShowDel = function(num) {
				$("#rn" + num + "").find("img").css('visibility', 'visible');
			}
			// rn显示图片
			contract.rnHideDel = function(num) {
				$("#rn" + num + "").find("img").css('visibility', 'hidden');
			}

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
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/contractList') == 0) {// 如果是合同列表页
					services.getContractList({
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
									contract.getContractList(p);// 点击页码时获取第p页的数据
								}
							});
						}
					});

				} else if ($location.path().indexOf('/debtContract') == 0) {
					// contract.getDebtContract();

				} else if ($location.path().indexOf('/overdueContract') == 0) {
					// contract.getOverdueContract();

				} else if ($location.path().indexOf('/contractDetail') == 0) {
					// 获取合同查看界面
					selectContractById();
					addStage();
					dateformat();// 格式化日期格式

					// 查找设计部人员
					services.selectUsersFromDesign({}).success(function(data) {
						contract.userDepts = data;
					});

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