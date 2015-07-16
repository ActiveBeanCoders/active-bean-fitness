package com.activebeancoders.dao;

import com.activebeancoders.entity.Activity;
import com.activebeancoders.service.EsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityDao extends AbstractEsDao<Activity> {

    private static final Class<Activity> INDEX_CLASS = Activity.class;
    private static final String INDEX_NAME = INDEX_CLASS.getPackage().getName();
    private static final String INDEX_TYPE = INDEX_CLASS.getSimpleName();

    protected String getIndexName() {
        return INDEX_NAME;
    }

    protected String getIndexType() {
        return INDEX_TYPE;
    }

    protected Class<Activity> getIndexClass() {
        return INDEX_CLASS;
    }

    public List<Activity> findByLocation(String location) throws JsonProcessingException {
        SearchResponse response = esClient.prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setQuery(QueryBuilders.termQuery("location", location))
                .setFrom(0).setSize(getResultsSize()).setExplain(true)
                .execute()
                .actionGet();
        return convertSearchResponse(response);
    }


}
