package com.activebeancoders.fitness.controller;

import com.activebeancoders.fitness.domain.Activity;
import com.activebeancoders.fitness.repository.ActivityRepository;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Dan Barrese
 */
@RestController
// TODO: how is authz done now?
//@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityRepository activityRepo;

    @RequestMapping(value = RestEndpoint.ACTIVITY_BY_ID, method = RequestMethod.GET)
    public Activity get(@PathVariable("id") String id) {
        return activityRepo.findOne(id);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addActivity(@RequestBody Activity activity) {
        activityRepo.save(activity);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    public List<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        // TODO: implement
        throw new NotImplementedException("not implemented");
//        int size = 10;
//        try {
//            size = Integer.valueOf(count);
//        } catch (Exception e) {
//            log.warn("Count of `{}` cannot be converted to a number.  Defaulting to {}.", count, size);
//        }
//        return activityRepo.findMostRecentActivities(size);
    }

    @RequestMapping(value = RestEndpoint.SEARCH, method = RequestMethod.POST)
    public List<Activity> search(@RequestBody ActivitySearchCriteria activitySearchCriteria) {
        // TODO: implement
        throw new NotImplementedException("not implemented");
//        return activityRepo.search(activitySearchCriteria);
    }

}
