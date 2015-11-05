package com.activebeancoders.fitness.example.infrastructure;

import com.activebeancoders.fitness.example.api.samplestuff.ServiceGateway;
import com.activebeancoders.fitness.domain.DomainUser;
import com.activebeancoders.fitness.domain.Stuff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dan Barrese
 */
@Component
public class ServiceGatewayImpl extends ServiceGatewayBase implements ServiceGateway {

    @Autowired
    public ServiceGatewayImpl(AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider) {
        super(authenticatedExternalServiceProvider);
    }

    @Override
    public List<Stuff> getSomeStuff() {
        String stuffFromExternalWebService = externalService().example();
        // do some processing, create return list
        return null;
    }

    @Override
    public void createStuff(Stuff newStuff, DomainUser domainUser) {
        // do some processing, store domainUser in newStuff, send newStuff over the wire to external web service etc.
    }
}

