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
	
	$scope.danDoSomething = function(){
		//do something here
		alert("You pushed Me!  Value of your field is: " + $scope.danObject.input1);
	}
	
    $http.get('data/activity-log.json')
      .success(function(data) {
    });

    $http.get('/activityLog').success(function(data) {
        $scope.recentActivities = data;
    });

	
}]);
