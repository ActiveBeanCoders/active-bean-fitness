(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('ActionDetailController', ActionDetailController);

    ActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Action'];

    function ActionDetailController($scope, $rootScope, $stateParams, previousState, entity, Action) {
        var vm = this;

        vm.action = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('activeBeanFitnessApp:actionUpdate', function(event, result) {
            vm.action = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
