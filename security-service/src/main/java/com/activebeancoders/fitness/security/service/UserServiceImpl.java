package com.activebeancoders.fitness.security.service;

import org.springframework.stereotype.Component;

/**
 * @author Dan Barrese
 */
@Component
public class UserServiceImpl {

    public String getName() {
        return "From security-service -> user service";
    }

    public String getOpen() {
        return "hello from open";
    }

}

