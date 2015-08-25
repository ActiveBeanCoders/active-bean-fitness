package com.activebeancoders.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents search criteria.
 */
public class SearchCriteria {

    private Map<String, Object> simpleCriteria;

    public SearchCriteria() {
        simpleCriteria = new HashMap<>();
    }

    public void addSimpleCriteria(String name, Object value) {
        simpleCriteria.put(name, value);
    }

    public String toString() {
        return simpleCriteria.toString();
    }

}
