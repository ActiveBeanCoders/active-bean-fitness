var app = angular.module('app', ['ngTouch']);

app.controller('MainCtrl',  ['$scope', '$http', '$log', '$interval', '$filter', 
    function ($scope, $http, $log, $interval, $filter) {

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
	
    $scope.messages = [];
    $scope.search = {};
    $scope.searchActivity = function() {
        $http({
            method: 'POST', url: '/search',
            data: { 'fullText': $scope.search.fullText }
        }).
        success(function(data, status, headers, config) {
            $scope.searchResults = data;
        }).
        error(function(data, status, headers, config) {
            alert("failed");
            if(status == 400) {
                $scope.messages = data;
            } else {
                alert('Unexpected server error.');
            }
        });
    };

    $http.get('/activityLog').success(function(data) {
        $scope.recentActivities = data;
    });

}]);
