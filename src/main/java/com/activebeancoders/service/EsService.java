package com.activebeancoders.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;

public class EsService {

    private static Client client;
    private static ObjectMapper mapper = new ObjectMapper();
    private static String esHostname = "localhost";
    private static int esPort = 9300;

    /**
     * Establish connection to a runnig Elasticsearch cluster,
     * or retrieve connection if it already exists.
     */
    public static Client connectToElasticsearch() {
        if (client == null) {
            synchronized (EsService.class) {
                if (client == null) {
                    TransportClient tc = new TransportClient();
                    tc.addTransportAddress(new InetSocketTransportAddress(esHostname, esPort));
                    client = tc;
                }
            }
        }
        return client;
    }

    /**
     * Convert object to JSON.
     */
    public static <T> String toJson(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO: log error
            return "ERROR";
        }
    }

    /**
     * Convert JSON to object.
     */
    public static <T> T toObject(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    /**
     * Save object into Elasticsearch.
     */
    public static <T> void save(T t, String indexName, String indexType, String id) {
        IndexRequestBuilder b = connectToElasticsearch().prepareIndex(indexName, indexType, id);
        b.setSource(toJson(t));
        b.execute().actionGet();
    }

    /**
     * Get document from Elasticsearch as Java object.
     */
    public static <T> T get(String id, String indexName, String indexType, Class<T> clazz) throws IOException {
        GetResponse r = connectToElasticsearch().prepareGet(indexName, indexType, id).execute().actionGet();
        String json = r.getSourceAsString();
        return mapper.readValue(json, clazz);
    }

}

