package com.activebeancoders.fitness.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Dan Barrese
 */
public class JsonResponse {

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String value;

    public JsonResponse() {
    }

}

