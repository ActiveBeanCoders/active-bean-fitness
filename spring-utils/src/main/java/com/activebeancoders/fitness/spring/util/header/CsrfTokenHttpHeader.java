package com.activebeancoders.fitness.spring.util.header;

/**
 * @author Dan Barrese
 */
public class CsrfTokenHttpHeader extends HttpHeader {

    public String name;
    public String value;

    public CsrfTokenHttpHeader(String value) {
        super("X-CSRF-Token", value);
    }

}

