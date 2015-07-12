package net.pladform.dao;

import net.pladform.service.EsService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEsDao<T> {

    protected abstract String getIndexName();
    protected abstract String getIndexType();
    protected abstract Class<T> getIndexClass();

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
                        T t = EsService.toObject(hit.getSourceAsString(), getIndexClass());
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
