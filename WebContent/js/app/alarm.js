/**
 * 张姣娜新建
 */
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
//获取权限列表
var permissionList; 
angular.element(document).ready(function() {
console.log("获取权限列表！"); 
$.get('/CIMS/login/getUserPermission.do', function(data) { 
	  permissionList = data; // 
	  console.log("身份是：" + permissionList);
	  angular.bootstrap($("#ng-section"), ['alarm']); //手动加载angular模块
	  }); 
});

app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/alarmDetail', {
		templateUrl : '/CIMS/jsp/alarmInformation/alarmDetail.html',
		controller : 'AlarmController'
	}).when('/debtAlarmList', {
		templateUrl : '/CIMS/jsp/alarmInformation/alarmList.html',
		controller : 'AlarmController'
	}).when('/overdueAlarmList', {
		templateUrl : '/CIMS/jsp/alarmInformation/alarmList.html',
		controller : 'AlarmController'
	}).when('/taskAlarmList', {
		templateUrl : '/CIMS/jsp/alarmInformation/alarmList.html',
		controller : 'AlarmController'
	}).when('/contractDetail', {
		templateUrl : '/CIMS/jsp/contractInformation/contractDetail.html',
		controller : 'ContractController'
	})

} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	// 张姣娜：根据报警类型查询报警列表
	services.selectAlarmByType = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarm/selectAlarmByType.do',
			data : data
		});
	};

	services.selectAlarmById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'alarm/selectAlarmById.do',
			data : data
		});
	}
	return services;
} ]);

app.controller('AlarmController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			// 合同
			var alarm = $scope;
			var alarmType;
			var alarmContent = "";
			// zq查看合同ID，并记入sessione
			alarm.getContId = function(contId) {
				sessionStorage.setItem('conId', contId);
			};
			// 获取报警ID
			alarm.getAlarmId = function(alarId) {
				sessionStorage.setItem('alarId', alarId);
			};
			// 根据内容进行查找
			alarm.selectAlarmByContent = function() {
				alarmContent = alarm.alarmContent;
				services.selectAlarmByType({
					alarmType : alarmType,
					page : 1,
					searchKey : alarmContent
				}).success(function(data) {
					alarm.alarms = data.list;
					pageTurnByContent(alarmType, data.totalPage, 1);
				});
			}
			// zq根据类型查找报警列表
			function selectAlarmByType(page, alarmType) {
				services.selectAlarmByType({
					alarmType : alarmType,
					page : page,
					searchKey : alarmContent
				}).success(function(data) {
					alarm.alarms = data.list;
				});
			}
			// zjn根据contId查找合同详情
			function contDetailByContId() {
				var contId = sessionStorage.getItem('contId');
				services.contDetailByContId({
					contId : contId
				}).success(function(data) {
					alarm.cont = data.cont;
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
			// zq按查询内容获取任务列表的换页
			function pageTurnByContent(alarmType, totalPage, page) {

				var $pages = $(".tcdPageCode");
				console.log($pages.length);
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							selectAlarmByType(p, alarmType);
						}
					});
				}
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
						sessionStorage.setItem("userRole",
								value);
					}

				}
			}
			// zq初始化页面信息
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/debtAlarmList') == 0) {// 获取收款超时报警列表
					alarmType = "2,3";
					services.selectAlarmByType({
						alarmType : alarmType,
						page : 1,
						searchKey : alarmContent
					}).success(function(data) {
						alarm.alarms = data.list;
						pageTurnByContent(alarmType, data.totalPage, 1);

					});
				} else if ($location.path().indexOf('/overdueAlarmList') == 0) {// 获取获取工程逾期报警列表
					alarmType = "4,5";
					services.selectAlarmByType({
						alarmType : alarmType,
						page : 1,
						searchKey : alarmContent
					}).success(function(data) {
						alarm.alarms = data.list;

						pageTurnByContent(alarmType, data.totalPage, 1);
					});
				} else if ($location.path().indexOf('/taskAlarmList') == 0) {// 获取任务超时报警列表
					alarmType = "1";
					services.selectAlarmByType({
						alarmType : alarmType,
						page : 1,
						searchKey : alarmContent
					}).success(function(data) {
						alarm.alarms = data.list;
						pageTurnByContent(alarmType, data.totalPage, 1);
					});
				} else if ($location.path().indexOf('/alarmDetail') == 0) {
					selectAlarmById();
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