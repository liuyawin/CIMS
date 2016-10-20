var app = angular
		.module(
				'contract',
				[ 'ngRoute', 'angularFileUpload' ],
				function($httpProvider) { // ngRoute引入路由依赖
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

/*
 * var permissionList; angular.element(document).ready(function() {
 * console.log("获取权限列表！"); permissionList = "W"; // console.log("身份是：" +
 * permissionList); angular.bootstrap(document, ['contract']);
 * $.get('/CIMS/login/getUserPermission.do', function(data) { permissionList =
 * data.permission; // console.log("身份是：" + permissionList);
 * angular.bootstrap(document, ['contract']); }); });
 */

/*
 * app.run([ 'permissions', function(permissions) {
 * permissions.setPermissions(permissionList) } ]);
 */
app.run([ '$rootScope', '$location', function($rootScope, $location) {
	// permissions.setPermissions(permissionList)
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		// permissions.setPermissions(permissionList);
		$rootScope.$broadcast('reGetData');
	});
} ]);

/*
 * angular.module('contract').factory('permissions', function($rootScope) {
 * return { setPermissions: function(permissions) { permissionList =
 * permissions; $rootScope.$broadcast('permissionsChanged') }, hasPermission:
 * function(permission) { permission = permission.trim(); console.log("后台传来的权限：" +
 * permissionList); return permissionList.indexOf(permission) != -1; } }; });
 * 
 * angular.module('contract').directive('hasPermission', function(permissions) {
 * return { link: function(scope, element, attrs) {
 * 
 * var value = attrs.hasPermission.trim(); // value:界面上的权限
 * 
 * function toggleVisibilityBasedOnPermission() { console.log("界面上的权限：" +
 * value); var hasPermission = permissions.hasPermission(value); //
 * hasPermission console.log("是否有这个权限：" + hasPermission); if(hasPermission)
 * element.show(); else element.hide(); } toggleVisibilityBasedOnPermission();
 * scope.$on('permissionsChanged', toggleVisibilityBasedOnPermission); } }; });
 */

// 路由配置
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/contractList',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/debtContract',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/overdueContract',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/finishedContract',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
										controller : 'ContractController'
									})
							.when(
									'/contractAdd',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractAdd.html',
										controller : 'ContractController'
									})
							.when(
									'/contractDetail',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractDetail.html',
										controller : 'ContractController'
									})
							.when(
									'/contractModify',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractDetail.html',
										controller : 'ContractController'
									})
							.when(
									'/contractInfo',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractInfo.html',
										controller : 'ContractController'
									})
							.when(
									'/contractUpdate',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractUpdate.html',
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
										templateUrl : '/CIMS/jsp/contractInformation/contractInfo.html',
										controller : 'ContractController'
									});
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getContractList = function(data) {
		/* console.log("发送请求获取合同信息"); */
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getContractList.do',
			data : data
		});
	};

	services.getDebtContract = function(data) {
		/* console.log("发送请求获取合同信息"); */
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getDebtContract.do',
			data : data
		});
	};

	services.getOverdueContract = function(data) {
		/* console.log("发送请求获取合同信息"); */
		return $http({
			method : 'post',
			url : baseUrl + 'contract/getOverdueContract.do',
			data : data
		});
	};

	services.getFinishedContract = function(data) {
		/* console.log("发送请求获取合同信息"); */
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

	services.addContract = function(data, file) {
		console.log(data);
		return $http({
			method : 'post',
			url : baseUrl + 'contract/addContract.do',
			data : data,
			file : file
		});
	};

	services.updateConById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/updateConById.do',
			data : data,
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
			url : baseUrl + 'receiveNode/selectRenoByContId.do',
			data : data
		});
	};

	services.selectFileByConId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'file/selectFileByConId.do',
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
	// zq从设计部取出项目经理人选
	services.selectUsersFromDesign = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUsersFromDesign.do',
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
	return services;
} ]);

app.controller('ContractController', [
		'$scope',
		'services',
		'$location',
		'FileUploader',
		function($scope, services, $location, FileUploader) {
			// 合同
			var contract = $scope;
			contract.flag = 0; // 标志位，用于控制按钮是否显示
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
					findType : "4",
					contName : ""
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
				console.log(contract.file);
				console.log(contract.contract.cont_cheader);
				services.addContract({
					contract : conFormData
				}, contract.file).success(function(data) {
					sessionStorage.setItem("contractId", data);
					alert("创建合同成功！");
				});
			};
			// 修改合同
			contract.updateContract = function() {
				var conFormData = JSON.stringify(contract.cont);
				console.log(conFormData);
				services.updateConById({
					contract : conFormData
				}).success(function(data) {
					alert("修改合同成功！");
				});
			};
			// zq查看合同ID，并记入sessionStorage
			contract.getConId = function(conId) {
				sessionStorage.setItem('conId', conId);
			};

			function preventDefault(e) {
				if (e && e.preventDefault) {
					// 阻止默认浏览器动作(W3C)
					e.preventDefault();
				} else {
					// IE中阻止函数器默认动作的方式
					window.event.returnValue = false;
					return false;
				}
			}
			// 删除合同
			contract.deleteContract = function(e) {
				var pageType = $location.path();
				var conId = this.con.cont_id;
				var msg = "确认删除该合同？";
				if (confirm(msg) == true) {
					services.deleteContract({
						conId : conId,
						pageType : pageType.substring(1, pageType.length)
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						alert("删除成功！");
						preventDefault(e);
					});
				} else {
					preventDefault(e);
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
					taskType : "1", // 1代表文书任务
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
					taskType : "2", // 2代表执行管控任务
					conId : conId
				}).success(function(data) {
					alert("添加执行管控任务成功！");
				});
			};
			// zq：从设计部查找人员
			function selectUsersFromDesign() {
				services.selectUsersFromDesign({}).success(function(data) {
					contract.userDepts = data;
				});
			}
			// zq：读取合同的信息
			function selectContractById() {
				var cont_id = sessionStorage.getItem('conId');
				console.log("合同id：" + cont_id);
				services.selectContractById({
					cont_id : cont_id
				}).success(
						function(data) {
							contract.cont = data;
							contract.contract = data;
							contract.contract.cont_stime = new Date(
									data.cont_stime).toLocaleDateString()
									.replace(/\//g, '-');

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

			contract.selectAllTask = function() {
				var $selectAll = $("#selectAll");
				console.log($selectAll.is(':checked'));
				if ($selectAll.is(':checked')) {
					$(":checkbox").attr("checked", true);
				} else {
					$(":checkbox").attr("checked", false);
				}
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

			// zq添加添加收据功能
			// zq查看合同ID，并记入sessione
			contract.addReceipt = function() {
				var renoId = this.node.reno_id;
				var contId = this.node.contract.cont_id;
				$(".overlayer").fadeIn(200);
				$("#tipAdd").fadeIn(200);
				$("#sureAdd").click(function() {
					var receFormData = JSON.stringify(contract.receipt);
					services.addReceipt({
						receipt : receFormData,
						renoId : renoId,
						contId : contId
					}).success(function(data) {

						$("#tipAdd").fadeOut(100);
						$(".overlayer").fadeOut(200);
						selectRenoByContId();
						alert("收据添加成功！");
						contract.receipt = "";

					});
				});

				$("#cancelAdd").click(function() {
					$("#tipAdd").fadeOut(100);
					$(".overlayer").fadeOut(200);
					contract.receipt = "";
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
			// 添加单个工期阶段
			contract.addOneStage = function() {// 动态添加工期阶段
				$scope.fchat = new Object();
				console.log("工期阶段" + JSON.stringify($scope.fchat));
				$scope.fchat.stages = [ {
					key : 0,
					value : ""
				} ];
				$(".overlayer").fadeIn(200);
				$("#prstAdd").fadeIn(200);
				$("#sureAddPrst").click(function() {
					var conId = sessionStorage.getItem("conId");
					var prstFormData = JSON.stringify($scope.fchat);
					console.log(prstFormData);
					services.addProjectStage({
						projectStage : prstFormData,
						cont_id : conId
					}).success(function(data) {
						alert("添加工期成功！");
						selectPrstByContId();
					});
					$(".overlayer").fadeOut(100);
					$("#prstAdd").fadeOut(100);
				});

				$("#cancelAddPrst").click(function() {

					$(".overlayer").fadeOut(100);
					$("#prstAdd").fadeOut(100);
				});

			}

			// zq：补录合同
			contract.repeatAddContract = function() {
				console.log(contract.contract);
				var conFormData = JSON.stringify(contract.contract);
				console.log(conFormData);
				services.repeatAddContract({
					contract : conFormData,
					cont_id : sessionStorage.getItem('conId')
				}).success(function(data) {
					/* window.sessionStorage.setItem("contractId",); */
					alert("添加合同成功！");
				});
			};
			// zq：添加工期阶段到数据库
			contract.addProjectStage = function() {
				var cont_id = sessionStorage.getItem("conId");
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
			// zq：添加收款节点到数据库
			contract.addReceiveNode = function() {
				var cont_id = sessionStorage.getItem("conId");
				var renoFormData = JSON.stringify($scope.rnchat);
				console.log(renoFormData);
				services.addReceiveNode({
					receiveNode : renoFormData,
					cont_id : cont_id
				}).success(function(data) {
					alert("添加节点成功！");
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

				} // 减少回复数
				$scope.rnchat.decrNode = function($index) {

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
			// 添加单个收款节点
			contract.addOneNode = function() {// 动态添加工期阶段

				$scope.rnchat = new Object();
				$scope.rnchat.nodes = [ {
					key : 0,
					value : ""
				} ];
				selectPrstByContId();
				$(".overlayer").fadeIn(200);
				$("#renoAdd").fadeIn(200);
				$("#sureAddReno").click(function() {

					var conId = sessionStorage.getItem("conId");
					var renoFormData = JSON.stringify($scope.rnchat);
					console.log(renoFormData);
					services.addReceiveNode({
						receiveNode : renoFormData,
						cont_id : conId
					}).success(function(data) {
						selectRenoByContId();
						alert("添加收款节点成功！");
					});
					$(".overlayer").fadeOut(100);
					$(".tip").fadeOut(100);
				});

				$("#cancelAddReno").click(function() {

					$(".overlayer").fadeOut(100);
					$(".tip").fadeOut(100);
				});

			}

			// 初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				// 点击创建任务时弹出模态框
				contract.newTask = function() {
					console.log("弹出模态框！");
					var conId = this.con.cont_id;
					services.getAllUsers().success(function(data) {
						contract.users = data;
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
						services.addTask({
							task : task1,
							taskType : "1", // 1代表文书任务
							conId : conId
						}).success(function(data) {
							alert("添加文书任务成功！");
						});
					} else if (contract.task.task_type == "0") {
						var task2 = JSON.stringify(contract.task2);
						services.addTask({
							task : task2,
							taskType : "2", // 2代表执行管控任务
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

				if ($location.path().indexOf('/contractList') == 0) { // 如果是合同列表页
					contract.flag = 1; // 标志位，用于控制按钮是否显示
					services.getContractList({
						page : 1
					}).success(function(data) {
						// 合同列表分页
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getContractList(p); // 点击页码时获取第p页的数据
								}
							});
						}
						/*
						 * // 点击创建任务时弹出模态框 contract.newTask = function() {
						 * console.log("弹出模态框！"); var conId = this.con.cont_id;
						 * services.getAllUsers().success(function(data) {
						 * contract.users = data;
						 * sessionStorage.setItem("contractId", conId); });
						 * $(".overlayer").fadeIn(200); $(".tip").fadeIn(200);
						 * return false; };
						 * 
						 * $(".tiptop a").click(function() {
						 * sessionStorage.setItem("contractId", "");
						 * $(".overlayer").fadeOut(200); $(".tip").fadeOut(200);
						 * });
						 * 
						 * $(".sure").click(function() { var conId =
						 * sessionStorage.getItem("contractId"); if
						 * (contract.task.task_type == "1") { var task1 =
						 * JSON.stringify(contract.task1); services.addTask({
						 * task : task1, taskType : "1",// 1代表文书任务 conId : conId
						 * }).success(function(data) { alert("添加文书任务成功！"); }); }
						 * else if (contract.task.task_type == "0") { var task2 =
						 * JSON.stringify(contract.task2); services.addTask({
						 * task : task2, taskType : "2",// 2代表执行管控任务 conId :
						 * conId }).success(function(data) {
						 * alert("添加执行管控任务成功！"); }); }
						 * $(".overlayer").fadeOut(100); $(".tip").fadeOut(100);
						 * });
						 * 
						 * $(".cancel").click(function() {
						 * sessionStorage.setItem("contractId", "");
						 * $(".overlayer").fadeOut(100); $(".tip").fadeOut(100);
						 * });
						 * 
						 * $(".taskType").change(function() { if
						 * (contract.task.task_type == "1") {
						 * $("#addTask1-form").slideDown(200);
						 * $("#addTask2-form").hide(); } else if
						 * (contract.task.task_type == "0") {
						 * $("#addTask1-form").hide();
						 * $("#addTask2-form").slideDown(200); } });
						 */
					});

				} else if ($location.path().indexOf('/debtContract') == 0) {
					contract.flag = 1; // 标志位，用于控制按钮是否显示
					services.getDebtContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getDebtContract(p); // 点击页码时获取第p页的数据
								}
							});
						}
					});
				} else if ($location.path().indexOf('/contractDetail') == 0) {
					selectUsersFromDesign();// 查找设计部人员

					selectContractById(); // 根据ID获取合同信息
					addStage();// 显示工期阶段录入界面
				} else if ($location.path().indexOf('/contractModify') == 0) {
					selectUsersFromDesign();// 查找设计部人员
					selectContractById(); // 根据ID获取合同信息
					$("#prstContainer").hide();
					$("#renoContainer").hide();
				} else if ($location.path().indexOf('/overdueContract') == 0) {
					contract.flag = 1; // 标志位，用于控制按钮是否显示
					services.getOverdueContract({
						page : 1
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getOverdueContract(p); // 点击页码时获取第p页的数据
								}
							});
						}
					});
				} else if ($location.path().indexOf('/finishedContract') == 0) { // 获取终结合同信息
					contract.flag = 0; // 标志位，用于控制按钮是否显示
					services.getFinishedContract({
						page : 1,
						findType : "4",
						contName : ""
					}).success(function(data) {
						contract.contracts = data.list;
						contract.totalPage = data.totalPage;
						var $pages = $(".tcdPageCode");
						if ($pages.length != 0) {
							$pages.createPage({
								pageCount : contract.totalPage,
								current : 1,
								backFn : function(p) {
									contract.getFinishedContract(p); // 点击页码时获取第p页的数据
								}
							});
						}
					});
				} else if ($location.path().indexOf('/contractAdd') == 0) {
					// 这里先获取人员列表
					console.log("进入添加合同页面！");
					services.getAllUsers().success(function(data) {
						contract.users = data;
						sessionStorage.setItem("contractId", "");
					});
					// 输入时间的input默认值设置为当前时间
					var date = new Date();
					var timeNow = date.getFullYear() + "-"
							+ (date.getMonth() + 1) + "-" + (date.getDate());
					contract.task1 = {
						task_stime : timeNow,
						task_etime : timeNow
					};
					contract.task2 = {
						task_stime : timeNow,
						task_etime : timeNow
					};
					console.log("离开添加合同页面！");
				} else if ($location.path().indexOf('/prstInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
					selectRenoByContId(); // 根据合同ID获取该合同的收款节点
					$("#renoInformation").hide();
					$("#contInformation").hide();
				} else if ($location.path().indexOf('/renoInfo') == 0) {
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
					selectRenoByContId(); // 根据合同ID获取该合同的收款节点
					$("#contInformation").hide();
					$("#prstInformation").hide();
				} else if ($location.path().indexOf('/contractInfo') == 0) {
					// zq添加查找合同详情
					selectContractById(); // 根据ID获取合同信息
					selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
					selectRenoByContId(); // 根据合同ID获取该合同的收款节点
					$("#renoInformation").hide();
					$("#prstInformation").hide();
				} else if ($location.path().indexOf('/contractUpdate') == 0) {
					selectContractById(); // 根据ID获取合同信息
					services.selectFileByConId({
						conId : sessionStorage.getItem('conId')
					}).success(function(data) {
						contract.fileList = data.list;
					});
				}
			}

			initData();
			$scope.$on('reGetData', function() {
				console.log("重新获取数据！");
				initData();
			});

			// 验证日期输入格式
			var $dateFormat = $(".dateFormat");
			var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
			$(".dateFormat").blur(function() {
				if (!dateRegexp.test(this.value)) {
					$(this).parent().children("span").css('display', 'inline');
				}
			});
			$(".dateFormat").click(function() {
				$(this).parent().children("span").css('display', 'none');
			});

		} ]);
app
		.controller(
				"UploadController",
				[
						'$scope',
						'FileUploader',
						function($scope, FileUploader) {
							/* ！！！上传文件 */
							var uploader = $scope.uploader = new FileUploader({
								url : '/CIMS/file/upload.do',
								data : {
									conId : sessionStorage
											.getItem("contractId")
								}
							});

							// FILTERS

							uploader.filters.push({
								name : 'customFilter',
								fn : function(
										item /* {File|FileLikeObject} */,
										options) {
									return this.queue.length < 10;
								}
							});

							// CALLBACKS

							uploader.onWhenAddingFileFailed = function(
									item /* {File|FileLikeObject} */, filter,
									options) {
								console.info('onWhenAddingFileFailed', item,
										filter, options);
							};
							uploader.onAfterAddingFile = function(fileItem) {
								console.info('onAfterAddingFile', fileItem);
							};
							uploader.onAfterAddingAll = function(addedFileItems) {
								console
										.info('onAfterAddingAll',
												addedFileItems);
							};
							uploader.onBeforeUploadItem = function(item) {
								console.info('onBeforeUploadItem', item);
							};
							uploader.onProgressItem = function(fileItem,
									progress) {
								console.info('onProgressItem', fileItem,
										progress);
							};
							uploader.onProgressAll = function(progress) {
								console.info('onProgressAll', progress);
							};
							uploader.onSuccessItem = function(fileItem,
									response, status, headers) {
								console.info('onSuccessItem', fileItem,
										response, status, headers);
							};
							uploader.onErrorItem = function(fileItem, response,
									status, headers) {
								console.info('onErrorItem', fileItem, response,
										status, headers);
							};
							uploader.onCancelItem = function(fileItem,
									response, status, headers) {
								console.info('onCancelItem', fileItem,
										response, status, headers);
							};
							uploader.onCompleteItem = function(fileItem,
									response, status, headers) {
								console.info('onCompleteItem', fileItem,
										response, status, headers);
							};
							uploader.onCompleteAll = function() {
								console.info('onCompleteAll');
							};
							console.info('uploader', uploader);
							/* ！！！上传文件完 */
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