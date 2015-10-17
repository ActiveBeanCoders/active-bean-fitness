var app = angular.module('app', ['ngTouch']);

app.controller('MainCtrl',  ['$scope', '$http', '$log', '$interval', '$filter', '$rootScope', '$window',
    function ($scope, $http, $log, $interval, $filter, $rootScope, $window) {

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

}]);

