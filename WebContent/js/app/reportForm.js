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
<<<<<<< HEAD
	}).when('/projectList', {
		templateUrl : '/CIMS/jsp/reportForm/projectList.html',
		controller : 'ReportController'
=======
>>>>>>> f18b41a8e0e1003b483275e365270fd6ad064cf0
	})
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
<<<<<<< HEAD
	// zq从设计部取出项目经理人选zq2016-11-17
	services.selectUsersFromDesign = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUsersFromDesign.do',
			data : data
		});
	};
	// 根据限制条件查询项目统计表zq2016-11-17
	services.selectProjectListBylimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectProjectListBylimits.do',
=======
	
	services.getRemoAnalyzeDataByYear = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectComoRemoAnalyse.do',
>>>>>>> f18b41a8e0e1003b483275e365270fd6ad064cf0
			data : data
		});
	};
	return services;
} ]);

<<<<<<< HEAD
app.controller('ReportController', [
		'$scope',
		'services',
		'$location',
		function($scope, services, $location) {
			var reportForm = $scope;
			// zq2016-11-17
			var reportPage = 1;
			// zq查询条件实体2016-11-17
			var proListLimits = {};
			// zq设定查询条件初始值2016-11-17
			reportForm.limit = {
				contType : "-1",
				proStage : "",
				contStatus : "",
				province : "",
				startDate : "",
				endDate : "",
				userId : ""
			};
			// zq点击查询list2016-11-17
			reportForm.selectProjectListBylimits = function() {
				var errorText = $("#errorText").css("display");
				if (errorText == "inline") {
					alert("时间格式错误！");
					return false;
				}
				if (reportForm.limit.startDate != "") {
					if (reportForm.limit.endDate == "") {
						alert("请输入截止时间！");
						return false;
					} else {
						var date1 = new Date(reportForm.limit.startDate);
						var date2 = new Date(reportForm.limit.endDate);
						if (date1.getTime() > date2.getTime()) {
							alert("截止时间不能大于起始时间！");
							return false;
						}
					}
				}
				proListLimits = JSON.stringify(reportForm.limit);
				services.selectProjectListBylimits({
					limit : proListLimits,
					page : reportPage
				}).success(function(data) {
					reportForm.prStForms = data.list;// prstForms查询出来的列表（ProjectStatisticForm）
					pageTurn(data.totalPage, 1);
				});

			}
			// zq换页查找函数2016-11-17
			function findProjectListBylimits(p) {
				services.selectProjectListBylimits({
					limit : proListLimits,
					page : p
				}).success(function(data) {
					reportForm.prStForms = data.list;// prstForms查询出来的列表（ProjectStatisticForm）
				});
			}
			// zq：从设计部查找人员2016-11-17
			function selectUsersFromDesign() {
				services.selectUsersFromDesign({}).success(function(data) {
					reportForm.userDepts = data.list;
				});
			}
			// zq换页2016-11-17
			function pageTurn(totalPage, page) {
				var $pages = $(".tcdPageCode");
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							reportPage = p;
							findProjectListBylimits(p);
						}
					});
				}
			}
			// 初始化
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/remoAnalyzeList') == 0) {
				} else if ($location.path().indexOf('/projectList') == 0) {
					selectUsersFromDesign();
				}
			}
			initData();
			// zq控制年月2016-11-17
			var $dateFormat = $(".dateFormatForYM");
			var dateRegexp = /^[0-9]{4}-[0-9]{1,2}$/;
			$(".dateFormatForYM").blur(
					function() {
						if (this.value.trim() != "") {
							if (!dateRegexp.test(this.value)) {
								$(this).parent().children("span").css(
										'display', 'inline');
							} else {
								var month = parseInt(this.value.split("-")[1]);
								if (month > 12) {
									$(this).parent().children("span").css(
											'display', 'inline');
								}
							}
						}

					});
			$(".dateFormatForYM").click(function() {
				$(this).parent().children("span").css('display', 'none');
			});
		} ]);
// 截取任务内容zq2016-11-17
app.filter('cutString', function() {
	return function(input) {
		var content = "";
		if (input != "") {
			var shortInput = input.substr(0, 10);
			content = shortInput + "……";
		}

		return content;
	}
});
// 小数过滤器zq2016-11-17
app.filter('numberFloat', function() {
	var money = 0.00;
	return function(input) {
		if (input) {
			money = parseFloat(input).toFixed(2);
		}
		return money;
	}
});
=======
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
>>>>>>> f18b41a8e0e1003b483275e365270fd6ad064cf0

