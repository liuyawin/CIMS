var app = angular.module('contract',['ngRoute']);
	app.config([ '$routeProvider', function($routeProvider) {
		$routeProvider
		.when('/test', {
			templateUrl : '/CIMS/jsp/contractInformation/test.html',
			//controller : 'UserController'
		})
		.when('/another', {
			templateUrl : '/CIMS/jsp/contractInformation/another.html',
			//controller : 'UserController'
		});
	} ]);
