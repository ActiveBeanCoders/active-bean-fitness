(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('ActionController', ActionController);

    ActionController.$inject = ['$scope', '$state', 'Action'];

    function ActionController ($scope, $state, Action) {
        var vm = this;
        
        vm.actions = [];

        loadAll();

        function loadAll() {
            Action.query(function(result) {
                vm.actions = result;
            });
        }
    }
})();
