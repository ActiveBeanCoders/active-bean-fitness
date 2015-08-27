package com.activebeancoders.dao;

import com.activebeancoders.entity.AbstractEsEntity;
import com.activebeancoders.service.EsClient;
import com.activebeancoders.service.EsService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEsDao<T extends AbstractEsEntity> {

    @Autowired
    protected EsService esService;

    @Autowired
    protected EsClient esClient;

    protected abstract String getIndexName();
    protected abstract String getIndexType();
    protected abstract Class<T> getIndexClass();

    private static final Logger log = LoggerFactory.getLogger(AbstractEsDao.class);

    public T get(String id) {
        try {
            return esService.get(id, getIndexName(), getIndexType(), getIndexClass());
        } catch (IOException e) {
            return null;
        }
    }

    public void save(T t) {
        esService.save(t, getIndexName(), getIndexType(), t.getId());
    }

    public void update(T t, Class<?> jsonView) {
        esService.update(t, jsonView, getIndexName(), getIndexType(), t.getId());
    }

    protected int getResultsSize() {
        return 100;
    }

    protected List<T> convertSearchResponse(SearchResponse response) {
        List<T> results = new ArrayList<>();
        if (response != null) {
            SearchHits hits = response.getHits();
            if (hits != null) {
                try {
                    for (SearchHit hit : hits) {
                        T t = esService.toObject(hit.getSourceAsString(), getIndexClass());
                        results.add(t);
                    }
                } catch (IOException e) {
                    log.error("Failed to convert a search result to Java object.  Returning empty results.");
                    results.clear();
                }
            }
        }
        return results;
    }

    protected SearchRequestBuilder getDefaultSearchRequestBuilder() {
        SearchRequestBuilder b = esClient.prepareSearch(getIndexName());
        b.setTypes(getIndexType());
        b.setFrom(0).setExplain(true);
        return b;
    }

}
