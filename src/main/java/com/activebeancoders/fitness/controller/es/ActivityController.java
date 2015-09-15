package com.activebeancoders.fitness.controller.es;

import com.activebeancoders.fitness.controller.RestEndpoint;
import com.activebeancoders.fitness.dao.IActivityDao;
import com.activebeancoders.fitness.dao.es.ActivityEsDao;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    @Qualifier("activityEsDao")
    private IActivityDao activityEsDao;

    @RequestMapping(value = RestEndpoint.ACTIVITY_BY_ID, method = RequestMethod.GET)
    public Activity get(@PathVariable("id") String id) {
        return activityEsDao.get(id);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    public void addActivity(@RequestBody Activity activity) {
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
    public List<Activity> search(@RequestBody ActivitySearchCriteria activitySearchCriteria) {
        return activityEsDao.search(activitySearchCriteria);
    }

//    @RequestMapping(value = "/activityUpdate", method = RequestMethod.POST)
//    public String updateActivity(@ModelAttribute Activity activity, Model model) {
//        model.addAttribute("activity", activity); // TODO: what does this do?
//        activityDao.update(activity);
//        return "result";
//    }

}
