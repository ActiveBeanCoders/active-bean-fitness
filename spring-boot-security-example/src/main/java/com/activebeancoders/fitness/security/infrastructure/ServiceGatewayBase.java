package com.activebeancoders.fitness.security.infrastructure;

/**
 * @author Dan Barrese
 */
public abstract class ServiceGatewayBase {

    private AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider;

    public ServiceGatewayBase(AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider) {
        this.authenticatedExternalServiceProvider = authenticatedExternalServiceProvider;
    }

    protected ExampleServiceImpl externalService() {
        return authenticatedExternalServiceProvider.provide().getExampleService();
    }

}

