package com.activebeancoders.fitness.example.api.samplestuff;

import com.activebeancoders.fitness.domain.CurrentlyLoggedUser;
import com.activebeancoders.fitness.domain.DomainUser;
import com.activebeancoders.fitness.domain.Stuff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class SampleController {

    @Autowired
    @Qualifier("remoteSecurityService")
    private SecurityService securityService;

    private final ServiceGateway serviceGateway;

    @Autowired
    public SampleController(ServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    @RequestMapping(value = "/safe", method = RequestMethod.GET)
    public String getSomeStuff() {
        System.out.println(String.format("getting some stuff!!!"));
//        return "hi there, here's some stuff!";
        return securityService.sayHello();
        //        return serviceGateway.getSomeStuff();
    }

    @RequestMapping(value = "/safe", method = RequestMethod.POST)
    public void createStuff(@RequestBody Stuff newStuff, @CurrentlyLoggedUser DomainUser domainUser) {
        serviceGateway.createStuff(newStuff, domainUser);
    }

}

