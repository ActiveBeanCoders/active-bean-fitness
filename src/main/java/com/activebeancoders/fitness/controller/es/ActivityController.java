package com.activebeancoders.fitness.controller.es;

import com.activebeancoders.fitness.controller.RestEndpoint;
import com.activebeancoders.fitness.dto.ActivityDto;
import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private IActivityDto activityDto;

    @RequestMapping(value = RestEndpoint.ACTIVITY_BY_ID, method = RequestMethod.GET)
    public Activity get(@PathVariable("id") String id) {
        return activityDto.get(id);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_ADD, method = RequestMethod.POST)
    public void addActivity(@RequestBody Activity activity) {
        activityDto.save(activity);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_LOG, method = RequestMethod.GET)
    public List<Activity> mostRecentActivities(@RequestParam(required = false, defaultValue = "10") String count) {
        int size = 10;
        try {
            size = Integer.valueOf(count);
        } catch (Exception e) {
            log.warn("Count of `{}` cannot be converted to a number.  Defaulting to {}.", count, size);
        }
        return activityDto.findMostRecentActivities(size);
    }

    @RequestMapping(value = RestEndpoint.SEARCH, method = RequestMethod.POST)
    public List<Activity> search(@RequestBody ActivitySearchCriteria activitySearchCriteria) {
        return activityDto.search(activitySearchCriteria);
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_GET_PRIMARY_DTO, method = RequestMethod.GET)
    public String getPrimaryDtoName() {
        return activityDto.getPrimaryActivityDtoName();
    }

    @RequestMapping(value = RestEndpoint.ACTIVITY_SET_PRIMARY_DTO, method = RequestMethod.GET)
    public String setPrimaryDtoName(@PathVariable("name") String primaryDtoName) {
        activityDto.setPrimaryActivityDtoName(primaryDtoName);
        return activityDto.getPrimaryActivityDtoName();
    }

//    @RequestMapping(value = "/activityUpdate", method = RequestMethod.POST)
//    public String updateActivity(@ModelAttribute Activity activity, Model model) {
//        model.addAttribute("activity", activity); // TODO: what does this do?
//        activityDto.update(activity);
//        return "result";
//    }

}
