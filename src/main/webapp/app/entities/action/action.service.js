(function() {
    'use strict';
    angular
        .module('activeBeanFitnessApp')
        .factory('Action', Action);

    Action.$inject = ['$resource', 'DateUtils'];

    function Action ($resource, DateUtils) {
        var resourceUrl =  'api/actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
