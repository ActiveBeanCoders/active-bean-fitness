package com.activebeancoders.fitness.example.infrastructure.externalwebservice;

import org.springframework.stereotype.Component;

/**
 * @author Dan Barrese
 */
@Component
public class UserServiceImpl {

    public String getName() {
        System.out.println(String.format("%s -> getSomeStuff called", getClass().getSimpleName()));
        return "From external WebService";
    }

    public String getOpen() {
        return "hello from open";
    }

}

