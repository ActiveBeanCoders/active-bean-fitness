package com.activebeancoders.service;

import com.activebeancoders.entity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for defining the object mappings for the index.
 */
@Component
public class EsMappings {

    private static final Logger log = LoggerFactory.getLogger(EsIndexer.class);

    @Autowired
    private EsService esService;

    public void defineAllMappings() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> type = new HashMap<>();

        type.put("type", "date");
        properties.put(Activity._date, type);
        mapping.put("properties", properties);

        esService.buildIndex(Activity.class.getPackage().toString(), Activity.class.getSimpleName(), mapping);
    }

}
