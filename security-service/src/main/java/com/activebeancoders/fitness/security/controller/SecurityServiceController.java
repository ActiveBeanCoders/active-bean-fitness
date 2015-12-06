package com.activebeancoders.fitness.security.controller;

import com.activebeancoders.fitness.security.api.SecurityService;
import com.activebeancoders.fitness.security.domain.CurrentlyLoggedInUser;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * @author Dan Barrese
 */
@Configuration
@RestController
public class SecurityServiceController {

    @Autowired
    private SecurityService securityService;

    public SecurityServiceController() {
    }

    // Sample method showing off @CurrentlyLoggedInUser
    @RequestMapping("/user")
    public Map<String, String> user(@CurrentlyLoggedInUser DomainUser domainUser) {
        return Collections.singletonMap("username", domainUser.getUsername());
    }

    // TODO: should this be publicly accessible?  Seems like easy DOS opportunity.
    @RequestMapping(value = "/public/user/create", method = RequestMethod.POST)
    public DomainUser createUserAccount(@RequestHeader(value = "username") final String username,
                                        @RequestHeader(value = "plaintextPassword") final String plaintextPassword) {
        DomainUserCredentials domainUserCredentials = new DomainUserCredentials(username, plaintextPassword);
        return securityService.createUserAccount(domainUserCredentials);
    }

}

