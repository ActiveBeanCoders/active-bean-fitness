(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .controller('ActionDialogController', ActionDialogController);

    ActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action'];

    function ActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Action) {
        var vm = this;

        vm.action = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.action.id !== null) {
                Action.update(vm.action, onSaveSuccess, onSaveError);
            } else {
                Action.save(vm.action, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('activeBeanFitnessApp:actionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
