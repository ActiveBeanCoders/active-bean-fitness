package com.activebeancoders.fitness.dao;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

public class ActivityDao implements IActivityDao {

    private static final Logger log = LoggerFactory.getLogger(ActivityDao.class);

    private IActivityDao primaryActivityDao;

    private List<IActivityDao> daos;

    public ActivityDao(List<IActivityDao> daos) {
        this.daos = daos;
    }

    @PostConstruct
    protected void init() {
        // TODO: assert daos not null and size > 0.
        primaryActivityDao = daos.get(0);
    }

    @Override
    public Activity get(Object id) {
        return primaryActivityDao.get(id);
    }

    @Override
    public boolean save(Activity activity) {
        boolean allSucceeded = true;
        for (IActivityDao dao : daos) {
            if (dao.save(activity)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    // TODO: should not accept jsonview
    public boolean update(Activity activity, Class<?> jsonView) {
        boolean allSucceeded = true;
        for (IActivityDao dao : daos) {
            if (dao.update(activity, jsonView)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        return primaryActivityDao.search(activitySearchCriteria);
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        return primaryActivityDao.findMostRecentActivities(size);
    }

}

