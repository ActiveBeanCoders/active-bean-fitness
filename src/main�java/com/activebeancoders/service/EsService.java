package com.activebeancoders.service;

import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

public class EsService {

    private static final Logger log = LoggerFactory.getLogger(EsService.class);

    private boolean verbose;

    @Autowired
    private EsClient client;

    public EsService() {
        verbose = true;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
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

