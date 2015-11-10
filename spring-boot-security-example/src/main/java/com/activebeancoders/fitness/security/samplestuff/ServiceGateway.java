package com.activebeancoders.fitness.security.samplestuff;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.Stuff;

import java.util.List;

/**
 * @author Dan Barrese
 */
public interface ServiceGateway {
    List<Stuff> getSomeStuff();

    void createStuff(Stuff newStuff, DomainUser domainUser);
}
