package com.activebeancoders.fitness.dao.hib;

import com.activebeancoders.fitness.dao.IActivityDao;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivityHibDao implements IActivityDao {

    private static final Logger log = LoggerFactory.getLogger(ActivityHibDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional
    public Activity get(Object id) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(Activity.QUERY_FIND_BY_ID);
        query.setParameter("id", id);
        Object result = query.uniqueResult();
        return result == null ? null : (Activity) result;
    }

    @Override
    @Transactional
    public boolean save(Activity activity) {
        sessionFactory.getCurrentSession().saveOrUpdate(activity);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Activity activity, Class<?> jsonView) {
        sessionFactory.getCurrentSession().update(activity);
        return true;
    }

    @Override
    @Transactional
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(Activity.QUERY_FULL_TEXT_SEARCH);
        query.setParameter("criteria", activitySearchCriteria.getFullText());
        List<Object> results = query.list();
        if (results == null) {
            return null;
        }
        List<Activity> typedResults = new ArrayList<>();
        for (Object o : results) {
            typedResults.add((Activity) o);
        }
        return typedResults;
    }

}

