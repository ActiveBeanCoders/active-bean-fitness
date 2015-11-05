package com.activebeancoders.fitness.example.infrastructure;

import com.activebeancoders.fitness.domain.DomainUser;
import com.activebeancoders.fitness.domain.Stuff;
import com.activebeancoders.fitness.example.api.samplestuff.ServiceGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceGatewayImpl extends ServiceGatewayBase implements ServiceGateway {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ServiceGatewayImpl(AuthenticatedUserServiceProvider authenticatedUserServiceProvider) {
        super(authenticatedUserServiceProvider);
    }

    @Override
    public List<Stuff> getSomeStuff() {
        String name = externalService().getName();
        if (log.isDebugEnabled()) {
            log.debug("");
        }
        // do some processing, create return list
        return null;
    }

    @Override
    public void createStuff(Stuff newStuff, DomainUser domainUser) {
        // do some processing, store domainUser in newStuff, send newStuff over the wire to external web service etc.
    }
}

