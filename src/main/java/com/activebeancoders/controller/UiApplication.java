package com.activebeancoders.controller;

import com.activebeancoders.dao.ActivityDao;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.service.EsIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
@ComponentScan("com.activebeancoders")
public class UiApplication {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private EsIndexer esIndexer;

    @RequestMapping(RestEndpoint.ACTIVITY_LOG)
    public Map<String, Object> activityLog() {
        // TODO: why is this method being called twice on page load?
        Activity a = activityDao.get("1");
        if (a == null) {
            esIndexer.indexAllData();
            a = activityDao.get("1");
        }
        Map<String, Object> model = new HashMap<>();
        model.put("activities", Arrays.asList(a, activityDao.get("2")));
        return model;
    }

    @RequestMapping(RestEndpoint.RESOURCE)
    public Map<String, Object> home() {
        Map<String, Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }

}
