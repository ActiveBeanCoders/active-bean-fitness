package com.activebeancoders.fitness.example.api.samplestuff;

import com.activebeancoders.fitness.domain.DomainUser;
import com.activebeancoders.fitness.domain.Stuff;

import java.util.List;

public interface ServiceGateway {
    List<Stuff> getSomeStuff();

    void createStuff(Stuff newStuff, DomainUser domainUser);
}
