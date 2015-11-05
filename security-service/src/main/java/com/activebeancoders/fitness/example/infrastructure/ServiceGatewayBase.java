package com.activebeancoders.fitness.example.infrastructure;

import com.activebeancoders.fitness.example.infrastructure.externalwebservice.UserServiceImpl;

public abstract class ServiceGatewayBase {

    private AuthenticatedUserServiceProvider authenticatedUserServiceProvider;

    public ServiceGatewayBase(AuthenticatedUserServiceProvider authenticatedUserServiceProvider) {
        this.authenticatedUserServiceProvider = authenticatedUserServiceProvider;
    }

    protected UserServiceImpl externalService() {
        return authenticatedUserServiceProvider.provide().getUserService();
    }

}

