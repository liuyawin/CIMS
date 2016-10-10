var app = angular
		.module(
				'alarm',
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
									'/alarmDetail',
									{
										templateUrl : '/CIMS/jsp/userManagement/alarmInformation/alarmDetail.html',
										controller : 'AlarmController'
									})
							.when(
									'/newAlarmList',
									{
										templateUrl : '/CIMS/jsp/userManagement/alarmInformation/alarmList.html',
										controller : 'AlarmController'
									})
							.when(
									'/doneAlarmList',
									{
										templateUrl : '/CIMS/jsp/userManagement/alarmInformation/alarmList.html',
										controller : 'AlarmController'
									})
							.when(
									'/alarmSet',
									{
										templateUrl : '/CIMS/jsp/userManagement/alarmInformation/alarmSet.html',
										controller : 'AlarmController'
									})

				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	// zq根据状态查找报警列表
	services.selectAlarmByState = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarm/selectAlarmByState.do',
			data : data
		});
	};

	services.selectAlarmById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarm/selectAlarmById.do',
			data : data
		});
	};

	services.selectAllAlarmLevel = function() {
		return $http({
			method : 'post',
			url : baseUrl + 'alarmLevel/selectAllAlarmLevel.do',
		});
	};
	services.getAlarmLevelByID = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarmLevel/getAlarmLevelByID.do',
			data : data
		});
	}

	services.alarmLevelAdd = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarmLevel/alarmLevelAdd.do',
			data : data
		});
	};

	services.deleteAlarmLevel = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarmLevel/deleteAlarmLevel.do',
			data : data
		});
	};
	// 获取所有角色
	services.getAllRoles = function() {
		return $http({
			method : 'post',
			url : baseUrl + 'role/getAllRoleList.do',
		});
	};
	return services;
} ]);

app.controller('AlarmController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			// 合同
			var alarm = $scope;
			var isRemove;
			// zq查看合同ID，并记入sessione
			alarm.getContId = function(contId) {
				sessionStorage.setItem('contId', contId);
			};
			// 获取报警ID
			alarm.getAlarmId = function(alarId) {
				sessionStorage.setItem('alarId', alarId);
			};
			// zq根据状态查找报警列表
			function selectAlarmByState(page, isRemove) {
				services.selectAlarmByState({
					isRemove : isRemove,
					page : page
				}).success(function(data) {
					alarm.alarms = data.list;

				});
			}
			// zq根据id 查找报警详情
			function selectAlarmById() {
				var alarId = sessionStorage.getItem('alarId');
				services.selectAlarmById({
					alarId : alarId
				}).success(function(data) {
					alarm.alarm = data.alarm;

				});
			}

			// bao根据状态查找报警列表
			function selectAllAlarmLevel() {
				services.selectAllAlarmLevel({}).success(function(data) {
					alarm.alarmLevels = data;
				});
			}

			// zq按查询内容获取任务列表的换页
			function pageTurnByContent(isRemove, totalPage, page) {

				var $pages = $(".tcdPageCode");
				console.log($pages.length);
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							selectAlarmByState(p, isRemove);
						}
					});
				}
			}

			// 添加alarmLevel
			alarm.addAlarm = function() {
				var alarmLevelFormData = JSON.stringify(alarm.alarmLevel);

				services.alarmLevelAdd({
					alarmLevel : alarmLevelFormData
				}).success(function(data) {
					sessionStorage.setItem("alle_id", data);
					alert("报警设置成功！");
				});
			};
			// 删除alarmLevel
			alarm.deleteAlarmLevel = function(obj) {
				var alleID = this.level.alle_id;
				var msg = "确认删除该设置？";
				if (confirm(msg) == true) {
					services.deleteAlarmLevel({
						alle_id : alleID
					}).success(function(data) {
						alert("删除成功！");
						selectAllAlarmLevel();
					});
				} else {
					return false;
				}
			}
			// 点击新建按钮事件
			alarm.addNewAlarmLevel = function() {
				services.getAllRoles().success(function(data) {
					alarm.roles = data;
					console.log(data);
				});
				$(".overlayer").fadeIn(200);
				$(".tip").fadeIn(200);
				$("#addAlarm-form").slideDown(200);
				$("#editAlarm-form").hide();
			};
			// 点击修改时弹出模态框
			alarm.edit = function(obj) {
				var alleId = this.level.alle_id;
				services.getAllRoles().success(function(data) {
					alarm.roles2 = data;
					console.log(data);
					services.getAlarmLevelByID({
						alle_id : alleId
					}).success(function(data) {
						alarm.editLevel = data.alarmLevel;
						console.log(alleId);
						sessionStorage.setItem("alleId", alleId);
					});
				});

				$(".overlayer").fadeIn(200);
				$(".tip").fadeIn(200);
				$("#addAlarm-form").hide();
				$("#editAlarm-form").slideDown(200);
				return false;
			};
			// 修改报警设置
			$(".sure2").click(function() {
				var alarmLl = JSON.stringify(alarm.editLevel);
				console.log(alarmLl);
				services.alarmLevelAdd({
					alle_rank : alarm.editLevel.alle_rank,
					alle_days : alarm.editLevel.alle_days,
					role_id : alarm.editLevel.role.role_id,
					alle_id : alarm.editLevel.alle_id
				}).success(function(data) {
					alert("修改成功！");
					selectAllAlarmLevel();
				});

				$(".overlayer").fadeOut(100);
				$(".tip").fadeOut(100);
			});
			// 隐藏模态框
			$(".tiptop a").click(function() {
				$(".overlayer").fadeOut(200);
				$(".tip").fadeOut(200);
			});

			$(".cancel").click(function() {
				// sessionStorage.setItem("contractId", "");

				$(".overlayer").fadeOut(100);
				$(".tip").fadeOut(100);
			});
			// 添加报警设置
			$(".sure1").click(function() {
				var conId = sessionStorage.getItem("contractId");

				var alarmLl = JSON.stringify(alarm.alarmLevel);
				console.log(alarmLl);
				services.alarmLevelAdd({
					alle_rank : alarm.alarmLevel.alle_rank,
					alle_days : alarm.alarmLevel.alle_days,
					role_id : alarm.alarmLevel.role_id
				}).success(function(data) {
					alert("新建成功！");
					selectAllAlarmLevel();
				});

				$(".overlayer").fadeOut(100);
				$(".tip").fadeOut(100);
			});
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/newAlarmList') == 0) {
					isRemove = 0;
					services.selectAlarmByState({
						isRemove : isRemove,
						page : 1
					}).success(function(data) {
						alarm.alarms = data.list;
						pageTurnByContent(isRemove, data.totalPage, 1);

					});
				} else if ($location.path().indexOf('/doneAlarmList') == 0) {
					isRemove = 1;
					services.selectAlarmByState({
						isRemove : isRemove,
						page : 1
					}).success(function(data) {
						alarm.alarms = data.list;
						pageTurnByContent(isRemove, data.totalPage, 1);
					});
				} else if ($location.path().indexOf('/alarmDetail') == 0) {

					selectAlarmById();
				} else if ($location.path().indexOf('/alarmSet') == 0) {

					selectAllAlarmLevel();

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
// 合同重要等级过滤器
app.filter('alleRank', function() {
	return function(input) {
		var state = "";
		if (input == "0")
			state = "重要";
		else if (input == "1")
			state = "一般";
		return state;
	}
});
// 小数过滤器
app.filter('receFloat', function() {
	return function(input) {
		var money = parseFloat(input).toFixed(2);
		return money;
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
// 截取任务内容
app.filter('alarmState', function() {
	return function(input) {
		var state = "";
		if (input == '0') {
			state = "待解除";
		}
		if (input == '1') {
			state = "已解除";
		}

		return state;
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