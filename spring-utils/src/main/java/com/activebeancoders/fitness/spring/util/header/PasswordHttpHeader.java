package com.activebeancoders.fitness.spring.util.header;

/**
 * @author Dan Barrese
 */
public class PasswordHttpHeader extends HttpHeader {

    public String name;
    public String value;

    public PasswordHttpHeader(String value) {
        super("X-Auth-Password", value);
    }

}

