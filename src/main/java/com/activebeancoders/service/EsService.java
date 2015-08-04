package com.activebeancoders.service;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EsService {

    @Autowired
    private EsClient client;

    @Autowired
    private EsObjectMapper esObjectMapper;

    /**
     * Convert object to JSON using the specified JsonView class.
     */
    public <T> String toJson(T t, Class<?> jsonView) {
        try {
            String json = esObjectMapper.writerWithView(jsonView).writeValueAsString(t);
            System.out.println("toJson produced: " + json);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO: log error
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
            e.printStackTrace(); // TODO: log error
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
        IndexRequestBuilder b = client.prepareIndex(indexName, indexType, id);
        b.setSource(toJson(t));
        b.execute().actionGet();
    }

    /**
     * Save object into Elasticsearch.
     */
    public <T> void update(T t, Class<?> jsonView, String indexName, String indexType, String id) {
        UpdateRequest ur = new UpdateRequest();
        ur.index(indexName);
        ur.type(indexType);
        ur.id(id);
        ur.doc(toJson(t, jsonView));
        client.update(ur).actionGet();
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

