package com.activebeancoders.controller;

import java.util.*;

import com.activebeancoders.entity.Activity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UiApplication {

    @RequestMapping("/activityLog")
    public Map<String, Object> activityLog() {
        Map<String, Object> model = new HashMap<>();
        List<Activity> activities = new ArrayList<>();
        Activity a = new Activity();
        a.setComment("hi");
        activities.add(a);
        model.put("activities", activities);
        return model;
    }

    @RequestMapping("/resource")
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
