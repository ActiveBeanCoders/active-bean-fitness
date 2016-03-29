package com.activebeancoders.fitness.spring.util.header;

/**
 * @author Dan Barrese
 */
public class UsernameHttpHeader extends HttpHeader {

    public String name;
    public String value;

    public UsernameHttpHeader(String value) {
        super("X-Auth-Username", value);
    }

}

