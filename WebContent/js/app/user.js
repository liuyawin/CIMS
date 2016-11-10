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
// 获取权限列表
var permissionList;
angular.element(document).ready(function() {
	console.log("获取权限列表！");
	$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data; // 
		angular.bootstrap($("#user"), [ 'user' ]); // 手动加载angular模块
	});
});

app.directive('hasPermission', function($timeout) {
	return {
		restrict : 'ECMA',
		link : function(scope, element, attr) {
			var key = attr.hasPermission.trim(); // 获取页面上的权限值
			var keys = permissionList;
			var regStr = "\\s" + key + "\\s";
			var reg = new RegExp(regStr);
			if (keys.search(reg) < 0) {
				element.css("display", "none");
			}
		}
	};
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
		templateUrl : '/CIMS/jsp/systemManagement/roleAdd.html',
		controller : 'userController'
	}).when('/roleList', {
		templateUrl : '/CIMS/jsp/systemManagement/roleList.html',
		controller : 'userController'
	}).when('/userList', {
		templateUrl : '/CIMS/jsp/systemManagement/userList.html',
		controller : 'userController'
	});
} ]);
app.constant('baseUrl', '/CIMS/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	services.getUserListByPage = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/getUserListByPage.do',
			data : data
		});
	};

	services.getAllDepartmentList = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'department/getAllDepartmentList.do',
			data : data
		});
	};

	services.addUser = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/addUser.do',
			data : data
		});
	};
	services.deleteUser = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/deleteUser.do',
			data : data
		});
	};

	services.selectUserByName = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUserByName.do',
			data : data
		});
	};
	services.selectUserById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'user/selectUserById.do',
			data : data
		});
	};
	services.getRoleListByPage = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'role/getRoleListByPage.do',
			data : data
		});
	};

	services.deleteRole = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'role/deleteRole.do',
			data : data
		});
	};
	services.getAllRoleList = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'role/getAllRoleList.do',
			data : data
		});
	};
	services.addRole = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'role/addRole.do',
			data : data
		});

	};
	services.selectRoleById = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'role/selectRoleById.do',
			data : data
		});
	};
	return services;
} ]);

app
		.controller(
				'userController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {

							var user = $scope;
							var searchKey = null;

							// 换页
							function pageTurn(totalPage, page) {

								var $pages = $(".tcdPageCode");
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
									page : page,
									searchKey : searchKey
								}).success(function(data) {
									user.users = data.list;
								});
							}
							// 功能模块权限字段名
							var perName = [ "con_per", "task_per", "bill_per",
									"system_per", "index_per", "left_per" ];
							// 初始化权限数据容器
							function initCheckBoxData() {
								$("input:checkbox[name='selectAllChkBx']")
										.attr("checked", false);

								user.selected = {};
								for (var i = 0; i < 6; i++) {
									user.selected[perName[i]] = new Array();
									for (var j = 0; j < 12; j++)
										user.selected[perName[i]][j] = 0;
								}
								console.log(user.selected);
							}
							// 根据用户选择更新权限数据容器
							var updateSelected = function(action, clazz, name) {
								if (action == 'add') {
									user.selected[clazz][name] = 1;
								}
								if (action == 'remove') {
									user.selected[clazz][name] = 0;
								}
							}
							user.selectAll = function($event, subPerName) {
								if ($event.target.checked == true) {
									for (var i = 0; i < 10; i++)
										user.selected[subPerName][i] = 1;
								} else {
									for (var i = 0; i < 10; i++)
										user.selected[subPerName][i] = 0;
								}

							}
							// 根据用户选择更新权限数据容器
							user.updateSelection = function(e, clazz, name) {
								var checkbox = e.target;
								var action = (checkbox.checked ? 'add'
										: 'remove');
								updateSelected(action, clazz, name);
							}
							// 控件内容初始化
							user.isSelected = function(clazz, name) {
								var t = user.selected[clazz][name];
								return t;
							}

							// 用户模态框开始
							// 点击新建按钮事件
							user.addNewUser = function(e) {
								preventDefault(e);
								services.getAllRoleList().success(
										function(data) {
											user.roles = data;
										});
								user.addinguser = "";
								$(".overlayer").fadeIn(200);
								$(".tip").fadeIn(200);
								$("#addUser-form").slideDown(200);
								$("#editUser-form").hide();
								user.addinguser = {
									user_sex : 0,
									user_dept : 0,
									role : null
								};

							};

							// 点击修改时弹出模态框
							user.editUserBtn = function(obj) {
								var user_id = this.user.user_id;
								services.getAllRoleList().success(
										function(data) {
											user.roles2 = data;
											services.selectUserById({
												userid : user_id
											}).success(function(data) {
												user.editUser = data.user;
											});
										});

								$(".overlayer").fadeIn(200);
								$(".tip").fadeIn(200);
								$("#addUser-form").hide();
								$("#editUser-form").slideDown(200);
								return false;
							};

							// 修改报用户
							$(".sure2").click(function() {
								var EditUser = JSON.stringify(user.editUser);
								services.addUser({
									user : EditUser
								}).success(function(data) {
									alert("修改成功！");
									getUserListByPage(1);
									user.editUser = "";
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
							// 添加用户
							$(".sure1")
									.click(
											function() {
												// 输入验证
												var flag = false;
												if (!user.addinguser.user_num) {
													$('#userNumError')
															.css('display',
																	'inline');
													flag = true;
												} else if (user.addinguser.user_num
														.trim() == "") {
													$('#userNumError')
															.css('display',
																	'inline');
													flag = true;
												}
												if (!user.addinguser.user_pwd) {
													$('#userPwdError')
															.css('display',
																	'inline');
													flag = true;
												} else if (user.addinguser.user_pwd.length < 6) {
													$('#userPwdError')
															.css('display',
																	'inline');
													flag = true;
												}
												if (!user.addinguser.user_name) {
													$('#userNameError')
															.css('display',
																	'inline');
													flag = true;
												} else if (user.addinguser.user_name
														.trim() == "") {
													$('#userNameError')
															.css('display',
																	'inline');
													flag = true;
												}
												if (!user.addinguser.user_tel) {
													$('#userTelError')
															.css('display',
																	'inline');
													flag = true;
												} else if (!user.addinguser.user_tel
														.match(/\d{11}/)) {
													$('#userTelError')
															.css('display',
																	'inline');
													flag = true;
												}
												if (!user.addinguser.user_email) {
													/*
													 * $('#userEmailError')
													 * .css('display',
													 * 'inline'); flag = true;
													 */
												} else if (!user.addinguser.user_email
														.match(/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/)) {
													$('#userEmailError')
															.css('display',
																	'inline');
													flag = true;
												}
												if ($('#userExistError').css(
														'display') == "inline") {
													flag = true;
												}

												if (!user.addinguser.role) {
													$('#userRoleError')
															.css('display',
																	'inline');
													flag = true;
												} else if (!user.addinguser.role.role_id) {
													$('#userRoleError')
															.css('display',
																	'inline');
													flag = true;
												}
												if (flag) {
													return false;
												}
												// 验证完毕
												var AddUser = JSON
														.stringify(user.addinguser);
												services.addUser({
													user : AddUser
												}).success(function(data) {
													alert("新建成功！");
													getUserListByPage(1);
													user.addinguser = "";
												});

												$(".overlayer").fadeOut(100);
												$(".tip").fadeOut(100);
											});
							// 模态框完

							// 角色模态框开始
							// 点击新建按钮事件
							user.addNewRole = function(e) {
								preventDefault(e);
								initCheckBoxData();
								$(".overlayer").fadeIn(200);
								$(".tip").fadeIn(200);
								$("#addRole-form").slideDown(200);
								$("#editRole-form").hide();

							};
							function preventDefault(e) {
								if (e && e.preventDefault) {
									// 阻止默认浏览器动作(W3C)
									e.preventDefault();
								} else {
									// IE中阻止函数器默认动作的方式
									window.event.returnValue = false;
									return false;
								}
							}
							// 点击修改时弹出模态框
							user.editRoleBtn = function(obj) {
								var roleID = this.role.role_id;
								initCheckBoxData();
								services
										.selectRoleById({
											roleid : roleID
										})
										.success(
												function(data) {
													user.editRole = data.role;
													var obj = $
															.parseJSON(data.role.role_permission);
													user.selected = $
															.parseJSON(data.role.role_permission);
												});
								$(".overlayer").fadeIn(200);
								$(".tip").fadeIn(200);
								$("#addRole-form").hide();
								$("#editRole-form").slideDown(200);
								$(".roleEdit").show();
								return false;
							};
							// 点击查看按钮时弹出模态框
							user.detailRoleBtn = function(obj) {
								var roleID = this.role.role_id;
								initCheckBoxData();
								services
										.selectRoleById({
											roleid : roleID
										})
										.success(
												function(data) {
													user.editRole = data.role;
													user.selected = $
															.parseJSON(data.role.role_permission);
												});
								$(".overlayer").fadeIn(200);
								$(".tip").fadeIn(200);
								$("#addRole-form").hide();
								$("#editRole-form").slideDown(200);
								$(".roleEdit").hide();
								return false;
							};
							// 修改角色
							$(".roleEdit")
									.click(
											function() {
												var EditRole = JSON
														.stringify(user.editRole);
												var EditRolePermission = JSON
														.stringify(user.selected);
												console.log(EditRolePermission);
												services
														.addRole(
																{
																	role_name : user.editRole.role_name,
																	role_id : user.editRole.role_id,
																	role_permission : EditRolePermission
																})
														.success(
																function(data) {
																	alert("修改成功！");
																	getRoleListByPage(1);
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
							// 添加角色
							$(".roleAdd")
									.click(
											function() {
												var AddUser = JSON
														.stringify(user.addinguser);
												var rolePermission = JSON
														.stringify(user.selected);
												services
														.addRole(
																{
																	role_name : user.addingRole.role_name,
																	role_permission : rolePermission
																})
														.success(
																function(data) {
																	alert("新建成功！");
																	getRoleListByPage(1);
																});

												$(".overlayer").fadeOut(100);
												$(".tip").fadeOut(100);
											});
							// 模态框完

							// 删除用户
							user.deleteUser = function(user_id) {
								if (confirm("是否删除该用户？") == true) {
									services.deleteUser({
										userId : user_id
									}).success(function(data) {

										user.result = data;
										if (data == "true") {
											console.log("删除用户列表成功！");
										} else {
											console.log("删除用户列表失败！");
										}
										initData();
									});
								}
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

								if (confirm("确定删除该角色吗？")) {
									services.deleteRole({
										roleId : role_id
									}).success(function(data) {
										if (data == "true") {
											console.log("删除用户列表成功！");
										} else {
											console.log("删除用户列表失败！");
										}
										initData();
									});
								}
							}
							// 获取部门列表
							function getAllDepartmentList() {
								services.getAllDepartmentList({}).success(
										function(data) {
											user.departs = data;
										});
							}
							// 获取角色列表
							function getAllRoleList() {
								services.getAllRoleList({}).success(
										function(data) {
											user.roles = data;
										});
							}

							// 根据输入筛选用户
							user.selectUserByName = function() {
								searchKey = user.cName;
								services.getUserListByPage({
									page : 1,
									searchKey : searchKey
								}).success(function(data) {
									user.users = data.list;
									pageTurn(data.totalPage, 1)
								});
							};
							function findRoleFromCookie() {
								var cookie = {};

								var cookies = document.cookie;
								if (cookies === "")
									return cookie;
								var list = cookies.split(";");
								for (var i = 0; i < list.length; i++) {
									var cookieString = list[i];
									var p = cookieString.indexOf("=");
									var name = cookieString.substring(0, p);
									var value = cookieString.substring(p + 1,
											cookieString.length);
									cookie[name.trim()] = value;
									if (name.trim() == "role") {
										sessionStorage.setItem("userRole",
												value);
									}

								}
							}
							function preventDefault(e) {
								if (e && e.preventDefault) {
									// 阻止默认浏览器动作(W3C)
									e.preventDefault();
								} else {
									// IE中阻止函数器默认动作的方式
									window.event.returnValue = false;
									return false;
								}
							}
							// 初始化
							function initData() {
								console.log("初始化页面信息");
								$("#alarm").hide();
								$("#user").show();
								if ($location.path().indexOf('/userList') == 0) {
									searchKey = null;
									services.getUserListByPage({
										page : 1,
										searchKey : searchKey
									}).success(function(data) {
										user.users = data.list;
										pageTurn(data.totalPage, 1)
									});
								} else if ($location.path().indexOf('/userAdd') == 0) {
									getAllDepartmentList();
									getAllRoleList();

								} else if ($location.path()
										.indexOf('/roleList') == 0) {
									initCheckBoxData();
									services.getRoleListByPage({
										page : 1
									}).success(function(data) {
										user.roles = data.list;
										pageTurn(data.totalPage, 1)
									});
								}
							}

							initData();
							findRoleFromCookie();
							$scope.$on('reGetData', function() {
								console.log("重新获取数据！");
								initData();
							});

						} ]);

// 部门的判断
app.filter('userDept', function() {
	return function(input) {
		var dept = "";
		if (input == "0")
			dept = "综合部";
		else if (input == "1")
			dept = "设计部";
		else if (!input)
			dept = "";
		return dept;
	}
});
// 性别判断
app.filter('userSex', function() {
	return function(input) {
		var sex = "";
		if (input == "0")
			sex = "男";
		else if (input == "1")
			sex = "女";
		else if (!input)
			sex = "";
		return sex;
	}
});
