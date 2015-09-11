package com.activebeancoders.service;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EsService {

    private static final Logger log = LoggerFactory.getLogger(EsService.class);

    private boolean verbose;

    @Autowired
    private EsClient client;

    @Autowired
    private EsObjectMapper esObjectMapper;

    @Value("${elasticsearch.activity.refresh_interval}")
    private String refreshInterval;

    @Value("${elasticsearch.activity.replicas}")
    private String replicas;

    public EsService() {
        verbose = true;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
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
    public <T> boolean save(T t, String indexName, String indexType, String id) {
        String json = toJson(t);
        if (verbose) {
            log.debug("SAVE - {}", json);
        }
        IndexRequestBuilder b = client.prepareIndex(indexName, indexType, id);
        b.setSource(json);
        return b.execute().actionGet().isCreated();
    }
    /**
     * Save object into Elasticsearch without waiting for response from Elasticsearch.
     * Beware, Elasticsearch heap size must be large to handle hundreds of thousands
     * of successive async requests.
     */
    public <T> ListenableActionFuture<IndexResponse> saveAsync(T t, String indexName, String indexType, String id) {
        String json = toJson(t);
        if (verbose) {
            log.debug("SAVE ASYNC - {}", json);
        }
        IndexRequestBuilder b = client.prepareIndex(indexName, indexType, id);
        b.setSource(json);
        return b.execute();
    }

    /**
     * Save object into Elasticsearch.
     */
    public <T> boolean update(T t, Class<?> jsonView, String indexName, String indexType, String id) {
        String json = toJson(t, jsonView);
        if (verbose) {
            log.debug("UPDATE - {}", json);
        }
        UpdateRequest ur = new UpdateRequest();
        ur.index(indexName);
        ur.type(indexType);
        ur.id(id);
        ur.doc(json);
        return client.update(ur).actionGet().isCreated();
    }

    /**
     * Get document from Elasticsearch as Java object.
     */
    public <T> T get(String id, String indexName, String indexType, Class<T> clazz) throws IOException {
        if (verbose) {
            log.debug("GET - {} - {} - {}", indexName, indexType, id);
        }
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
    public boolean buildIndex(String indexName, String indexType, Map<String, Object> mapping) {
        // delete index if it already exists
        DeleteIndexRequestBuilder deleteIndexAction = client.admin().indices().prepareDelete(indexName);
        deleteIndexAction.execute().actionGet();

        Map<String, String> settings = new HashMap<>();
        settings.put("index.number_of_replicas", replicas);
        settings.put("index.refresh_interval", refreshInterval);

        CreateIndexRequestBuilder b = client.admin().indices().prepareCreate(indexName);
        b.addMapping(indexType, mapping);
        b.setSettings(settings);
        return b.execute().actionGet().isAcknowledged();
    }

    public boolean setRefreshInterval(String indexName, String interval) {
        Map<String, String> settings_map = new HashMap<>();
        settings_map.put("index.refresh_interval", interval);
        Settings settings = ImmutableSettings.settingsBuilder().put(settings_map).build();
        UpdateSettingsRequestBuilder usrb = client.admin().indices().prepareUpdateSettings();
        usrb.setIndices(indexName);
        usrb.setSettings(settings);
        UpdateSettingsResponse response = usrb.execute().actionGet();
        if (response.isAcknowledged()) {
            log.debug("Successfully set refresh_interval on index '{}' to '{}'.", indexName, interval);
        } else {
            log.warn("Failed to set refresh_interval on index '{}' to '{}'.", indexName, interval);
        }
        return response.isAcknowledged();
    }

}

