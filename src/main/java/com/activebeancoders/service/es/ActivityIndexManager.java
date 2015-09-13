package com.activebeancoders.service.es;

import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.entity.ActivityEs;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class ActivityIndexManager extends AbstractIndexManager {

    @Value("${elasticsearch.activity.refresh_interval}")
    private String refreshInterval;

    @Value("${elasticsearch.activity.replicas}")
    private String replicas;

    @Value("${elasticsearch.activity.shards}")
    private String shards;

    @Value("${elasticsearch.activity.store.type}")
    private String storeType;

    public String getNumberOfReplicas() {
        return replicas;
    }

    public String getNumberOfShards() {
        return shards;
    }

    public String getRefreshInterval() {
        return refreshInterval;
    }

    public String getIndexName() {
        return ActivityEsDao.INDEX_NAME;
    }

    public String getIndexType() {
        return ActivityEsDao.INDEX_TYPE;
    }

    public String getStoreType() {
        return storeType;
    }

    public boolean rebuildIndex() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> type = new HashMap<>();

        type.put("type", "date");
        properties.put(ActivityEs._date, type);
        mapping.put("properties", properties);
        return super.rebuildIndex(mapping);
    }

}
