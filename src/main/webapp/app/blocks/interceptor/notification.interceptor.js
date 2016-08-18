(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .factory('notificationInterceptor', notificationInterceptor);

    notificationInterceptor.$inject = ['$q', 'AlertService'];

    function notificationInterceptor ($q, AlertService) {
        var service = {
            response: response
        };

        return service;

        function response (response) {
            var alertKey = response.headers('X-activeBeanFitnessApp-alert');
            if (angular.isString(alertKey)) {
                AlertService.success(alertKey, { param : response.headers('X-activeBeanFitnessApp-params')});
            }
            return response;
        }
    }
})();
