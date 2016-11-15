var alarmApp = angular
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
alarmApp.directive('hasPermission', function($timeout) {
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

alarmApp.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
alarmApp
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
										templateUrl : '/CIMS/jsp/systemManagement/alarmSet.html',
										controller : 'AlarmController'
									})

				} ]);
alarmApp.constant('baseUrl', '/CIMS/');
alarmApp.factory('systemServices', [ '$http', 'baseUrl',
		function($http, baseUrl) {
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

alarmApp.controller('AlarmController', [
		'$scope',
		'systemServices',
		'$location',
		function($scope, services, $location) {
			// 合同
			var alarm = $scope;
			var isRemove;
			// zq查看合同ID，并记入sessione
			alarm.getContId = function(contId) {
				sessionStorage.setItem('conId', contId);
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
					alarm.alarmLevels = data.list;
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
				alarm.alarmLevel = {
					alle_days : 0,
					alle_rank : 1,
					role_id : null
				};
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
			alarm.editAlarmLevel=function() {
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
			}
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
			alarm.addAlarmLevel = function() {
				var conId = sessionStorage.getItem("contractId");
				var alarmLl = JSON.stringify(alarm.alarmLevel);
				services.alarmLevelAdd({
					alle_rank : alarm.alarmLevel.alle_rank,
					alle_days : alarm.alarmLevel.alle_days,
					role_id : alarm.alarmLevel.role_id
				}).success(function(data) {
					alert("新建成功！");
					selectAllAlarmLevel();
					alarm.alarmLevel = "";
				});

				$(".overlayer").fadeOut(100);
				$(".tip").fadeOut(100);
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
						sessionStorage.setItem("userRole", value);
					}

				}
			}
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				$("#user").hide();
				$("#alarm").show();
				if ($location.path().indexOf('/alarmSet') == 0) {

					selectAllAlarmLevel();

				}
			}
			function dateformat() {
				var $dateFormat = $(".dateFormat");
				var dateRegexp = /^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/;
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
			findRoleFromCookie();
			dateformat();// 格式化日期格式

		} ]);
// 合同重要等级过滤器
alarmApp.filter('alleRank', function() {
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
alarmApp.filter('receFloat', function() {
	return function(input) {
		var money = parseFloat(input).toFixed(2);
		return money;
	}
});

// 时间的格式化的判断
alarmApp.filter('dateType', function() {
	return function(input) {

		var type = "";
		if (input != null) {
			type = new Date(input).toLocaleDateString().replace(/\//g, '-');
		}

		return type;
	}
});

// 自定义表单验证日期格式
alarmApp.directive("dateFormat", function() {
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
alarmApp.filter('cutString', function() {
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
alarmApp.filter('alarmState', function() {
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