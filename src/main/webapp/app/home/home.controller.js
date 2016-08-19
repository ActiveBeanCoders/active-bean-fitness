(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$http', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, $http, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
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
                method: 'POST', url: '/activity-add',
                data: $scope.activityToAdd
            }).
            success(function(data, status, headers, config) {
                alert('success');
                $scope.messages.push("Saved.");
            }).
            error(function(data, status, headers, config) {
                alert(status);
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

    }
})();

