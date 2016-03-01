package com.activebeancoders.fitness.data.dao;

import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author Dan Barrese
 */
public class ActivityDaoImpl extends RotatableDao<ActivityDao> implements ActivityDao {

    private static final Logger log = LoggerFactory.getLogger(ActivityDaoImpl.class);

    @Autowired
    @Lazy
    private IdGenerator idGenerator;

    public ActivityDaoImpl(List<ActivityDao> daos) {
        super(daos);
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    @Override
    public Activity get(Object id) {
        return getPrimaryDao().get(id);
    }

    @Override
    public boolean save(Activity activity) {
        // Setting the ID here in the abstracted DAO because at some point each
        // DAO's save method will be asynchronous, and then the same object could
        // end up with different IDs in different data sources.
        if (activity.getId() == null) {
            activity.setId(idGenerator.getNextId());
        }

        Exception exception = null;

        // do work with primary DAO.
        int i = primaryDaoIndex;
        boolean allSucceeded;
        try {
            allSucceeded = daos.get(i).save(activity);
        } catch (Exception e) {
            allSucceeded = false;
            exception = e;
        }

        // do work with all other DAO's.
        i++;
        for (int n = 1; n < daos.size(); n++, i++) {
            if (i >= daos.size()) {
                i -= daos.size();
            }
            try {
                if (!daos.get(i).save(activity)) {
                    allSucceeded = false;
                }
            } catch (Exception e) {
                log.error("Non-primary DAO '{}' could not save activity with id '{}'.  Message: {}", daos.get(i), activity.getId(), e.getMessage());
            }
        }

        if (exception != null) {
            throw new RuntimeException("Could not save activity with id '" + activity.getId() + "'.", exception);
        }
        return allSucceeded;
    }

    // TODO: should not accept jsonview?
    @Override
    public boolean update(Activity activity, Class<?> jsonView) {
        boolean allSucceeded;
        Exception exception = null;

        // do work with primary DAO.
        int i = primaryDaoIndex;
        try {
            allSucceeded = daos.get(i).update(activity, jsonView);
        } catch (Exception e) {
            allSucceeded = false;
            exception = e;
        }

        // do work with all other DAO's.
        i++;
        for (int n = 1; n < daos.size(); n++, i++) {
            if (i >= daos.size()) {
                i -= daos.size();
            }
            try {
                if (!daos.get(i).update(activity, jsonView)) {
                    allSucceeded = false;
                }
            } catch (Exception e) {
                log.error("Non-primary DAO '{}' could not update activity with id '{}'.  Message: {}", daos.get(i), activity.getId(), e.getMessage());
            }
        }

        if (exception != null) {
            throw new RuntimeException("Could not update activity with id '" + activity.getId() + "'.", exception);
        }
        return allSucceeded;
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        return getPrimaryDao().search(activitySearchCriteria);
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        return getPrimaryDao().findMostRecentActivities(size);
    }

    @Override
    public Long findMaxId() {
        Long maxId = -1L;
        for (ActivityDao dao : daos) {
            try {
                Long possibleMax = dao.findMaxId();
                if (possibleMax > maxId) {
                    maxId = possibleMax;
                }
            } catch (Exception e) {
                log.warn("Could not find max ID from activity DAO '{}'.", dao, e);
            }
        }
        if (maxId == -1L) {
            throw new RuntimeException("Could not find max ID from any data source!");
        }
        return maxId;
    }

}

