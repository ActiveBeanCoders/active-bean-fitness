package com.activebeancoders.dao;

import com.activebeancoders.entity.AbstractEsEntity;
import com.activebeancoders.service.EsClient;
import com.activebeancoders.service.EsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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

    public void update(T t) {
        esService.update(t, getIndexName(), getIndexType(), t.getId());
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
                    e.printStackTrace(); // TODO: log
                    results.clear();
                }
            }
        }
        return results;
    }

}
