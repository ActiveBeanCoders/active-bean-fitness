package com.activebeancoders.service;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EsService {

    private static final Logger log = LoggerFactory.getLogger(EsService.class);

    @Autowired
    private EsClient client;

    @Autowired
    private EsObjectMapper esObjectMapper;

    public EsService() {
    }

    /**
     * Convert object to JSON using the specified JsonView class.
     */
    public <T> String toJson(T t, Class<?> jsonView) {
        try {
            String json = esObjectMapper.writerWithView(jsonView).writeValueAsString(t);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Could not convert object to JSON.", e);
            return "ERROR";
        }
    }

    /**
     * Convert object to JSON.
     */
    public <T> String toJson(T t) {
        return toJson(t, View.All.class);
    }

    /**
     * Convert object to byte array.
     */
    public <T> byte[] toBytes(T t) {
        try {
            return esObjectMapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            log.error("Could not convert object to byte array.", e);
            return null;
        }
    }

    /**
     * Convert JSON to object.
     */
    public <T> T toObject(String json, Class<T> clazz) throws IOException {
        return esObjectMapper.readValue(json, clazz);
    }

    /**
     * Save object into Elasticsearch.
     */
    public <T> void save(T t, String indexName, String indexType, String id) {
        String json = toJson(t);
        log.info("SAVE - {}", json);
        IndexRequestBuilder b = client.prepareIndex(indexName, indexType, id);
        b.setSource(json);
        b.execute().actionGet();
    }

    /**
     * Save object into Elasticsearch.
     */
    public <T> void update(T t, Class<?> jsonView, String indexName, String indexType, String id) {
        String json = toJson(t, jsonView);
        log.info("UPDATE - {}", json);
        UpdateRequest ur = new UpdateRequest();
        ur.index(indexName);
        ur.type(indexType);
        ur.id(id);
        ur.doc(json);
        client.update(ur).actionGet();
    }

    /**
     * Get document from Elasticsearch as Java object.
     */
    public <T> T get(String id, String indexName, String indexType, Class<T> clazz) throws IOException {
        log.info("GET - {}", id);
        GetResponse r = client.prepareGet(indexName, indexType, id).execute().actionGet();
        String json = r.getSourceAsString();
        return json == null ? null : toObject(json, clazz);
    }

    /**
     * Rebuilds an index.  If the index exists, it will be deleted first.
     * TODO: this cannot be called twice in succession with different index types because the 2nd call will erase what was created by the 1st.
     *
     * @param indexName The name of the index.
     * @param indexType The name of the index type.
     * @param mapping Any custom mappings to be applied to the index.
     */
    public void buildIndex(String indexName, String indexType, Map<String, Object> mapping) {
        // delete index if it already exists
        DeleteIndexRequestBuilder deleteIndexAction = client.admin().indices().prepareDelete(indexName);
        deleteIndexAction.execute().actionGet();

        CreateIndexRequestBuilder b = client.admin().indices().prepareCreate(indexName);
        b.addMapping(indexType, mapping);
        b.execute().actionGet();
    }

}

