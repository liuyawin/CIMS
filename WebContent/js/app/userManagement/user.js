var app = angular
		.module(
				'user',
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
	$routeProvider.when('/roleAdd', {
		templateUrl : '/CIMS/jsp/userManagement/roleAdd.html',
		controller : 'userController'
	}).when('/roleList', {
		templateUrl : '/CIMS/jsp/userManagement/roleList.html',
		controller : 'userController'
	}).when('/userList', {
		templateUrl : '/CIMS/jsp/userManagement/userInformation/userList.html',
		controller : 'userController'
	}).when('/userAdd', {
		templateUrl : '/CIMS/jsp/userManagement/userInformation/userAdd.html',
		controller : 'userController'
	}).when('/alarm', {
		templateUrl : '/CIMS/jsp/userManagement/',
		controller : 'userController'
	}).when('/journal', {
		templateUrl : '/CIMS/jsp/userManagement/',
		controller : 'userController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getUserListByPage = function(data) {
		console.log("发送请求根据页数获取用户信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/getUserListByPage.do',
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

	services.addUser = function(data) {
		console.log("发送请求增加用户信息");

		return $http({
			method : 'post',
			url : baseUrl + 'user/addUser.do',
			data : data
		});
	};
	services.deleteUser = function(data) {
		console.log("发送请求删除用户信息");
		return $http({
			method : 'post',
			url : baseUrl + 'user/deleteUser.do',
			data : data
		});
	};

	services.selectUserByName = function(data) {
		console.log("按名字查找用户");
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUserByName.do',
			data : data
		});
	};

	services.getRoleListByPage = function(data) {
		console.log("发送请求根据页数获取角色信息");
		return $http({
			method : 'post',
			url : baseUrl + 'role/getRoleListByPage.do',
			data : data
		});
	};

	services.deleteRole = function(data) {
		console.log("发送请求删除角色信息");
		return $http({
			method : 'post',
			url : baseUrl + 'role/deleteRole.do',
			data : data
		});
	};
	services.getAllRoleList = function(data) {
		console.log("发送请求获取所有角色信息");
		return $http({
			method : 'post',
			url : baseUrl + 'role/getAllRoleList.do',
			data : data
		});
	};
	return services;
} ]);

app.controller('userController', [ '$scope', 'services', '$location',
		function($scope, services, $location) {

			var user = $scope;
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
			// 根据页数获取用户列表
			function getUserListByPage(page) {
				services.getUserListByPage({
					page : page
				}).success(function(data) {
					user.users = data.list;
				});
			}

			// 添加用户
			user.addUser = function() {
				console.log(user.user);
				var userFormData = JSON.stringify(user.user);
				console.log(userFormData);
				if ($("#mustInput").val() == "") {
					alert("带*号的必须填写哦！");
				} else {
					services.addUser({
						user : userFormData
					}).success(function(data) {
						alert("添加用户成功！");
					});
				}
			};

			// 删除用户
			user.deleteUser = function(user_id) {
				$(".tip").fadeIn(200);
				$(".sure").click(function() {
					services.deleteUser({
						userId : user_id
					}).success(function(data) {

						user.result = data;
						if (data == "true") {
							console.log("删除用户列表成功！");
							$("#" + user_id).hide();
						} else {
							console.log("删除用户列表失败！");
						}
					});
					$(".tip").fadeOut(100);
				});
				$(".cancel").click(function() {
					$(".tip").fadeOut(100);
				});
			}
			// 根据页数获取角色列表
			function getRoleListByPage(page) {
				services.getRoleListByPage({
					page : page
				}).success(function(data) {
					user.roles = data.list;
				});
			}

			// 删除角色
			user.deleteRole = function(role_id) {
				$(".tip").fadeIn(200);
				$(".sure").click(function() {
					services.deleteRole({
						roleId : role_id
					}).success(function(data) {

						role.result = data;
						if (data == "true") {
							console.log("删除用户列表成功！");
							$("#" + user_id).hide();
						} else {
							console.log("删除用户列表失败！");
						}
					});
					$(".tip").fadeOut(100);
				});
				$(".cancel").click(function() {
					$(".tip").fadeOut(100);
				});
			}
			// 获取部门列表
			function getAllDepartmentList() {
				services.getAllDepartmentList({}).success(function(data) {
					console.log("获取部门列表成功！");
					user.departs = data;
				});
			}
			// 获取角色列表
			function getAllRoleList() {
				services.getAllRoleList({}).success(function(data) {
					console.log("获取角色列表成功！");
					user.roles = data;
				});
			}

			// 根据输入筛选用户
			user.selectUserByName = function() {
				services.selectUserByName({
					userName : $("#uName").val()
				}).success(function(data) {
					user.users = data;
				});
			};
			// 初始化
			function initData() {
				console.log("初始化页面信息");
				if ($location.path().indexOf('/userList') == 0) {
					services.getUserListByPage({
						page : 1
					}).success(function(data) {
						user.users = data.list;
						pageTurn(data.totalPage, 1)
					});
				} else if ($location.path().indexOf('/userAdd') == 0) {
					console.log("初始化用户新增信息");

					getAllDepartmentList();
					getAllRoleList();

				} else if ($location.path().indexOf('/roleList') == 0) {

					services.getRoleListByPage({
						page : 1
					}).success(function(data) {
						user.roles = data.list;
						pageTurn(data.totalPage, 1)
					});
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