package com.activebeancoders.fitness.data.es.service.es;

import com.activebeancoders.fitness.data.es.dao.ActivityDaoEsImpl;
import com.activebeancoders.fitness.data.es.entity.mixin.ActivityMixin;
import net.pladform.elasticsearch.service.es.AbstractIndexManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dan Barrese
 */
@Component
public class ActivityIndexManagerImpl extends AbstractIndexManager {

    @Value("${elasticsearch.activity.refresh_interval}")
    private String refreshInterval;

    @Value("${elasticsearch.activity.replicas}")
    private String replicas;

    @Value("${elasticsearch.activity.shards}")
    private String shards;

    @Value("${elasticsearch.activity.store.type}")
    private String storeType;

    @Value("${elasticsearch.field.format.date}")
    private String dateFormat;

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
        return ActivityDaoEsImpl.INDEX_NAME;
    }

    public String getIndexType() {
        return ActivityDaoEsImpl.INDEX_TYPE;
    }

    public String getStoreType() {
        return storeType;
    }

    public boolean rebuildIndex() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> type;

        type = new HashMap<>();
        type.put("type", "string");
        type.put("analyzer", "standard");
        properties.put(ActivityMixin._comment, type);

        type = new HashMap<>();
        type.put("type", "string");
        properties.put(ActivityMixin._activity, type);

        type = new HashMap<>();
        type.put("type", "date");
        type.put("format", dateFormat);
        properties.put(ActivityMixin._date, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._distance, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._distHour, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._distMin, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._distSec, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._userId, type);

        type = new HashMap<>();
        type.put("type", "string");
        properties.put(ActivityMixin._unit, type);

        type = new HashMap<>();
        type.put("type", "long");
        properties.put(ActivityMixin._id, type);

        mapping.put("properties", properties);
        return super.rebuildIndex(mapping);
    }

}
