package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@RestController
public class SecurityController extends SecurityServiceController {

    private UserServiceImpl userService;

    @Autowired
    public SecurityController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getName() {
        // TODO: pretty sure this doesn't work any more.
        return userService.getName();
    }

}

