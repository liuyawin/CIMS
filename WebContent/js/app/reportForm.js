var app = angular
		.module(
				'reportForm',
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
// 获取权限列表
var permissionList;
angular.element(document).ready(function() {
	console.log("获取权限列表！");
	$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data; // 
		console.log("身份是：" + permissionList);
		angular.bootstrap($("#reportForm"), [ 'reportForm' ]); // 手动加载angular模块
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
	$routeProvider.when('/remoAnalyzeList', {
		templateUrl : '/CIMS/jsp/reportForm/remoAnalyzeList.html',
		controller : 'ReportController'
	})
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	
	services.getRemoAnalyzeDataByYear = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectComoRemoAnalyse.do',
			data : data
		});
	};
	return services;
} ]);

app
		.controller(
				'ReportController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							// zq合同
							var reportForm = $scope;
							
							reportForm.getTableDate = function(){
								var beginYear = $('#begin-year').val();
								var endYear = $('#end-year').val();
								services.getRemoAnalyzeDataByYear({
									beginYear:beginYear,
									endYear  :endYear
								}).success(function(data){
									console.log(data);
									reportForm.comoCompareRemo = data.reportForm;
								});
								
							}
							// zq初始化
							function initData() {
								console.log("初始化页面信息");
								if ($location.path().indexOf('/remoAnalyzeList') == 0) {
									var date = new Date();
									var year = date.getFullYear();
									$('#begin-year').val(year);
									$('#end-year').val(year);
								}
							}
							initData();
							
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

						} ]);

