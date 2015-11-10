package com.activebeancoders.fitness.security.samplestuff;

import com.activebeancoders.fitness.security.domain.CurrentlyLoggedUser;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.Stuff;
import com.activebeancoders.fitness.security.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@RestController
@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class SampleController {

    @Autowired
    @Qualifier("remoteSecurityService")
    private SecurityService securityService;

    public SampleController() {
    }

    @RequestMapping(value = "/safe", method = RequestMethod.GET)
    public String getSomeStuff() {
        return "hi there, here's some stuff!";
    }

    @RequestMapping(value = "/safe", method = RequestMethod.POST)
    public void createStuff(@RequestBody Stuff newStuff, @CurrentlyLoggedUser DomainUser domainUser) {
        System.out.println("creating some stuff!!");
    }

}

