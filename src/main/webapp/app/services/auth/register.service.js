(function () {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
