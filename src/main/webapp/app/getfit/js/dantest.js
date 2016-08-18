angular.module('hello', []).controller('navigation',

    function($scope, $window) {
        $scope.msg = "hello there";
        $scope.login = function() {
            window.alert('hi');
        };
    }

);

