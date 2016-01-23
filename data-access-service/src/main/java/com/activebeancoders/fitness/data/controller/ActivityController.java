package com.activebeancoders.fitness.data.controller;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dan Barrese
 */
@RestController
@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityDao activityDao;

    @RequestMapping(value = com.activebeancoders.fitness.data.es.controller.RestEndpoint.ACTIVITY_BY_ID, method = RequestMethod.GET)
    public Activity get(@PathVariable("id") String id) {
        return activityDao.get(id);
    }

    @RequestMapping(value = com.activebeancoders.fitness.data.es.controller.RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addActivity(@RequestBody Activity activity) {
        activityDao.save(activity);
    }

    @RequestMapping(value = com.activebeancoders.fitness.data.es.controller.RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    public List<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        int size = 10;
        try {
            size = Integer.valueOf(count);
        } catch (Exception e) {
            log.warn("Count of `{}` cannot be converted to a number.  Defaulting to {}.", count, size);
        }
        return activityDao.findMostRecentActivities(size);
    }

    @RequestMapping(value = com.activebeancoders.fitness.data.es.controller.RestEndpoint.SEARCH, method = RequestMethod.POST)
    public List<Activity> search(@RequestBody ActivitySearchCriteria activitySearchCriteria) {
        return activityDao.search(activitySearchCriteria);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_GET_PRIMARY_DAO, method = RequestMethod.GET)
    public int getPrimaryDaoIndex() {
        return activityDao.getPrimaryDaoIndex();
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_SET_PRIMARY_DAO, method = RequestMethod.GET)
    public int setPrimaryDaoName(@PathVariable("name") int primaryDaoIndex) {
        activityDao.setPrimaryDaoIndex(primaryDaoIndex);
        return activityDao.getPrimaryDaoIndex();
    }

//    @RequestMapping(value = "/activityUpdate", method = RequestMethod.POST)
//    public String updateActivity(@ModelAttribute Activity activity, Model model) {
//        model.addAttribute("activity", activity); // TODO: what does this do?
//        activityDao.update(activity);
//        return "result";
//    }

}
