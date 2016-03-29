package com.activebeancoders.fitness.spring.util.header;

/**
 * @author Dan Barrese
 */
public class AuthcTokenHttpHeader extends HttpHeader {

    public String name;
    public String value;

    public AuthcTokenHttpHeader(String value) {
        super("X-Auth-Token", value);
    }

}

