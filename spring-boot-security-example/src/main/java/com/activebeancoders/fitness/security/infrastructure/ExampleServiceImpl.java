package com.activebeancoders.fitness.security.infrastructure;

/**
 * @author Dan Barrese
 */
public class ExampleServiceImpl {

    public String example() {
        System.out.println(String.format("%s -> example called", getClass().getSimpleName()));
        return "From ExampleService";
    }
}
