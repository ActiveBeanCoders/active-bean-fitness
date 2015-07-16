package com.activebeancoders.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexAction;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Component
public class EsService {

    @Autowired
    private EsClient client;

    @Autowired
    private EsObjectMapper esObjectMapper;

    /**
     * Convert object to JSON.
     */
    public <T> String toJson(T t) {
        try {
            return esObjectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO: log error
            return "ERROR";
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
        IndexRequestBuilder b = client.prepareIndex(indexName, indexType, id);
        b.setSource(toJson(t));
        b.execute().actionGet();
    }

    /**
     * Get document from Elasticsearch as Java object.
     */
    public <T> T get(String id, String indexName, String indexType, Class<T> clazz) throws IOException {
        GetResponse r = client.prepareGet(indexName, indexType, id).execute().actionGet();
        String json = r.getSourceAsString();
        return json == null ? null : toObject(json, clazz);
    }

}

