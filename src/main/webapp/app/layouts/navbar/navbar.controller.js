(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($scope, $state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
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
            alert('activity page');
            $scope.activePage = $scope.activityPage;
        }

        $scope.initLogPage = function(){
            alert('log page');
//            loadActivityLog();
            $scope.activePage = $scope.logPage;
        }

        $scope.initWorkoutsPage = function(){
            alert('workouts page');
            $scope.activePage = $scope.workoutsPage;
        }

        $scope.initEquipmentPage = function(){
            alert('equip page');
            $scope.activePage = $scope.equipmentPage;
        }

        $scope.initCoursesPage = function(){
            alert('courses page');
            $scope.activePage = $scope.coursesPage;
        }

        $scope.initMyProgress = function(){
            alert('progress page');
            $scope.activePage = $scope.progressPage;
        }

        $scope.initViewOthers = function(){
            alert('others page');
            $scope.activePage = $scope.progressPage;
        }

        $scope.initHomePage();
    }
})();
