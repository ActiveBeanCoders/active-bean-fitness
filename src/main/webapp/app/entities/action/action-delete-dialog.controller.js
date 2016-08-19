(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('ActionDeleteController',ActionDeleteController);

    ActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Action'];

    function ActionDeleteController($uibModalInstance, entity, Action) {
        var vm = this;

        vm.action = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Action.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
