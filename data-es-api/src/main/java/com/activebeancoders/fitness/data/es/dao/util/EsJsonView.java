package com.activebeancoders.fitness.data.es.dao.util;

import net.pladform.elasticsearch.entity.util.RootEsJsonView;

/**
 * Classes that are used as @JsonView values to control which fields are allowed in the resulting JSON string.
 *
 * @author Dan Barrese
 */
public class EsJsonView extends RootEsJsonView {

    public static class Comment {
    }

}
