package com.activebeancoders.controller;

import com.activebeancoders.dao.ActivityDao;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.search.SearchCriteria;
import com.activebeancoders.service.EsIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private EsIndexer esIndexer;

    @RequestMapping(value = RestEndpoint.ACTIVITY, method = RequestMethod.GET)
    public Activity getActivity(@RequestParam String id, Model model) {
        Activity activity = activityDao.get(id);
        model.addAttribute(activity); // TODO: what does this do?
        return activity;
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY, method = RequestMethod.POST)
    public String addActivity(@ModelAttribute Activity activity, Model model) {
        model.addAttribute("activity", activity); // TODO: what does this do?
        activityDao.save(activity);
        return "result";
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    public List<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        int size = 10;
        try {
            size = Integer.valueOf(count);
        } catch (Exception e) {
            log.warn("Count of `{}` cannot be converted to a number.  Defaulting to {}.", count, size);
        }
        return activityDao.findMostRecentActivities(size);
    }

    public List<Activity> search(@RequestParam(required = true) SearchCriteria searchCriteria, Model model) {
        log.debug("searching for... {} ", searchCriteria.toString());
        return new ArrayList<>();
    }

//    @RequestMapping(value = "/activityUpdate", method = RequestMethod.POST)
//    public String updateActivity(@ModelAttribute Activity activity, Model model) {
//        model.addAttribute("activity", activity); // TODO: what does this do?
//        activityDao.update(activity);
//        return "result";
//    }

}
