package com.activebeancoders.fitness.dao;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDao implements IActivityDao {

    public static String primaryActivityDaoName;

    private static final Logger log = LoggerFactory.getLogger(ActivityDao.class);
    private Map<String, IActivityDao> daos;

    @Autowired
    @Qualifier("activityEsDao")
    private IActivityDao activityEsDao;

    @Autowired
    @Qualifier("activityHibDao")
    private IActivityDao activityHibDao;

    @Autowired
    @Lazy
    private IdGenerator idGenerator;

    public ActivityDao(String primaryActivityDaoName, Map<String, IActivityDao> daos) {
        this.daos = daos;
        ActivityDao.setPrimaryActivityDaoName(primaryActivityDaoName);
    }

    // static methods
    // ````````````````````````````````````````````````````````````````````````

    public static void setPrimaryActivityDaoName(String primaryActivityDaoName) {
        ActivityDao.primaryActivityDaoName = primaryActivityDaoName;
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    // TODO: provide mechanism to change primary dao.

    @Override
    public Activity get(Object id) {
        return getPrimaryActivityDao().get(id);
    }

    @Override
    public boolean save(Activity activity) {
        // Setting the ID here in the abstracted DTO because at some point each
        // DTO's save method will be asynchronous, and then the same object could
        // end up with different IDs in different data sources.
        if (activity.getId() == null) {
            activity.setId(idGenerator.getNextId());
        }

        boolean allSucceeded = getPrimaryActivityDao().save(activity);
        for (Map.Entry<String, IActivityDao> entry : daos.entrySet()) {
            boolean thisDtoIsPrimary = entry.getKey().equals(ActivityDao.primaryActivityDaoName);
            if (!thisDtoIsPrimary && !entry.getValue().save(activity)) {
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

    @Override
    public Long findMaxId() {
        Long maxId = 0L;
        for (Map.Entry<String, IActivityDao> entry : daos.entrySet()) {
            Long possibleMax = entry.getValue().findMaxId();
            if (possibleMax > maxId) {
                maxId = possibleMax;
            }
        }
        return maxId;
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        if (daos == null || daos.isEmpty()) {
            throw new IllegalArgumentException("ActivityDto must have at least one platform-specific DTO inside of it.");
        }
        daos = new HashMap<>();
        daos.put("es", activityEsDao);
        daos.put("hib", activityHibDao);
        ActivityDao.setPrimaryActivityDaoName("es");
    }

    // private methods
    // ````````````````````````````````````````````````````````````````````````

    private IActivityDao getPrimaryActivityDao() {
        return daos.get(ActivityDao.primaryActivityDaoName);
    }

}

