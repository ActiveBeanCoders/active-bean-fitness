package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.security.infrastructure.TokenResponse;
import com.activebeancoders.fitness.security.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dan Barrese
 */
@RestController
public class SecurityController extends SecurityServiceController {

    private UserServiceImpl userService;
    private AuthenticationService authenticationService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public SecurityController(UserServiceImpl userService, AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getName() {
        return userService.getName();
    }

    @RequestMapping(value = "/public/authenticate", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestHeader(value = "X-Auth-Username") String username, @RequestHeader(value = "X-Auth-Password") String password) {
        Optional<String> optionalUsername = Optional.fromNullable(username);
        Optional<String> optionalPassword = Optional.fromNullable(password);

        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(optionalUsername, optionalPassword);
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        return String.valueOf(responseAuthentication.getDetails());
    }

}
