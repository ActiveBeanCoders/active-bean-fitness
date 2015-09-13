package com.activebeancoders.service.es;

import com.activebeancoders.service.EsClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractIndexManager {

    private static final Logger log = LoggerFactory.getLogger(AbstractIndexManager.class);

    @Autowired
    protected EsClient client;

    public String getNumberOfReplicas() {
        return null;
    }

    public String getNumberOfShards() {
        return null;
    }

    public String getRefreshInterval() {
        return null;
    }

    public String getStoreType() {
        return null;
    }

    public abstract String getIndexName();
    public abstract String getIndexType();

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

    public boolean resetRefreshInterval() {
        return setRefreshInterval(getIndexName(), getRefreshInterval());
    }

    /**
     * Rebuilds an index structure.  If the index exists, it will be deleted first.
     * TODO: this cannot be called twice in succession with different index types because the 2nd call will erase what was created by the 1st.
     *
     * @param mapping   Any custom mappings to be applied to the index.
     */
    protected boolean rebuildIndex(Map<String, Object> mapping) {
        // delete index if it already exists
        DeleteIndexRequestBuilder deleteIndexAction = client.admin().indices().prepareDelete(getIndexName());
        deleteIndexAction.execute().actionGet();

        Map<String, String> settings = new HashMap<>();
        if (getNumberOfReplicas() != null) {
            settings.put("index.number_of_replicas", getNumberOfReplicas());
        }
        if (getNumberOfShards() != null) {
            settings.put("index.number_of_shards", getNumberOfShards());
        }
        if (getRefreshInterval() != null) {
            settings.put("index.refresh_interval", getRefreshInterval());
        }
        if (getStoreType() != null) {
            settings.put("index.store.type", getStoreType());
        }

        CreateIndexRequestBuilder b = client.admin().indices().prepareCreate(getIndexName());
        b.addMapping(getIndexType(), mapping);
        b.setSettings(settings);
        return b.execute().actionGet().isAcknowledged();
    }


}
