package com.activebeancoders.fitness.dao;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;

import java.util.List;

public interface IActivityDao {

    public Activity get(Object id);
    public boolean save(Activity activity);
    public boolean update(Activity activity, Class<?> jsonView);
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria);
    public List<Activity> findMostRecentActivities(int size);

}

