package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.entity.Activity;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public class HibDataLoader implements DataLoader {

    private static final Logger log = LoggerFactory.getLogger(HibDataLoader.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean beforeLoad() {
        sessionFactory.getCurrentSession().getNamedQuery(Activity.EXEC_DELETE_ALL).executeUpdate();
        return true;
    }

    @Override
    public boolean afterLoad() {
        return true;
    }

}
