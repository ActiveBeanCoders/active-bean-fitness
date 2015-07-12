package net.pladform.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.pladform.service.EsService;
import net.pladform.entity.Run;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.List;

public class RunEsDao extends AbstractEsDao<Run> {

    private static final Class<Run> INDEX_CLASS = Run.class;
    private static final String INDEX_NAME = INDEX_CLASS.getPackage().getName();
    private static final String INDEX_TYPE = INDEX_CLASS.getSimpleName();

    protected String getIndexName() {
        return INDEX_NAME;
    }

    protected String getIndexType() {
        return INDEX_TYPE;
    }

    protected Class<Run> getIndexClass() {
        return INDEX_CLASS;
    }

    public Run get(String id) throws IOException {
        return EsService.get(id, getIndexName(), getIndexType(), getIndexClass());
    }

    public void save(Run run) throws JsonProcessingException {
        EsService.save(run, getIndexName(), getIndexType(), run.id);
    }

    public List<Run> findByLocation(String location) throws JsonProcessingException {
        SearchResponse response = EsService.connectToElasticsearch().prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setQuery(QueryBuilders.termQuery("location", location))
                .setFrom(0).setSize(getResultsSize()).setExplain(true)
                .execute()
                .actionGet();
        return convertSearchResponse(response);
    }


}
