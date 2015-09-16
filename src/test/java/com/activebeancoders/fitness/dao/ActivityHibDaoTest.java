package com.activebeancoders.fitness.dao;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
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

public class ActivityHibDaoTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("activityHibDao")
    public IActivityDao activityHibDao;

    @Test
    public void save() throws Exception {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setUserId(1L);
        activity.setComment("hello 1 12 123 1234");
        activity.setDate(new Date());
        activityHibDao.save(activity);
    }

    @Test
    public void get() throws Exception {
        Activity activity = activityHibDao.get(1L);
        Assert.assertNotNull(activity);
        Assert.assertNotNull(activity.getId());
        log.info(activity.toString());
    }

    @Autowired
    SessionFactory sessionFactory;

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
        List<Activity> results = activityHibDao.search(crit);
        log.info(results.toString());
    }

}
