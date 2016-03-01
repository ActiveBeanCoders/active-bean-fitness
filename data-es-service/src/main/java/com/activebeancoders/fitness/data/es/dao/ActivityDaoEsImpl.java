package com.activebeancoders.fitness.data.es.dao;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.es.entity.mixin.ActivityMixin;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;
import net.pladform.elasticsearch.dao.AbstractEsDto;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Moves Activity objects in/out of Elasticsearch.
 *
 * @author Dan Barrese
 */
// TODO: rename to "activityDaoEs"
@Component("activityEsDao")
@Primary
public class ActivityDaoEsImpl extends AbstractEsDto<Activity> implements ActivityDao {

    public static final Class<Activity> INDEX_CLASS = Activity.class;
    public static final String INDEX_NAME = "com.activebeancoders.entity";
    public static final String INDEX_TYPE = "Activity";
    private static final Logger log = LoggerFactory.getLogger(ActivityDaoEsImpl.class);

    @Override
    protected String getIndexName() {
        return INDEX_NAME;
    }

    @Override
    protected String getIndexType() {
        return INDEX_TYPE;
    }

    @Override
    protected Class<Activity> getIndexClass() {
        return INDEX_CLASS;
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        SearchResponse response = esClient.prepareSearch(getIndexName()).setTypes(getIndexType()).setFrom(0)
                .setSize(size).setExplain(true).addSort(ActivityMixin._date, SortOrder.DESC).execute().actionGet();
        return convertSearchResponse(response);
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        log.info("searching for... {} ", activitySearchCriteria);
        SearchRequestBuilder b = getDefaultSearchRequestBuilder();
        if (activitySearchCriteria.getFullText() != null) {
            String[] tokens = activitySearchCriteria.getFullText().split(" ");
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            for (String token : tokens) {
                bool.must(QueryBuilders.matchQuery("_all", token));
            }
            b.setQuery(bool);
        }
        b.addSort(ActivityMixin._date, SortOrder.DESC);
        SearchResponse response = b.execute().actionGet();
        return convertSearchResponse(response);
    }

    @Override
    public Long findMaxId() {
        SearchRequestBuilder b = getDefaultSearchRequestBuilder();
        b.addSort("id", SortOrder.DESC);
        b.setSize(1);
        SearchResponse response = b.execute().actionGet();
        List<Activity> results = convertSearchResponse(response);
        if (results == null || results.isEmpty()) {
            return 0L;
        } else {
            return results.get(0).getId();
        }
    }

}

