package com.activebeancoders.dao;

import com.activebeancoders.entity.Activity;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.SortOrder;
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

//    public List<Activity> findByLocation(String location) {
//        SearchResponse response = esClient.prepareSearch(getIndexName())
//                .setTypes(getIndexType())
//                .setQuery(QueryBuilders.termQuery(Activity., location))
//                .setFrom(0).setSize(getResultsSize()).setExplain(true)
//                .execute()
//                .actionGet();
//        return convertSearchResponse(response);
//    }

    public List<Activity> findMostRecentActivities(int size) {
        SearchResponse response = esClient.prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setFrom(0).setSize(size).setExplain(true)
                .addSort(Activity._date, SortOrder.DESC)
                .execute()
                .actionGet();
        return convertSearchResponse(response);
    }

}
