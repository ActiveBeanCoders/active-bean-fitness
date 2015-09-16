package com.activebeancoders.fitness.dao;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

public class ActivityDao implements IActivityDao {

    private static final Logger log = LoggerFactory.getLogger(ActivityDao.class);

    public static String primaryActivityDaoName;
    private Map<String, IActivityDao> daos;

    public ActivityDao(String primaryActivityDaoName, Map<String, IActivityDao> daos) {
        ActivityDao.setPrimaryActivityDaoName(primaryActivityDaoName);
        this.daos = daos;
    }

    public static void setPrimaryActivityDaoName(String primaryActivityDaoName) {
        ActivityDao.primaryActivityDaoName = primaryActivityDaoName;
    }

    // TODO: provide mechanism to change primary dao.

    @PostConstruct
    protected void init() {
        // TODO: assert daos not null and size > 0.
    }

    @Override
    public Activity get(Object id) {
        return getPrimaryActivityDao().get(id);
    }

    @Override
    public boolean save(Activity activity) {
        boolean allSucceeded = getPrimaryActivityDao().save(activity);
        for (Map.Entry<String, IActivityDao> entry : daos.entrySet()) {
            if (!entry.getKey().equals(ActivityDao.primaryActivityDaoName)
                    && entry.getValue().save(activity)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    // TODO: should not accept jsonview?
    @Override
    public boolean update(Activity activity, Class<?> jsonView) {
        boolean allSucceeded = getPrimaryActivityDao().update(activity, jsonView);
        for (Map.Entry<String, IActivityDao> entry : daos.entrySet()) {
            if (!entry.getKey().equals(ActivityDao.primaryActivityDaoName)
                    && entry.getValue().update(activity, jsonView)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        return getPrimaryActivityDao().search(activitySearchCriteria);
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        return getPrimaryActivityDao().findMostRecentActivities(size);
    }

    private IActivityDao getPrimaryActivityDao() {
        return daos.get(ActivityDao.primaryActivityDaoName);
    }

}

