package com.activebeancoders.controller.es;

import com.activebeancoders.controller.RestEndpoint;
import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityEsDao activityEsDao;

    @RequestMapping(value = RestEndpoint.ACTIVITY, method = RequestMethod.GET)
    public Activity getActivity(@RequestParam String id, Model model) {
        Activity activity = activityEsDao.get(id);
        model.addAttribute(activity); // TODO: what does this do?
        return activity;
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    public void addActivity(@RequestBody Activity activity) {
        System.out.println("comment=" + activity.getComment());
        activity.setDate(new Date());
        activityEsDao.save(activity);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    public List<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        int size = 10;
        try {
            size = Integer.valueOf(count);
        } catch (Exception e) {
            log.warn("Count of `{}` cannot be converted to a number.  Defaulting to {}.", count, size);
        }
        return activityEsDao.findMostRecentActivities(size);
    }

    @RequestMapping(value = RestEndpoint.SEARCH, method = RequestMethod.POST)
    public List<Activity> search(@RequestBody SearchCriteria searchCriteria) {
        return activityEsDao.search(searchCriteria);
    }

//    @RequestMapping(value = "/activityUpdate", method = RequestMethod.POST)
//    public String updateActivity(@ModelAttribute Activity activity, Model model) {
//        model.addAttribute("activity", activity); // TODO: what does this do?
//        activityDao.update(activity);
//        return "result";
//    }

}
