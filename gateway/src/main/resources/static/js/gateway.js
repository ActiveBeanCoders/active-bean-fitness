var app = angular.module('gateway', ['ngCookies']).config(function($httpProvider) {
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

app.controller('navigation', ['$http', '$scope', '$cookies'], function($scope, $http, $cookies) {
    alert('in controller');

	var authenticate = function(credentials, callback) {

//        $cookies.put('X-Auth-Token', '5ff12051-5bd4-4fb2-8e0b-2f26009cc311');
        alert(credentials.username + ' ' + credentials.password);
		var headers = credentials ? {
			authorization : "Basic "
					+ btoa(credentials.username + ":"
							+ credentials.password)
		} : {};

		$scope.user = ''
		$http.get('user', {
			headers : headers
		}).success(function(data) {
			if (data.name) {
				$scope.authenticated = true;
				$scope.user = data.name
			} else {
				$scope.authenticated = false;
			}
			callback && callback(true);
		}).error(function() {
			$scope.authenticated = false;
			callback && callback(false);
		});

	}

	authenticate();

	$scope.credentials = {};
	$scope.login = function() {
	    alert('login called');
		authenticate($scope.credentials, function(authenticated) {
			$scope.authenticated = authenticated;
			$scope.error = !authenticated;
		})
	};

	$scope.logout = function() {
        alert('logOUT called');
		$http.post('logout', {}).success(function() {
			$scope.authenticated = false;
		}).error(function(data) {
			console.log("Logout failed")
			$scope.authenticated = false;
		});
	}

});
