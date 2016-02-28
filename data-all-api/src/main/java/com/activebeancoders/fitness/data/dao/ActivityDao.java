package com.activebeancoders.fitness.data.dao;

import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;

import java.util.List;

/**
 * @author Dan Barrese
 */
public interface ActivityDao {

    Activity get(Object id);

    boolean save(Activity activity);

    boolean update(Activity activity, Class<?> jsonView);

    List<Activity> search(ActivitySearchCriteria activitySearchCriteria);

    List<Activity> findMostRecentActivities(int size);

    Long findMaxId();

}

