var leftbar = angular.module('leftbar',[]);

app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

receiptapp.directive('hasLeftPermission', function($timeout) {
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