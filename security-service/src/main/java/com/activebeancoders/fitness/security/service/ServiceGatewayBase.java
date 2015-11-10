package com.activebeancoders.fitness.security.service;

/**
 * @author Dan Barrese
 */
public abstract class ServiceGatewayBase {

    private AuthenticatedUserServiceProvider authenticatedUserServiceProvider;

    public ServiceGatewayBase(AuthenticatedUserServiceProvider authenticatedUserServiceProvider) {
        this.authenticatedUserServiceProvider = authenticatedUserServiceProvider;
    }

    protected UserServiceImpl externalService() {
        return authenticatedUserServiceProvider.provide().getUserService();
    }

}

