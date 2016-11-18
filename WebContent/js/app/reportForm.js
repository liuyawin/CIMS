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
	}).when('/projectList', {
		templateUrl : '/CIMS/jsp/reportForm/projectList.html',
		controller : 'ReportController'
	}).when('/unGetContList', {
		templateUrl : '/CIMS/jsp/reportForm/unGetContList.html',
		controller : 'ReportController'
	})
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
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
			data : data
		});
	};
	// 根据年份获取合同额到款分析表的数据
	services.getRemoAnalyzeDataByYear = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectComoRemoAnalyse.do',
			data : data
		});
	};
	// zq获取所有用户2016-11-18
	services.getAllUsers = function() {
		return $http({
			method : 'post',
			url : baseUrl + 'user/getAllUserList.do',
		});
	};
	// zq根据限制条件查询未返回项目统计表2016-11-18
	services.selectUnGetContListBylimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectUnGetContListBylimits.do',
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
							var reportForm = $scope;
							// zq2016-11-17
							var reportPage = 1;
							// zq查询条件实体2016-11-17
							var proListLimits = {};
							var unGetListLimits = {};
							//zq显示没查询到数据提醒
							var listShow = true;
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

							// zq设定查询未返回合同查询条件初始值2016-11-18
							reportForm.unGetlimit = {
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
										var date1 = new Date(
												reportForm.limit.startDate);
										var date2 = new Date(
												reportForm.limit.endDate);
										if (date1.getTime() > date2.getTime()) {
											alert("截止时间不能大于起始时间！");
											return false;
										}
									}
								}
								proListLimits = JSON
										.stringify(reportForm.limit);
								services.selectProjectListBylimits({
									limit : proListLimits,
									page : 1
								}).success(function(data) {
									reportForm.prStForms = data.list;// prstForms查询出来的列表（ProjectStatisticForm）
									pageTurn(data.totalPage, 1);
									if (data.list.length == 0) {
										listShow = false;
									} else {
										listShow = true;
									}
								});
							}
							// zq换页查找函数2016-11-17
							function findProjectListBylimits(p) {
								services.selectProjectListBylimits({
									limit : proListLimits,
									page : p
								}).success(function(data) {
									reportForm.prStForms = data.list;// prstForms查询出来的列表（ProjectStatisticForm）
									if (data.list.length == 0) {
										listShow = false;
									} else {
										listShow = true;
									}
								});
							}
							// zq：从设计部查找人员2016-11-17
							function selectUsersFromDesign() {
								services.selectUsersFromDesign({}).success(
										function(data) {
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
											findProjectListBylimits(p);
										}
									});
								}
							}
							/*
							 * zq 2016-11-18未返回合同表
							 */
							// zq查找所有用户2016-11-18
							function selectAllUsers() {
								services.getAllUsers().success(function(data) {
									reportForm.users = data;
								});
							}
							// zq点击查询list2016-11-17
							reportForm.selectUnGetContListBylimits = function() {
								var errorText = $("#errorText").css("display");
								if (errorText == "inline") {
									alert("时间格式错误！");
									return false;
								}
								if (reportForm.unGetlimit.startDate != "") {
									if (reportForm.unGetlimit.endDate == "") {
										alert("请输入截止时间！");
										return false;
									} else {
										var date1 = new Date(
												reportForm.unGetlimit.startDate);
										var date2 = new Date(
												reportForm.unGetlimit.endDate);
										if (date1.getTime() > date2.getTime()) {
											alert("截止时间不能大于起始时间！");
											return false;
										}
									}
								}
								unGetListLimits = JSON
										.stringify(reportForm.unGetlimit);
								console.log(unGetListLimits);
								/*
								 * services.selectUnGetContListBylimits({ limit :
								 * unGetListLimits, page : 1
								 * }).success(function(data) {
								 * reportForm.unGetContForms = data.list;//
								 * prstForms查询出来的列表（ProjectStatisticForm）
								 * unGetPageTurn(data.totalPage, 1); });
								 */

							}
							// zq换页查找函数2016-11-18
							function findUnGetContListBylimits(p) {
								services.selectUnGetContListBylimits({
									limit : unGetListLimits,
									page : p
								}).success(function(data) {
									reportForm.unGetContForms = data.list;// prstForms查询出来的列表（ProjectStatisticForm）
								});
							}
							// zq换页2016-11-18
							function unGetPageTurn(totalPage, page) {
								var $pages = $(".tcdPageCode");
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											findUnGetContListBylimits(p);
										}
									});
								}
							}
							/*
							 * zq 2016-11-18未返回合同表结束
							 */
							// liu
							reportForm.getTableDate = function() {
								var beginYear = $('#begin-year').val();
								var endYear = $('#end-year').val();
								if (!(/^\d{4}$/.test(beginYear) && /^\d{4}$/
										.test(endYear))) {
									alert("输入格式错误");
									return false;
								}
								services
										.getRemoAnalyzeDataByYear({
											beginYear : beginYear,
											endYear : endYear
										})
										.success(
												function(data) {
													console.log("直接打印从后台获取的数据："
															+ data);
													reportForm.comoCompareRemo = data.reportForm;
													reportForm.table1Show = false;
													if (reportForm.comoCompareRemo) {
														reportForm.table1Show = true;
													}
													var chart1Data = [
															[ 'Firefox', 45.0 ],
															[ 'IE', 26.8 ],
															{
																name : 'Chrome',
																y : 12.8,
																sliced : true,
																selected : true
															},
															[ 'Safari', 8.5 ],
															[ 'Opera', 6.2 ],
															[ 'Others', 0.7 ] ];

													Highcharts
															.wrap(
																	Highcharts.Chart.prototype,
																	'getSVG',
																	function(
																			proceed) {
																		return proceed
																				.call(
																						this)
																				.replace(
																						/(fill|stroke)="rgba([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)"/g,
																						'$1="rgb($2)" $1-opacity="$3"');
																	});
													if (chart1Data) {
														var chart1 = new Chart(
																{
																	elementId : "#pieChart1",
																	title : "2014年自营项目新签合同额分析图",
																	name : "浏览器",
																	data : chart1Data
																});
														chart1.init();
													}

												});

							}
							// 初始化
							function initData() {
								console.log("初始化页面信息");
								if ($location.path()
										.indexOf('/remoAnalyzeList') == 0) {
									var date = new Date();
									var year = date.getFullYear();
									$('#begin-year').val(year);
									$('#end-year').val(year);
								} else if ($location.path().indexOf(
										'/projectList') == 0) {
									selectUsersFromDesign();
								} else if ($location.path().indexOf(
										'/unGetContList') == 0) {
									selectAllUsers();
								}
							}
							initData();
							// zq控制年月2016-11-17
							var $dateFormat = $(".dateFormatForYM");
							var dateRegexpForYM = /^[0-9]{4}-[0-9]{1,2}$/;
							$(".dateFormatForYM").blur(
									function() {
										if (this.value.trim() != "") {
											if (!dateRegexpForYM
													.test(this.value)) {
												$(this).parent().children(
														"span").css('display',
														'inline');
											} else {
												var month = parseInt(this.value
														.split("-")[1]);
												if (month > 12) {
													$(this).parent().children(
															"span")
															.css('display',
																	'inline');
												}
											}
										}
									});
							$(".dateFormatForYM").click(
									function() {
										$(this).parent().children("span").css(
												'display', 'none');
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
