var app = angular.module('app', ['ngTouch', 'ngCookies']);

app.controller('MainCtrl',  ['$scope', '$http', '$log', '$interval', '$filter', '$rootScope', '$window', '$cookies',
    function ($scope, $http, $log, $interval, $filter, $rootScope, $window, $cookies) {

    // authentication stuff
    // -----------------------------------------------------------------------

    // holds username, password
	$scope.credentials = {};

	// Indicates if user is authenticated.
    $scope.authenticated = false;

    // Default username.
    $scope.username = "guest";

    // authenticates the given credentials
	var verifyToken = function(token) {
        $http({
            method: 'POST',
            url: '/security/public/token/verify',
            headers: {
                'X-Auth-Token': token
            }
        }).
        success(function(data, status, headers, config) {
            return true;
        }).
        error(function(data, status, headers, config) {
            return false;
        });
    }

	// If the session token exists as a cookie, set it to be used for every HTTP(S) request.
	var useSessionTokenForEachRequest = function() {
	    sessionTokenCookie = $cookies.get('X-Auth-Token');
	    if (sessionTokenCookie) {
            $scope.authenticated = true;
            $http.defaults.headers.common['X-Auth-Token'] = sessionTokenCookie;
	    }
	    username = $cookies.get('username');
	    if (username) {
            $scope.username = $cookies.get('username');
	    }
	}

    // Clears all user data held locally as JS variables or browser cookies.
	var clearLocalUserData = function() {
        $cookies.remove('X-Auth-Token');
        $cookies.remove('username');
        $scope.authenticated = false;
        $scope.username = "guest";
	}

    // authenticates the given credentials
	var authenticate = function(credentials) {
        $http({
            method: 'POST',
            url: '/security/public/authenticate',
            headers: {
                'X-Auth-Username': credentials.username,
                'X-Auth-Password': credentials.password
            }
        }).
        success(function(data, status, headers, config) {
            $scope.authenticated = true;
            cookieOptions = {}
//            cookieOptions.domain = "localhost";
//            cookieOptions.secure = true; // restricts to HTTPS only
            $cookies.put('X-Auth-Token', data.token, cookieOptions);
            $cookies.put('username', credentials.username, cookieOptions);
            useSessionTokenForEachRequest();
        }).
        error(function(data, status, headers, config) {
//            alert(JSON.stringify(data));
            clearLocalUserData();
        });
    }

    useSessionTokenForEachRequest(); // if it exists.

    // Log-in function
	$scope.login = function() {
		authenticate($scope.credentials);
	};

    // Log-out function
	$scope.logout = function() {
		$http.post('/security/public/logout', {})
		.success(function() {
            clearLocalUserData();
			$window.location.reload();
		}).error(function(data) {
		    // TODO: do something useful here instead of alert.
            alert('log-out failed');
		});
	}

    // user sign-up stuff
    // -----------------------------------------------------------------------

	$scope.createUserAccount = function() {
		createUserAccount($scope.credentials);
	};

	var createUserAccount = function(credentials) {
        $http({
            method: 'POST',
            url: '/security/public/user/create',
            headers: {
                'username': credentials.username,
                'plaintextPassword': credentials.password
            }
        }).
        success(function(data, status, headers, config) {
            authenticate(credentials);
        }).
        error(function(data, status, headers, config) {
//            alert(JSON.stringify(data));
            clearLocalUserData();
        });
    }

    // "private" functions (not accessible from the UI).
    // -----------------------------------------------------------------------

    var loadActivityLog = function() {
        $http.get('/alldata/api/activity/recent').success(function(data) {
            $scope.recentActivities = data;
        });
    }


    // variables accessible in the UI
    // -----------------------------------------------------------------------

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
    $scope.signUpPage = "Sign_Up_Page";
    $scope.progressPage = "Progress_Page";
    $scope.logPage = "Log_Page";
    $scope.workoutsPage = "Workouts_Page";
    $scope.viewOthersPage = "View_Others_Page";

    // functions accessible in the UI
    // -----------------------------------------------------------------------

	$scope.initHomePage = function() {
		$scope.activePage = $scope.homePage;
	}

	$scope.initActivityPage = function(){
		$scope.activePage = $scope.activityPage;
	}

	$scope.initLogPage = function(){
	    loadActivityLog();
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

	$scope.initSignUpPage = function(){
		$scope.activePage = $scope.signUpPage;
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
            method: 'POST', url: '/alldata/api/activity/search',
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
            method: 'POST', url: '/alldata/api/activity/add',
            data: $scope.activityToAdd
        }).
        success(function(data, status, headers, config) {
            $scope.messages.push("Saved.");
        }).
        error(function(data, status, headers, config) {
            $scope.messages.push('ERROR - Save failed.');
        });
    };

    $scope.reloadCount = 0;
    $scope.reloadResult = "";
	$scope.reloadActivities = function() {
        $scope.reloadResult = "";
	    params = null;
	    if ($scope.reloadCount && $scope.reloadCount > 0) {
	        params = "?count=" + $scope.reloadCount;
	    } else {
	        params = "?count=11";
	    }
        $http({
            method: 'POST',
            url: '/alldata/api/activity/reload' + params
        }).
        success(function(data, status, headers, config) {
            $scope.reloadResult = "Success! " + data.value;
        }).
        error(function(data, status, headers, config) {
            $scope.reloadResult = "Failed to reload records.";
        });
    };

    $scope.initHomePage();

}]);

