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
var permissionList;
angular.element(document).ready(function() {
	console.log("获取权限列表！");
	$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data; // 
		console.log("身份是：" + permissionList);
		angular.bootstrap($("#ng-section"), [ 'contract' ]); // 手动加载angular模块
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
									})
							.when(
									'/contractRecord',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractRecord.html',
										controller : 'ContractController'
									})
							.when(
									'/stopedContract',
									{
										templateUrl : '/CIMS/jsp/contractInformation/contractList.html',
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
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectConByName.do',
			data : data
		});
	};
	// 分页获取合同数据
	services.selectConByPage = function(data) {
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
	// lwt根据合同ID获取合同操作记录
	services.selectContRecordByContId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contractRecord/selectContRecordByContId.do',
			data : data
		});
	};
	// 获取文件列表
	services.selectFileByConId = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'file/selectFileByConId.do',
			data : data
		});
	};
	// 删除文件
	services.deleteFileById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'file/deleteFileById.do',
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

	// 10.25zq工期完成
	services.finishPrst = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/finishPrst.do',
			data : data
		});
	};
	// 10.25zq工期删除
	services.delPrst = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/delPrst.do',
			data : data
		});
	};
	// 10.25zq工期修改
	services.modifyPrst = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/modifyPrst.do',
			data : data
		});
	};
	// 10.25zq收款删除
	services.delReno = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/delReno.do',
			data : data
		});
	};
	// 10.25zq收款修改
	services.modifyReno = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/modifyReno.do',
			data : data
		});
	};
	services.selectRenoById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'receiveNode/selectRenoById.do',
			data : data
		});
	}
	services.selectPrstById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'projectStage/selectPrstById.do',
			data : data
		});
	};
	// lwt:修改项目状态
	services.modifyStatus = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'contract/modifyStatus.do',
			data : data
		});
	};
	// lwt:停建合同
	services.getStopedContract = function(data) {
		/* console.log("发送请求获取合同信息"); */
		return $http({
			method : 'post',
			url : baseUrl + 'contract/selectContract.do',
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
						'FileUploader',
						function($scope, services, $location, FileUploader) {
							// 合同
							var contract = $scope;
							contract.flag = 0; // 标志位，用于控制按钮是否显示
							contract.reNode = null;

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
								services.getDebtContract({}).success(
										function(data) {
											contract.contracts = data;
										});
							};
							// 获取逾期合同
							contract.getOverdueContract = function() {
								services.getOverdueContract({}).success(
										function(data) {
											contract.contracts = data.list;
										});
							};
							// 获取终结合同
							contract.getFinishedContract = function() {
								services.getFinishedContract({
									findType : "4",
									contName : ""
								}).success(function(data) {
									contract.contracts = data.list;
								});
							};
							// lwt:获取停建合同
							contract.getStopedContract = function() {
								services.getFinishedContract({
									findType : "5",
									contName : ""
								}).success(function(data) {
									contract.contracts = data.list;
								});
							};
							// 通过合同名获取合同信息
							contract.selectConByName = function() {
								services.selectConByName({
									contName : $("#cName").val(),
									page : 1
								}).success(function(data) {
									contract.contracts = data.list;
									contract.totalPage = data.totalPage;
								});
							};
							// 添加合同
							contract.addContract = function() {
								var province = $("#province").val();
								var city = $("#city").val();
								contract.contract.province = province;
								contract.contract.city = city;
								var conFormData = JSON
										.stringify(contract.contract);
								services.addContract({
									contract : conFormData
								}, contract.file).success(function(data) {
									sessionStorage.setItem("conId", data);
									contract.contract.cont_id = data;
									$("#province").val(province);
									$("#province").change();
									$("#city").val(city);
									$("#city").change();
									$("#addContract").hide();
									$("#updateContract").show();
									alert("创建合同成功！");
								});
							};
							// 修改合同
							contract.updateContract = function() {
								var province = $("#province").val();
								var city = $("#city").val();
								contract.contract.province = province;
								contract.contract.city = city;
								var conFormData = JSON
										.stringify(contract.contract);
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
									services
											.deleteContract(
													{
														conId : conId,
														pageType : pageType
																.substring(
																		1,
																		pageType.length)
													})
											.success(
													function(data) {
														contract.contracts = data.list;
														alert(data.list
																.length());
														contract.totalPage = data.totalPage;
														console
																.log(contract.totalPage);
														var $pages = $(".tcdPageCode");
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getContractList(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
														preventDefault(e);
														alert("删除成功！");
													});
								} else {
									preventDefault(e);
								}
							}
							// 删除上传的文件
							contract.deleteFile = function(e) {
								preventDefault(e);
								var fileId = this.file.file_id;
								services.deleteFileById({
									fileId : fileId
								}).success(
										function(data) {
											selectFileByConId(sessionStorage
													.getItem("conId"));
											alert("删除文件成功！");
										});
							};
							// 添加文书任务
							contract.addTask1 = function() {
								var conId = sessionStorage.getItem("conId");
								if (conId.trim() == "") {
									alert("请先录入合同信息！");
									return false;
								}
								var task1 = JSON.stringify(contract.task1);
								services.addTask({
									task : task1,
									taskType : "1", // 1代表文书任务
									conId : conId
								}).success(function(data) {
									alert("添加文书任务成功！");
									$("#disTask1").attr("disabled", "true");
								});
							};
							// 添加执行管控任务
							contract.addTask2 = function() {
								var conId = sessionStorage.getItem("conId");
								if (conId.trim() == "") {
									alert("请先录入合同信息！");
									return false;
								}
								var task2 = JSON.stringify(contract.task2);
								services.addTask({
									task : task2,
									taskType : "2", // 2代表执行管控任务
									conId : conId
								}).success(function(data) {
									alert("添加补录合同任务成功！");
									$("#disTask2").attr("disabled", "true");
								});
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
													contract.contract = data.contract;
													if (data.contract.cont_stime) {
														contract.contract.cont_stime = changeDateType(data.contract.cont_stime);
													}

												});
							}
							// 在修改合同的时候将已选的项目阶段勾选
							function getContProStage() {
								var cont_id = sessionStorage.getItem('conId');
								services
										.selectContractById({
											cont_id : cont_id
										})
										.success(
												function(data) {
													contract.cont = data.contract;
													if (data.contract.pro_stage) {
														var strs = data.contract.pro_stage
																.split(",");
														for (var i = 0; i < strs.length; i++) {

															switch (strs[i]) {
															case "0":
																contract.proStage0 = "true";
																break;
															case "1":
																contract.proStage1 = "true";
																break;
															case "2":
																contract.proStage2 = "true";
																break;
															case "3":
																contract.proStage3 = "true";
																break;
															case "4":
																contract.proStage4 = "true";
																break;
															case "5":
																contract.proStage5 = "true";
																break;
															case "6":
																contract.proStage6 = "true";
																break;
															case "7":
																contract.proStage7 = "true";
																break;
															case "8":
																contract.proStage8 = "true";
																break;
															}
														}
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

							// 获取跟合同相关的上传文件列表
							function selectFileByConId(conId) {
								services.selectFileByConId({
									conId : conId
								}).success(function(data) {
									contract.fileList = data.list;
								});
							}

							contract.selectAllTask = function() {
								var $selectAll = $("#selectAll");
								if ($selectAll.is(':checked')) {
									contract.task1.print = "true";
									contract.task1.sign = "true";
									contract.task1.seal = "true";
									contract.task1.post = "true";
									contract.task1.file = "true";
								} else {
									contract.task1.print = "false";
									contract.task1.sign = "false";
									contract.task1.seal = "false";
									contract.task1.post = "false";
									contract.task1.file = "false";
								}
							}

							// 项目阶段的全选
							contract.selectAllProStage = function() {
								var $selectAll = $("#selectAllProStage");
								if ($selectAll.is(':checked')) {
									contract.proStage0 = "true";
									contract.proStage1 = "true";
									contract.proStage2 = "true";
									contract.proStage3 = "true";
									contract.proStage4 = "true";
									contract.proStage5 = "true";
									contract.proStage6 = "true";
									contract.proStage7 = "true";
									contract.proStage8 = "true";
								} else {
									contract.proStage0 = "false";
									contract.proStage1 = "false";
									contract.proStage2 = "false";
									contract.proStage3 = "false";
									contract.proStage4 = "false";
									contract.proStage5 = "false";
									contract.proStage6 = "false";
									contract.proStage7 = "false";
									contract.proStage8 = "false";
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
								 * $scope.fchat.combineReplies = function() {
								 * var cr = ""; for (var i = 0; i <
								 * $scope.fchat.stages.length; i++) { cr += "#" +
								 * $scope.fchat.stages[i].value; }
								 * 
								 * return cr; }
								 */
							}
							// 添加单个工期阶段
							contract.addOneStage = function() {
								if (!contract.contract.manager) {
									alert("请先选择项目设总！");
									return false;
								}
								// 动态添加工期阶段
								$(".overlayer").fadeIn(200);
								$("#prstAdd").fadeIn(200);
								// 输入时间的input默认值设置为当前时间
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								contract.prStage = {
									prst_etime : timeNow

								};

							}
							contract.addOneProStage = function() {
								var conId = sessionStorage.getItem("conId");
								var prstFormData = JSON
										.stringify(contract.prStage);
								console.log("添加单个工期" + prstFormData);
								services.addProjectStage({
									projectStage : prstFormData,
									cont_id : conId
								}).success(function(data) {
									alert("添加工期成功！");
									selectPrstByContId();
								});

								$(".overlayer").fadeOut(100);
								$("#prstAdd").fadeOut(100);
							}

							$("#cancelAddPrst").click(function() {

								$(".overlayer").fadeOut(100);
								$("#prstAdd").fadeOut(100);
							});
							// zq：补录合同
							contract.repeatAddContract = function() {
								var list = document
										.getElementsByClassName("proStage");
								contract.contract.proStage = "";
								for (var i = 0; i < list.length; i++) {
									if (list[i].checked) {
										contract.contract.proStage += ""
												+ list[i].value + ",";
									}
								}

								if (contract.contract.proStage) {
									var conFormData = JSON
											.stringify(contract.contract);
									services.repeatAddContract(
											{
												contract : conFormData,
												cont_id : sessionStorage
														.getItem('conId')
											}).success(function(data) {
										/* window.sessionStorage.setItem("contractId",); */
										alert("修改合同成功！");
										window.history.go(-1);
										window.location.reload();
									});

								} else {
									alert("请选择项目阶段！");
								}

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
								$("#ps" + num + "").find("img").css(
										'visibility', 'visible');
							}
							// ps显示图片
							contract.psHideDel = function(num) {
								$("#ps" + num + "").find("img").css(
										'visibility', 'hidden');
							}
							// rn显示图片
							contract.rnShowDel = function(num) {
								$("#rn" + num + "").find("img").css(
										'visibility', 'visible');
							}
							// rn显示图片
							contract.rnHideDel = function(num) {
								$("#rn" + num + "").find("img").css(
										'visibility', 'hidden');
							}
							// zq：添加收款节点到数据库
							contract.addReceiveNode = function() {
								var cont_id = sessionStorage.getItem("conId");
								var renoFormData = JSON
										.stringify($scope.rnchat);
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
								selectPrstByContId();
								$(".overlayer").fadeIn(200);
								$("#renoAdd").fadeIn(200);
								// 输入时间的input默认值设置为当前时间
								var date = new Date();
								var timeNow = date.getFullYear() + "-"
										+ (date.getMonth() + 1) + "-"
										+ (date.getDate());
								contract.reNode = {
									reno_time : timeNow
								};
							}
							contract.addOneReNode = function() {
								var conId = sessionStorage.getItem("conId");
								var renoFormData = JSON
										.stringify(contract.reNode);
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
							}

							$("#cancelAddReno").click(function() {

								$(".overlayer").fadeOut(100);
								$(".tip").fadeOut(100);
							});
							// zq：从设计部查找人员
							function selectUsersFromDesign() {
								services.selectUsersFromDesign({}).success(
										function(data) {
											contract.userDepts = data.list;
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
									var p = cookieString.indexOf("=");
									var name = cookieString.substring(0, p);
									var value = cookieString.substring(p + 1,
											cookieString.length);
									cookie[name.trim()] = value;
									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
									}

								}
							}
							// 10.25zq确认完工
							contract.finishPrst = function() {
								var prstId = this.stage.prst_id;
								sessionStorage.setItem("prstId", prstId);
								$("#tipFinish").fadeIn(200);
								$(".overlayer").fadeIn(200);
							}
							$("#sureFinishPrst").click(function() {
								services.finishPrst({
									prstId : sessionStorage.getItem("prstId")
								}).success(function(data) {
									alert("确认完工成功！");
									selectContractById(); // 根据ID获取合同信息
									selectPrstByContId();// 根据合同查看工期阶段
									selectRenoByContId();// 根据合同ID查看收款节点
									$("#tipFinish").fadeOut(100);
									$(".overlayer").fadeOut(200);
								})
							});
							$("#cancelFinishPrst").click(function() {
								$("#tipFinish").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 10.25zq删除工期和收款
							contract.delPrst = function() {
								if (this.stage.prst_state == 0) {
									var prstId = this.stage.prst_id;
									sessionStorage.setItem("delType", "prst");
									sessionStorage.setItem("prstId", prstId);
									$("#tipDel").fadeIn(200);
									$(".overlayer").fadeIn(200);
								} else {
									alert("该工期已完成不能删除！");
								}

							};
							// 10.25zq删除收款节点
							contract.delReno = function() {
								if (this.node.reno_state == 0) {
									var renoId = this.node.reno_id;
									sessionStorage.setItem("delType", "reno");
									sessionStorage.setItem("renoId", renoId);
									$("#tipDel").fadeIn(200);
									$(".overlayer").fadeIn(200);
								} else {
									alert("该收款节点处于未收全款或已收全款，不能删除！");
								}

							};
							$("#sureDel")
									.click(
											function() {
												$("#tipDel").fadeOut(100);
												$(".overlayer").fadeOut(200);
												if (sessionStorage
														.getItem("delType") == "prst") {
													services
															.delPrst(
																	{
																		prstId : sessionStorage
																				.getItem("prstId")
																	})
															.success(
																	function(
																			data) {
																		alert("删除成功！");
																		selectPrstByContId();// 根据合同查看工期阶段
																	});
												} else if (sessionStorage
														.getItem("delType") == "reno") {
													services
															.delReno(
																	{
																		renoId : sessionStorage
																				.getItem("renoId")
																	})
															.success(
																	function(
																			data) {
																		alert("删除成功！");
																		selectRenoByContId();// 根据合同查看工期阶段
																	});
												}

											});

							$("#cancelDel").click(function() {
								$("#tipDel").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 10.25zq删除工期和收款完
							// 10.25zq修改工期阶段
							contract.modifyPrst = function() {
								var prstId = this.stage.prst_id;
								sessionStorage.setItem("prstId", prstId);
								services
										.selectPrstById({
											prstId : prstId
										})
										.success(
												function(data) {
													contract.prStage = data.projectStage;
													$scope.prStage.prst_etime = changeDateType(data.projectStage.prst_etime);

												});

								/*
								 * contract.prStage = this.stage;
								 * contract.prStage.prst_etime =
								 * changeDateType(time);
								 */
								$(".overlayer").fadeIn(200);
								$("#prstModify").fadeIn(200);
							}
							contract.modifyOnePrst = function() {
								$("#prstModify").fadeOut(100);
								$(".overlayer").fadeOut(200);
								var prst = JSON.stringify($scope.prStage);
								services.modifyPrst({
									prstId : sessionStorage.getItem("prstId"),
									projectStage : prst
								}).success(function(data) {
									alert("修改成功！");
									selectPrstByContId();
									selectRenoByContId();
								});
							}

							$("#cancelModifyPrst").click(function() {
								$("#prstModify").fadeOut(100);
								$(".overlayer").fadeOut(200);

							});
							// 10.25zq修改收款节点
							contract.modifyReno = function() {
								var renoId = this.node.reno_id;
								contract.reNode = null;
								services
										.selectRenoById({
											renoId : renoId
										})
										.success(
												function(data) {
													contract.reNode = data.receiveNode;
													$scope.reNode.reno_time = changeDateType(data.receiveNode.reno_time);
												});
								sessionStorage.setItem("renoId", renoId);
								/*
								 * contract.reNode = this.node;
								 * contract.reNode.reno_time =
								 * changeDateType(this.node.reno_time);
								 */
								$(".overlayer").fadeIn(200);
								$("#renoModify").fadeIn(200);

							}
							contract.modifyOneReno = function() {
								$("#renoModify").fadeOut(100);
								$(".overlayer").fadeOut(200);
								var reno = JSON.stringify($scope.reNode);
								console.log("修改收款节点" + reno);
								services.modifyReno({
									renoId : sessionStorage.getItem("renoId"),
									receiveNode : reno
								}).success(function(data) {
									alert("修改成功！");
									selectRenoByContId();
								});
							}

							$("#cancelModifyReno").click(function() {
								$("#renoModify").fadeOut(100);
								$(".overlayer").fadeOut(200);
							});
							// 10.25zq更改时间的样式
							function changeDateType(date) {
								console.log("传进来的时间" + date);
								if (date != "") {
									var DateTime = new Date(date.time)
											.toLocaleDateString().replace(
													/\//g, '-');
								} else {
									var DateTime = "";
								}
								console.log("转化后的的时间" + DateTime);
								return DateTime;
							}
							// 10.26zq实现选择工期时的联动
							contract.prstChange = function(type) {
								// 0表示添加，1表示修改
								if (type == "0") {

									if ($("#nodePrstAdd").val() != "") {
										$("#nodeContentAdd").val(
												contract.prst[$("#nodePrstAdd")
														.val()].prst_content);

										contract.reNode.reno_content = contract.prst[$(
												"#nodePrstAdd").val()].prst_content;

									} else {
										$("#nodeContentAdd").val("");
										contract.reNode.reno_content = "";
									}

								} else if (type == "1") {
									if ($("#nodePrstModify").val() != "") {
										$("#nodeContentModify").val(
												contract.prst[$(
														"#nodePrstModify")
														.val()].prst_content);
										contract.reNode.reno_content = contract.prst[$(
												"#nodePrstModify").val()].prst_content;
									} else {
										$("#nodeContentModify").val("");
										contract.reNode.reno_content = "";
									}

								}
							}
							// lwt:点击项目状态时弹出模态框
							contract.modifyStatus = function(conId) {
								sessionStorage.setItem("conId", conId);
								$(".overlayer").fadeIn(200);
								$("#tipStatus").fadeIn(200);
								contract.status = {
									status_type : this.con.cont_state
								}

							};
							$("#sureStatus").click(function() {
								var conId = sessionStorage.getItem("conId");
								services.modifyStatus({
									contState : contract.status.status_type,
									contId : sessionStorage.getItem("conId")
								}).success(function(data) {
									if (data = "true") {
										alert("修改项目状态成功！");
										initData();
									} else {
										alert("修改项目状态失败！");
									}
								});
								$(".overlayer").fadeOut(100);
								$("#tipStatus").fadeOut(100);
							});

							$("#cancelStatus").click(function() {
								$(".overlayer").fadeOut(100);
								$("#tipStatus").fadeOut(100);
							});

							// 初始化页面信息
							function initData() {
								// 点击创建任务时弹出模态框
								contract.newTask = function() {
									var conId = this.con.cont_id;
									services.getAllUsers().success(
											function(data) {
												contract.users = data;
												sessionStorage.setItem("conId",
														conId);
											});
									// 输入时间的input默认值设置为当前时间
									var date = new Date();
									var timeNow = date.getFullYear() + "-"
											+ (date.getMonth() + 1) + "-"
											+ (date.getDate());
									contract.task1 = {
										task_stime : timeNow,
										task_etime : timeNow
									}
									contract.task2 = {
										task_stime : timeNow,
										task_etime : timeNow
									}
									$(".overlayer").fadeIn(200);
									$("#tipType").fadeIn(200);
									return false;
								};

								$(".tiptop a").click(function() {
									/* sessionStorage.setItem("conId", ""); */
									$(".overlayer").fadeOut(200);
									$(".tip").fadeOut(200);
								});

								contract.addNewTask = function() {
									var conId = sessionStorage.getItem("conId");
									if (contract.task.task_type == "1") {
										var task1 = JSON
												.stringify(contract.task1);
										services.addTask({
											task : task1,
											taskType : "1", // 1代表文书任务
											conId : conId
										}).success(function(data) {
											alert("添加文书任务成功！");
										});
									} else if (contract.task.task_type == "0") {
										var task2 = JSON
												.stringify(contract.task2);
										services.addTask({
											task : task2,
											taskType : "2", // 2代表执行管控任务
											conId : conId
										}).success(function(data) {
											alert("添加执行管控任务成功！");
										});
									}
									$(".overlayer").fadeOut(100);
									$("#tipType").fadeOut(100);
								}

								$(".cancel").click(function() {
									/* sessionStorage.setItem("conId", ""); */
									$(".overlayer").fadeOut(100);
									$("#tipType").fadeOut(100);
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
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getContractList(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
													});

								} else if ($location.path().indexOf(
										'/debtContract') == 0) {
									contract.flag = 1; // 标志位，用于控制按钮是否显示
									services
											.getDebtContract({
												page : 1
											})
											.success(
													function(data) {
														contract.contracts = data.list;
														contract.totalPage = data.totalPage;
														var $pages = $(".tcdPageCode");
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getDebtContract(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
													});
								} else if ($location.path().indexOf(
										'/overdueContract') == 0) {
									contract.flag = 1; // 标志位，用于控制按钮是否显示
									services
											.getOverdueContract({
												page : 1
											})
											.success(
													function(data) {
														contract.contracts = data.list;
														contract.totalPage = data.totalPage;
														var $pages = $(".tcdPageCode");
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getOverdueContract(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
													});
								} else if ($location.path().indexOf(
										'/finishedContract') == 0) { // 获取终结合同信息
									contract.flag = 0; // 标志位，用于控制按钮是否显示
									services
											.getFinishedContract({
												page : 1,
												findType : "4",
												contName : ""
											})
											.success(
													function(data) {
														contract.contracts = data.list;
														contract.totalPage = data.totalPage;
														var $pages = $(".tcdPageCode");
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getFinishedContract(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
													});
								} else if ($location.path().indexOf(
										'/contractAdd') == 0) {
									// 这里先获取人员列表
									services.getAllUsers().success(
											function(data) {
												contract.users = data;
												sessionStorage.setItem("conId",
														"");
											});
									// 输入时间的input默认值设置为当前时间
									var date = new Date();
									var timeNow = date.getFullYear() + "-"
											+ (date.getMonth() + 1) + "-"
											+ (date.getDate());
									contract.task1 = {
										task_stime : timeNow,
										task_etime : timeNow
									};
									contract.task2 = {
										task_stime : timeNow,
										task_etime : timeNow
									};
									contract.contract = {
										cont_type : 0,
										cont_rank : 1
									};
									/* contract.contract.cont_type="0"; */
								} else if ($location.path()
										.indexOf('/prstInfo') == 0) {
									selectContractById(); // 根据ID获取合同信息
									selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
									selectRenoByContId(); // 根据合同ID获取该合同的收款节点
									$("#renoInformation").hide();
									$("#contInformation").hide();
								} else if ($location.path()
										.indexOf('/renoInfo') == 0) {
									selectContractById(); // 根据ID获取合同信息
									selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
									selectRenoByContId(); // 根据合同ID获取该合同的收款节点
									$("#contInformation").hide();
									$("#prstInformation").hide();
								} else if ($location.path().indexOf(
										'/contractInfo') == 0) {
									// zq添加查找合同详情
									selectContractById(); // 根据ID获取合同信息
									selectPrstByContId(); // 根据合同ID获取该合同的工期阶段
									selectRenoByContId(); // 根据合同ID获取该合同的收款节点
									$("#contInformation").hide();
									$("#renoInformation").hide();
									$("#prstInformation").hide();
								} else if ($location.path().indexOf(
										'/contractUpdate') == 0) {
									// 根据ID获取合同信息
									var cont_id = sessionStorage
											.getItem('conId');
									services
											.selectContractById({
												cont_id : cont_id
											})
											.success(
													function(data) {
														contract.cont = data.contract;
														contract.contract = data.contract;
														if (data.contract.cont_stime) {
															contract.contract.cont_stime = changeDateType(data.contract.cont_stime);
														}
														$("#province")
																.val(
																		data.contract.province);
														$("#province").change();
														$("#city")
																.val(
																		data.contract.city);
														$("#city").change();
													});
									selectFileByConId(sessionStorage
											.getItem('conId'));
								} else if ($location.path().indexOf(
										'/contractDetail') == 0) {
									selectUsersFromDesign();// 查找设计部人员
									selectContractById(); // 根据ID获取合同信息
									addStage();// 显示工期阶段录入界面

								} else if ($location.path().indexOf(
										'/contractModify') == 0) {
									selectUsersFromDesign();// 查找设计部人员
									selectContractById(); // 根据ID获取合同信息
									getContProStage();
									$("#prstContainer").hide();
									$("#renoContainer").hide();

								} else if ($location.path().indexOf(
										'/contractRecord') == 0) {

									services.selectContRecordByContId(
											{
												cont_id : sessionStorage
														.getItem("conId")
											}).success(function(data) {
										contract.records = data.list;
									});
								} else if ($location.path().indexOf(
										'/stopedContract') == 0) { // lwt:获取停建合同信息
									contract.flag = 0; // 标志位，用于控制按钮是否显示
									services
											.getFinishedContract({
												page : 1,
												findType : "5",
												contName : ""
											})
											.success(
													function(data) {
														contract.contracts = data.list;
														contract.totalPage = data.totalPage;
														var $pages = $(".tcdPageCode");
														if ($pages.length != 0) {
															$pages
																	.createPage({
																		pageCount : contract.totalPage,
																		current : 1,
																		backFn : function(
																				p) {
																			contract
																					.getStopedContract(p); // 点击页码时获取第p页的数据
																		}
																	});
														}
													});
								}
							}

							initData();
							findRoleFromCookie();
							$scope.$on('reGetData', function() {
								initData();
							});

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
								alert("文件上传成功！");
							};
							console.info('uploader', uploader);
							/* ！！！上传文件完 */
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
		}

		return type;
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
app.filter('dateTimeType',
		function() {
			return function(input) {
				var type = "";
				if (input != null) {
					var date = new Date(input).toLocaleDateString().replace(
							/\//g, '-');
					var time = new Date(input).toLocaleTimeString()
					type = date + "  " + time;
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
		else if (!input)
			rank = "";
		return rank;
	}
});
// 合同状态的判断
app.filter('conIsBack', function() {
	return function(input) {
		var status = "";
		if (input == "0")
			status = "待办";
		else if (input == "1")
			status = "在办";
		else if (input == "2")
			status = "在办";
		else if (input == "3")
			status = "在办";
		else if (input == "4")
			status = "已邮寄";
		else if (input == "5")
			status = "已签";
		else if (!input)
			status = "";
		return status;
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
/*
 * app .directive( 'hasPermission', function($timeout) { return { restrict :
 * 'A', link : function(scope, element, attr) {
 * 
 * var key = attr.hasPermission.trim(); // 获取页面上的权限值 console.log("获取页面上的权限值" +
 * key); console.log("cookie内容" + JSON.stringify(cookie));
 * 
 * if (sessionStorage.getItem('userRole').trim() == "3") {
 * element.css("display", "none"); }
 * 
 * switch (sessionStorage.getItem('userRole').trim()) { case "1": var keys1 = "
 * cBodyEdit cPsAdd cPsEdit cPsDel cRnAdd cRnEdit cRnDel bReceAdd tContCollect
 * tInvoFinish bInvoAdd cAdd cHeadEdit cDel cTaskAdd tInvoAudit tContDetail ";
 * var regStr1 = "\\s" + key + "\\s"; var reg1 = new RegExp(regStr1); if
 * (keys1.search(reg1) < 0) { element.css("display", "none"); } break; case "2":
 * var keys2 = " tContDetail "; var regStr2 = "\\s" + key + "\\s"; var reg2 =
 * new RegExp(regStr2); if (keys2.search(reg2) < 0) { element.css("display",
 * "none"); } break; case "3": var keys3 = " cBodyEdit cPsAdd cPsEdit cPsDel
 * cRnAdd cRnEdit cRnDel bReceAdd tContCollect tInvoFinish "; var regStr3 =
 * "\\s" + key + "\\s"; var reg3 = new RegExp(regStr3); if (keys3.search(reg3) <
 * 0) { element.css("display", "none"); } break; case "4": var keys4 = "
 * bInvoAdd tContDetail "; var regStr4 = "\\s" + key + "\\s"; var reg4 = new
 * RegExp(regStr4); if (keys4.search(reg4) < 0) { element.css("display",
 * "none"); } break; case "5": var keys5 = " cAdd cHeadEdit cDel cTaskAdd
 * tInvoAudit tContDetail "; var regStr5 = "\\s" + key + "\\s"; var reg5 = new
 * RegExp(regStr5); if (keys5.search(reg5) < 0) { element.css("display",
 * "none"); } break; } } };
 * 
 * });
 */
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */