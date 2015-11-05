package com.activebeancoders.fitness.example.infrastructure;

public class ExampleServiceImpl {

    public String example() {
        System.out.println(String.format("%s -> example called", getClass().getSimpleName()));
        return "From ExampleService";
    }
}
