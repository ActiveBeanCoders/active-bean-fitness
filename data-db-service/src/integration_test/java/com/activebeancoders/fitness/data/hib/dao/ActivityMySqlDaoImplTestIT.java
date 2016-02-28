package com.activebeancoders.fitness.data.hib.dao;

import com.activebeancoders.fitness.data.hib.BaseTestIT;
import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author Dan Barrese
 */
public class ActivityMySqlDaoImplTestIT extends BaseTestIT {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("activityMySqlDao")
    public ActivityDao activityMySqlDao;

    @Autowired
    SessionFactory sessionFactory;

    @Test
    public void save() throws Exception {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setUserId(1L);
        activity.setComment("hello 1 12 123 1234");
        activity.setDate(new Date());
        activityMySqlDao.save(activity);
    }

    @Test
    public void get() throws Exception {
        Activity activity = activityMySqlDao.get(1L);
        Assert.assertNotNull(activity);
        Assert.assertNotNull(activity.getId());
        log.info(activity.toString());
    }

    @Test
    @Transactional
    public void createIndex() throws Exception {
        save();
        sessionFactory.getCurrentSession().getNamedQuery(Activity.EXEC_ADD_FULLTEXT_INDEX).executeUpdate();
    }

    @Test
    public void search() throws Exception {
        ActivitySearchCriteria crit = new ActivitySearchCriteria();
        crit.setFullText("123");
        List<Activity> results = activityMySqlDao.search(crit);
        log.info(results.toString());
    }

}
