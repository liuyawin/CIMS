var app = angular
		.module(
				'department',
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
	$routeProvider.when('/departmentList', {
		templateUrl : '/CIMS/jsp/userManagement/departInformation/departList.html',
		controller : 'AdminController'
	}).when('/departmentAdd', {
		templateUrl : '/CIMS/jsp/userManagement/departInformation/departAdd.html',
		controller : 'AdminController'
	}).when('/bulkImportStaff', {
		templateUrl : '/CIMS/jsp/userManagement/departInformation/bulkImportStaff.html',
		controller : 'AdminController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getDepartmentListByPage = function(data) {
		console.log("发送请求根据页数获取部门信息");
		return $http({
			method : 'post',
			url : baseUrl + 'department/getDepartmentListByPage.do',
			data : data
		});
	};

	services.getAllDepartmentList = function(data) {
		console.log("发送请求获取所有部门信息");
		return $http({
			method : 'post',
			url : baseUrl + 'department/getAllDepartmentList.do',
			data : data
		});
	};
	
	services.addDepart = function(data) {
		console.log("发送请求增加部门信息");
		return $http({
			method : 'post',
			url : baseUrl + 'department/addDepart.do',
			data : data
		});
	};
	services.deleteDepart = function(data) {
		console.log("发送请求删除部门信息");
		return $http({
			method : 'post',
			url : baseUrl + 'department/deleteDepart.do',
			data : data
		});
	};

	services.selectDeptByName = function(data) {
		console.log("按名字查找部门");
		return $http({
			method : 'post',
			url : baseUrl + 'department/selectDeptByName.do',
			data : data
		});
	};

	return services;
} ]);

app.controller('AdminController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {

			var admin = $scope;
			// 换页
			function pageTurn(totalPage, page) {

				var $pages = $(".tcdPageCode");
				console.log($pages.length);
				if ($pages.length != 0) {
					$(".tcdPageCode").createPage({
						pageCount : totalPage,
						current : page,
						backFn : function(p) {
							getDepartmentListByPage(p)
						}
					});
				}
			}
			// 根据页数获取部门列表
			function getDepartmentListByPage(page) {
				services.getDepartmentListByPage({
					page : page
				}).success(function(data) {
					admin.departs = data.list;
				});
			}
			// 获取部门列表
			function getAllDepartmentList() {
				services.getAllDepartmentList({}).success(function(data) {
					console.log("获取部门列表成功！");
					admin.departs = data;
				});
			}
           //添加部门
			admin.department={}; 
			admin.addDepart = function() {

				services.addDepart({
					dept_name : $scope.department.dept_name,
					dept_pid : $("#departSelect").val(),
					dept_remark : $scope.department.dept_remark
					}).success(function(data) {
					admin.result = data;
					if (data == "true") {
						console.log("添加部门列表成功！");
						
					} else {
						console.log("添加部门列表失败！");
					}
				});
			}
			 //删除部门
			admin.deleteDepart = function(dept_id) {
				$(".tip").fadeIn(200);
				$(".sure").click(function() {
					services.deleteDepart({
						deptId : dept_id
					}).success(function(data) {

						admin.result = data;
						if (data == "true") {
							console.log("删除部门列表成功！");
							$("#" + dept_id).hide();
						} else {
							console.log("删除部门列表失败！");
						}
					});
					$(".tip").fadeOut(100);
				});
				$(".cancel").click(function() {
					$(".tip").fadeOut(100);
				});
			}
           //根据输入筛选部门
			admin.selectDeptByName = function() {
				services.selectConByName({
					departName : $("#dName").val()
				}).success(function(data) {
					admin.departs = data;
				});
			};
             //初始化
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/departmentList') == 0) {
					services.getDepartmentListByPage({
						page : 1
					}).success(function(data) {
						admin.departs = data.list;
						pageTurn(data.totalPage, 1)
					});					
				} else if ($location.path().indexOf('/departmentAdd') == 0) {
					console.log("初始化部门新增信息");
					getAllDepartmentList();

				} 
			}

			initData();
		} ]);

/*
 * app.directive('minLength', function () { return { restrict: 'A', require:
 * 'ngModel', scope: { 'min': '@' }, link: function (scope, ele, attrs,
 * controller) { scope.$watch(attrs.ngModel, function (val) { if (!val) {
 * return; } console.log(val); if (val.length <= scope.min) {
 * controller.$setValidity('minlength', false); } else {
 * controller.$setValidity('minlength', true); } }); } } });
 */