var app = angular.module('app', ['ngTouch', 'ui.grid', 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.selection', 'ui.grid.pagination', 'ui.grid.exporter', 'ui.grid.autoResize']);

app.controller('MainCtrl',  ['$scope', '$http', '$log', '$interval', '$filter','uiGridConstants', 
    function ($scope, $http, $log, $interval, $filter, uiGridConstants) {

	$scope.hideHomePage  = true;
	$scope.hideActivityPage = true;
	$scope.hideLogPage = true;
	$scope.hideWorkoutsPage = true;
	$scope.hideEquipmentPage = true;
	$scope.hideCoursesPage = true;
	$scope.hideLoginPage = true;
	
	$scope.homePage = function() {
		$scope.hideAllPages();
		$scope.hideHomePage  = false;
	};

	$scope.activityPage = function(){
		$scope.hideAllPages();
		$scope.hideActivityPage = false;
	}
	
	$scope.logPage = function(){
		$scope.hideAllPages();
		$scope.hideLogPage = false;
	}
	
	$scope.workoutsPage = function(){
		$scope.hideAllPages();
		$scope.hideWorkoutsPage = false;
	}
	
	$scope.equipmentPage = function(){
		$scope.hideAllPages();
		$scope.hideEquipmentPage = false;
	}
	
	$scope.coursesPage = function(){
		$scope.hideAllPages();
		$scope.hideCoursesPage = false;
	}
	
	$scope.loginPage = function(){
		$scope.hideAllPages();
		$scope.hideLoginPage = false;
	}
	
	
	$scope.myProgress = function(){
		
	}
	
	$scope.viewOthers = function(){
		
	}
	
	$scope.hideAllPages = function(){
		$scope.hideHomePage  = true;
		$scope.hideActivityPage = true;
		$scope.hideLogPage = true;
		$scope.hideWorkoutsPage = true;
		$scope.hideEquipmentPage = true;
		$scope.hideCoursesPage = true;
		$scope.hideLoginPage = true;
	}
	
	//alert("nowhere");
	
//    $http.get('data/activity-log.json')
//      .success(function(data) {
//    	  alert("test test");
	   //$scope.gridOptions.data = data;
	   //var packageData = [];
	   //$scope.packageData = data;
//    });

    $http.get('/activityLog').success(function(data) {
        $scope.recentActivities = data;
    });

	
}]);