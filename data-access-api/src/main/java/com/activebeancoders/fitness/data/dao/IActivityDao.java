package com.activebeancoders.fitness.data.dao;

import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;

import java.util.List;

/**
 * @author Dan Barrese
 */
public interface IActivityDao {

    public Activity get(Object id);

    public boolean save(Activity activity);

    public boolean update(Activity activity, Class<?> jsonView);

    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria);

    public List<Activity> findMostRecentActivities(int size);

    public Long findMaxId();

}

