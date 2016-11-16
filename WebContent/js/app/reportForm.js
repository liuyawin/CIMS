var app = angular
		.module(
				'reportForm',
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



// 路由配置
app
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									'/remoAnalyzeList',
									{
										templateUrl : '/CIMS/jsp/reportForm/remoAnalyzeList.html',
										controller : 'ReportFormController'
									});
				} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getRemoAnalyzeList= function(data) {
		/* console.log("发送请求获取信息"); */
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/getRemoAnalyzeList.do',
			data : data
		});
	};

	return services;
} ]);

app
		.controller(
				'ReportFormController',
				[
						'$scope',
						'services',
						'$location',
						'FileUploader',
						function($scope, services, $location, FileUploader) {
							// 合同
							var reportForm = $scope;

							// 初始化页面信息
							function initData() {
								if ($location.path().indexOf('/remoAnalyzeList') == 0) { 

								} else if ($location.path().indexOf(
										'/paymentPlanList') == 0) {
									
								} else if ($location.path().indexOf(
										'/projectList') == 0) {
									
								} else if ($location.path().indexOf(
										'/unGetContList') == 0) { 
									
								} 
							}

							initData();
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
							var numberRegexp = /^[1-9]\d*(\.\d+)?$/;
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

