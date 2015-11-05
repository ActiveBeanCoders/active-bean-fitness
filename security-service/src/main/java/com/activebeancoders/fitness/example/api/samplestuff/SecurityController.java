package com.activebeancoders.fitness.example.api.samplestuff;

import com.activebeancoders.fitness.example.infrastructure.api.SecurityServiceController;
import com.activebeancoders.fitness.example.infrastructure.externalwebservice.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return userService.getName();
    }

}
