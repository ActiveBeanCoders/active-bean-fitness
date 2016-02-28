package com.activebeancoders.fitness.data.hib.service;

import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.service.DataLoader;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Primary
public class DataLoaderMySqlImpl implements DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderMySqlImpl.class);

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
