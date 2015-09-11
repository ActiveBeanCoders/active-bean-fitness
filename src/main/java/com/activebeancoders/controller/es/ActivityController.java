package com.activebeancoders.controller.es;

import com.activebeancoders.controller.RestEndpoint;
import com.activebeancoders.dao.es.ActivityRepository;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityRepository activityRepository;

    @RequestMapping(value = RestEndpoint.ACTIVITY_BY_ID, method = RequestMethod.GET)
    public Activity get(@PathVariable("id") String id) {
        return activityRepository.findOne(id);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    public void addActivity(@RequestBody Activity activity) {
        activityRepository.save(activity);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    // TODO: try Integer request param
    public Page<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        PageRequest pageRequest = new PageRequest(0, Integer.valueOf(count));
        return activityRepository.findTopNOrderByDateDesc(pageRequest);
    }

    @RequestMapping(value = RestEndpoint.SEARCH, method = RequestMethod.POST)
    public List<Activity> search(@RequestBody ActivitySearchCriteria activitySearchCriteria) {
        throw new UnsupportedOperationException();
    }

}
