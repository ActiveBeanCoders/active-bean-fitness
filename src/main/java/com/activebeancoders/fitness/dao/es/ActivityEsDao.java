package com.activebeancoders.fitness.dao.es;

import com.activebeancoders.fitness.dao.IActivityDao;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.entity.es.mixin.ActivityMixin;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import net.pladform.elasticsearch.dao.AbstractEsDao;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActivityEsDao extends AbstractEsDao<Activity> implements IActivityDao {

    public static final Class<Activity> INDEX_CLASS = Activity.class;
    public static final String INDEX_NAME = "com.activebeancoders.entity";
    public static final String INDEX_TYPE = "Activity";
    private static final Logger log = LoggerFactory.getLogger(ActivityEsDao.class);

    protected String getIndexName() {
        return INDEX_NAME;
    }

    protected String getIndexType() {
        return INDEX_TYPE;
    }

    protected Class<Activity> getIndexClass() {
        return INDEX_CLASS;
    }

    public List<Activity> findMostRecentActivities(int size) {
        SearchResponse response = esClient.prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setFrom(0).setSize(size).setExplain(true)
                .addSort(ActivityMixin._date, SortOrder.DESC)
                .execute()
                .actionGet();
        return convertSearchResponse(response);
    }

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

}

