var Permission = angular.module('Permission', []);

var permissionList;

angular.element(document).ready(function() {
	console.log("获取权限列表！");
	 $.get('/CIMS/login/getUserPermission.do', function(data) {
	 permissionList = data; // 
	 console.log("身份是：" + permissionList);
	 angular.bootstrap(document, ['App']);  
	 });
});

Permission.directive('hasPermission', function($timeout) {
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

