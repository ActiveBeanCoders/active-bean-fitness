package com.activebeancoders.fitness.data.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents search criteria.
 *
 * @author Dan Barrese
 */
public class ActivitySearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, Object> simpleCriteria;

    /**
     * "Full text search" string.  This will match against any/all fields.
     */
    private String fullText;

    public ActivitySearchCriteria() {
        simpleCriteria = new HashMap<>();
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public void addSimpleCriteria(String name, Object value) {
        simpleCriteria.put(name, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchCriteria{");
        sb.append("simpleCriteria=").append(simpleCriteria);
        sb.append(", fullText='").append(fullText).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
