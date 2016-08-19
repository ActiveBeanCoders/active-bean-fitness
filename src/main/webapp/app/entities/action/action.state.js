(function() {
    'use strict';

    angular
        .module('activeBeanFitnessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('action', {
            parent: 'entity',
            url: '/action',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Actions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action/actions.html',
                    controller: 'ActionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('action-detail', {
            parent: 'entity',
            url: '/action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Action'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action/action-detail.html',
                    controller: 'ActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Action', function($stateParams, Action) {
                    return Action.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'action',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('action-detail.edit', {
            parent: 'action-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-dialog.html',
                    controller: 'ActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Action', function(Action) {
                            return Action.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action.new', {
            parent: 'action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-dialog.html',
                    controller: 'ActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                start: null,
                                duration: null,
                                unit: null,
                                distance: null,
                                comment: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('action');
                });
            }]
        })
        .state('action.edit', {
            parent: 'action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-dialog.html',
                    controller: 'ActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Action', function(Action) {
                            return Action.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action.delete', {
            parent: 'action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-delete-dialog.html',
                    controller: 'ActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Action', function(Action) {
                            return Action.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
