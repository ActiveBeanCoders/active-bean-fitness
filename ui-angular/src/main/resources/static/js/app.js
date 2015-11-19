var app = angular.module('app', ['ngTouch', 'ngCookies']);

//app.config(function($httpProvider, $cookies) {
//    $httpProvider.defaults.headers.common['X-Auth-Token'] = '5ff12051-5bd4-4fb2-8e0b-2f26009cc311';
//    $cookies.put('X-Auth-Token', '5ff12051-5bd4-4fb2-8e0b-2f26009cc311');
//    $httpProvider.defaults.headers.common['X-Auth-Token'] = $cookies.get('X-Auth-Token');
//});

app.controller('MainCtrl',  ['$scope', '$http', '$log', '$interval', '$filter', '$rootScope', '$window', '$cookies',
    function ($scope, $http, $log, $interval, $filter, $rootScope, $window, $cookies) {

//    $http.defaults.headers.common['X-Auth-Token'] = '5ff12051-5bd4-4fb2-8e0b-2f26009cc311';
    $http.defaults.headers.common['X-Auth-Token'] = $cookies.get('X-Auth-Token');

    $scope.messages = [];

    $scope.activePage = "";
	$scope.pageMode = "";

    $scope.searchMode = "search";
    $scope.editMode = "edit";
    $scope.viewMode = "view";
    $scope.createMode = "create";

    //Page Names
    $scope.activityPage = "Activity_Page";
    $scope.coursesPage = "Courses_Page";
    $scope.equipmentPage = "Equipment_Page";
    $scope.homePage = "Home_Page";
    $scope.loginPage = "Login_Page";
    $scope.progressPage = "Progress_Page";
    $scope.logPage = "Log_Page";
    $scope.workoutsPage = "Workouts_Page";
    $scope.viewOthersPage = "View_Others_Page";
	$scope.danObject = {};

	$scope.initHomePage = function() {
		$scope.activePage = $scope.homePage;
	}

	$scope.initActivityPage = function(){
		$scope.activePage = $scope.activityPage;
	}

	$scope.initLogPage = function(){
		$scope.activePage = $scope.logPage;
	}

	$scope.initWorkoutsPage = function(){
		$scope.activePage = $scope.workoutsPage;
	}

	$scope.initEquipmentPage = function(){
		$scope.activePage = $scope.equipmentPage;
	}

	$scope.initCoursesPage = function(){
		$scope.activePage = $scope.coursesPage;
	}

	$scope.initLoginPage = function(){
		$scope.activePage = $scope.loginPage;
	}

	$scope.initMyProgress = function(){
		$scope.activePage = $scope.progressPage;
	}

	$scope.initViewOthers = function(){
		$scope.activePage = $scope.progressPage;
	}

    $scope.searchCriteria = {};
    $scope.searchActivity = function() {
        $http({
            method: 'POST', url: '/resource/search',
            data: { 'fullText': $scope.searchCriteria.fullText }
        }).
        success(function(data, status, headers, config) {
            $scope.messages.push('Search OK.');
            $scope.searchResults = data;
        }).
        error(function(data, status, headers, config) {
            $scope.messages.push('ERROR - Search failed.');
        });
    };

    $scope.activityToAdd = {};
    $scope.addActivity = function() {
        $http({
            method: 'POST', url: '/resource/activity-add',
            data: $scope.activityToAdd
        }).
        success(function(data, status, headers, config) {
            $scope.messages.push("Saved.");
        }).
        error(function(data, status, headers, config) {
            $scope.messages.push('ERROR - Save failed.');
        });
    };

    $http.get('/resource/activityLog').success(function(data) {
        $scope.recentActivities = data;
    });

	console.log('Loading')
	$http.get('user').success(function(data) {
		if (data.name) {
			$scope.authenticated = true;
			$scope.user = data.name
		} else {
			$scope.authenticated = false;
		}
	}).error(function() {
		$scope.authenticated = false;
	});

	var authenticate = function(credentials, callback) {
//        $cookies.put('X-Auth-Token', '5ff12051-5bd4-4fb2-8e0b-2f26009cc311');
//        alert(credentials.username + ' ' + credentials.password);
        alert('authenticating...');
        $http({
            method: 'POST',
            url: '/security/public/authenticate',
            headers: {
                'X-Auth-Username': 'user',
                'X-Auth-Password': 'password'
            }
        }).
        success(function(data, status, headers, config) {
            alert('AUTHENTIACTED!! with data:' + data);
//            $scope.authenticated = true;
//            $cookies.put('X-Auth-Token', data);
//            $http.defaults.headers.common['X-Auth-Token'] = data;
        }).
        error(function(data, status, headers, config) {
            alert('failed to authenticate...');
//            $scope.authenticated = false;
        });
    }
//		var headers = credentials ? {
//			authorization : "Basic "
//					+ btoa(credentials.username + ":"
//							+ credentials.password)
//		} : {};
//
//		$scope.user = ''
//		$http.get('user', {
//			headers : headers
//		}).success(function(data) {
//			if (data.name) {
//				$scope.authenticated = true;
//				$scope.user = data.name
//			} else {
//				$scope.authenticated = false;
//			}
//			callback && callback(true);
//		}).error(function() {
//			$scope.authenticated = false;
//			callback && callback(false);
//		});
//
//	}
//
//	authenticate();

	$scope.credentials = {};
	$scope.login = function() {
		authenticate($scope.credentials, function(authenticated) {
			$scope.authenticated = authenticated;
			$scope.error = !authenticated;
		})
	};

//	$scope.logout = function() {
//        alert('logOUT called');
//		$http.post('logout', {}).success(function() {
//			$scope.authenticated = false;
//		}).error(function(data) {
//			console.log("Logout failed")
//			$scope.authenticated = false;
//		});
//	}

}]);

