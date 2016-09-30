var app = angular
		.module(
				'assistant1TaskMgmt',
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
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/newTaskList', {
		templateUrl : '/CIMS/jsp/assistant1/taskInformation/taskList.html',
		controller : 'TaskController'
	}).when('/contractInfo', {
		templateUrl : '/CIMS/jsp/assistant1/taskInformation/contractInfo.html',
		controller : 'TaskController'
	}).when('/unfinishTaskList', {
		templateUrl : '/CIMS/jsp/assistant1/taskInformation/taskList.html',
		controller : 'TaskController'
	}).when('/finishTaskList', {
		templateUrl : '/CIMS/jsp/assistant1/taskInformation/taskList.html',
		controller : 'TaskController'
	})
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getTaskList = function(data) {
		console.log("发送请求获取合同信息data" + data);
		return $http({
			method : 'post',
			url : baseUrl + 'task/selectTaskByState.do',
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
	services.getTaskByConName = function(data) {
		console.log("通过内容查找子任务" + data.conName);
		return $http({
			method : 'post',
			url : baseUrl + 'task/getTaskByConName.do',
			data : data
		});
	};
	services.selectSubTask = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'subTask/selectSubTask.do',
			data : data
		});
	};
	services.finishTask1= function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'subTask/updateState.do',
			data : data
		});
	};
	return services;
} ]);

app.controller('TaskController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {
			// 合同
			var taskHtml = $scope;
			
			
			// 通过合同名查找子任务
			taskHtml.getTaskByConName = function() {
				services.getTaskByConName({
					conName : $('#conName').val(),
				/* taskState : 0 */
				}).success(function(data) {
					taskHtml.tasks = data.list;
					pageTurn(0, data.totalPage, 1)
				});
			}
			taskHtml.finishTask1=function(){
				
				var task = JSON.stringify($scope.fchat);
				console.log(task);
				services.finishTask1({task:task}).success(function(data){alert("操作成功！");});
			}
			// 点击查看时弹出模态框
			taskHtml.showTask = function() {
				
				var taskId = this.t.task_id;
				/*alert(taskId);*/
				services.selectSubTask({
					taskId : taskId
				}).success(function(data) {
					
					initState();
					$scope.fchat.taskId=taskId;
					taskHtml.subTasks = data.list;
					for (var i = 0; i < data.list.length; i++) {

						switch (data.list[i].suta_content) {
						case 'print':
							if (data.list[i].suta_state == '1') {
								$scope.fchat.task1.print = "true";//选中已经完成的任务
								taskHtml.disPrint = true;
							}
							
							$scope.fchat.subTaskId.print=data.list[i].suta_id;
							taskHtml.taskPrint = true;//显示小任务
							break;
						case 'sign':
							if (data.list[i].suta_state == '1') {
								$scope.fchat.task1.sign = "true";
								taskHtml.disSign = true;

							}
							
							$scope.fchat.subTaskId.sign=data.list[i].suta_id;
							taskHtml.taskSign = true;
							break;
						case 'seal':
							if (data.list[i].suta_state == '1') {
								$scope.fchat.task1.seal = "true";
								taskHtml.disSeal = true;
							}
							
							$scope.fchat.subTaskId.seal=data.list[i].suta_id;
							taskHtml.taskSeal = true;
							break;
						case 'post':
							if (data.list[i].suta_state == '1') {
								$scope.fchat.task1.post = "true";
								taskHtml.disPost = true;
							}
							
							$scope.fchat.subTaskId.post=data.list[i].suta_id;
							taskHtml.taskPost = true;
							break;
						case 'file':
							if (data.list[i].suta_state == '1') {
								$scope.fchat.task1.file = "true";
								taskHtml.disFile = true;
							}
							
							$scope.fchat.subTaskId.file=data.list[i].suta_id;
							taskHtml.taskFile = true;
							break;
						}
					}
				});
				// 这里添加一个方法修改标志位，将该合同的任务由新接收任务改为未完成任务
				$(".overlayer").fadeIn(200);
				$(".tip1").fadeIn(200);
				$(".tiptop a").click(function() {
					$(".overlayer").fadeOut(200);
					$(".tip1").fadeOut(200);
				});

				$(".sure").click(function() {

					$(".overlayer").fadeOut(100);
					$(".tip1").fadeOut(100);
				});

				$(".cancel").click(function() {
					/* sessionStorage.setItem("contractId", ""); */
					$(".overlayer").fadeOut(100);
					$(".tip1").fadeOut(100);
				});
				return false;
			};
			// zq所有任务换页
			function pageTurn(taskState, totalPage, page) {

				var $pages = $(".tcdPageCode");
				console.log($pages.length);
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							getTaskList(taskState, p)
						}
					});
				}
			}
			function initState(){
				$scope.fchat = new Object();
				
				$scope.fchat.task1 = {
						"print" : "",
						"sign" : "",
						"file" : "",
						"seal" : "",
						"post" : ""
					}
				$scope.fchat.subTaskId = {
						"print" : "",
						"sign" : "",
						"file" : "",
						"seal" : "",
						"post" : ""
					}
				$scope.fchat.taskId="";
				taskHtml.disPrint =false;
				taskHtml.disSign =false;
				taskHtml.disSeal =false;
				taskHtml.disPost =false;
				taskHtml.disFile =false;
				
				taskHtml.taskPrint=false;
				taskHtml.taskSign=false;
				taskHtml.taskSeal=false;
				taskHtml.taskPost=false;
				taskHtml.taskFile=false;
			}
			// zq：读取合同的信息
			function selectContractById() {
				var cont_id = sessionStorage.getItem('conId');

				services.selectContractById({
					cont_id : cont_id
				}).success(function(data) {
					taskHtml.cont = data;

				});
			}
			// zq：根据合同ID查询工期阶段的内容
			function selectPrstByContId() {
				var cont_id = sessionStorage.getItem('conId');
				services.selectPrstByContId({
					cont_id : cont_id
				}).success(function(data) {
					taskHtml.prst = data.list;
				});
			}
			// zq：根据合同ID查询收款节点的内容
			function selectRenoByContId() {
				var cont_id = sessionStorage.getItem('conId');
				services.selectRenoByContId({
					cont_id : cont_id
				}).success(function(data) {
					taskHtml.reno = data.list;
				});
			}

			// 合同，收款节点，工期阶段的详情
			taskHtml.showContInfo = function() {
				$('#contInformation').show();
				$('#contShow').hide();
				$('#contHide').show();
			}
			taskHtml.hideContInfo = function() {

				$('#contInformation').hide();
				$('#contShow').show();
				$('#contHide').hide();
			}
			taskHtml.showPrstInfo = function() {
				$('#prstInformation').show();
				$('#prstShow').hide();
				$('#prstHide').show();
			}
			taskHtml.hidePrstInfo = function() {

				$('#prstInformation').hide();
				$('#prstShow').show();
				$('#prstHide').hide();
			}
			taskHtml.showRenoInfo = function() {
				$('#renoInformation').show();
				$('#renoShow').hide();
				$('#renoHide').show();
			}
			taskHtml.hideRenoInfo = function() {

				$('#renoInformation').hide();
				$('#renoShow').show();
				$('#renoHide').hide();
			}
			// 初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/newTaskList') == 0) {// 如果是接受的新任务列表页
					tState = 0;
					services.getTaskList({
						taskState : 0,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
					// 点击创建任务时弹出模态框
					/*
					 * taskHtml.newTask = function(obj) { var conId =
					 * this.con.cont_id;
					 * services.getAllUsers().success(function(data){
					 * contract.users = data; console.log(conId);
					 * sessionStorage.setItem("contractId", conId); });
					 * $(".overlayer").fadeIn(200); $(".tip").fadeIn(200);
					 * return false; };
					 * 
					 * $(".tiptop a").click(function() {
					 * sessionStorage.setItem("contractId", "");
					 * $(".overlayer").fadeOut(200); $(".tip").fadeOut(200); });
					 * 
					 * $(".sure").click(function() { var conId =
					 * sessionStorage.getItem("contractId");
					 * if(contract.task.task_type=="1"){ var task1 =
					 * JSON.stringify(contract.task1); console.log(task1);
					 * services.addTask({ task : task1, taskType : "1",//1代表文书任务
					 * conId : conId }).success(function(data) {
					 * alert("添加文书任务成功！"); }); }else
					 * if(contract.task.task_type=="0"){ var task2 =
					 * JSON.stringify(contract.task2); console.log(task2);
					 * services.addTask({ task : task2, taskType :
					 * "2",//2代表执行管控任务 conId : conId }).success(function(data) {
					 * alert("添加执行管控任务成功！"); }); } $(".overlayer").fadeOut(100);
					 * $(".tip").fadeOut(100); });
					 * 
					 * $(".cancel").click(function() {
					 * sessionStorage.setItem("contractId", "");
					 * $(".overlayer").fadeOut(100); $(".tip").fadeOut(100); });
					 */

				} else if ($location.path().indexOf('/unfinishTaskList') == 0) {// 未完成任务
					services.getTaskList({
						taskState : 1,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
				} else if ($location.path().indexOf('/finishTaskList') == 0) {// 已完成任务
					services.getTaskList({
						taskState : 2,
						page : 1,
						sendOrReceive : 1
					}).success(function(data) {
						taskHtml.tasks = data.list;
						pageTurn(0, data.totalPage, 1)
					});
				} else if ($location.path().indexOf('/contractAdd') == 0) {
					// 这里先获取人员列表
					/*
					 * services.getAllUsers().success(function(data){
					 * contract.users = data;
					 * sessionStorage.setItem("contractId", ""); }); var $select =
					 * $("select"); for (var i = 0; i < $select.length; i++) {
					 * $select[i].options[0].selected = true; }
					 * $('select').prop('selectedIndex', 1);
					 */
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
			initState();
			// 验证日期输入格式
			/*
			 * var $dateFormat = $(".dateFormat"); var dateRegexp =
			 * /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/; $(".dateFormat").blur(function() {
			 * if (!dateRegexp.test(this.value)) {
			 * $(this).parent().children("span").css('display', 'inline'); } });
			 * $(".dateFormat").click(function() {
			 * $(this).parent().children("span").css('display', 'none'); });
			 */
		} ]);
//小数过滤器
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
// 合同状态过滤器
/*
 * app.filter('conState', function() { return function(input) { var state = "";
 * 
 * switch(input){ case "0":state="在建"; break; case "1":state="竣工"; break; case
 * "2":state="停建"; break; }
 * 
 * if (input == "0") state = "在建"; else if (input == "1") state = "竣工"; else if
 * (input == "2") state = "停建"; return state; } }); // 自定义表单验证日期格式
 * app.directive("dateFormat", function() { return { restrict : 'A', require :
 * 'ngModel', scope : true, link : function(scope, elem, attrs, controller) {
 * var dateRegexp = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/; // Model变化时执行 // 初始化指令时BU执行
 * scope.$watch(attrs.ngModel, function(val) { if (!val) { return; } if
 * (!dateRegexp.test(val)) { controller.$setValidity('dateformat', false); }
 * else { controller.$setValidity('dateformat', true); } }); } } });
 */
/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */