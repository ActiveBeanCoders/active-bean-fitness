package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.domain.DomainUser;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Dan Barrese
 */
@Repository
public class DomainUserDaoImpl implements DomainUserDao {

    private static final Logger log = LoggerFactory.getLogger(DomainUserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public DomainUserDaoImpl() {
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    @Transactional
    public void save(DomainUser domainUser) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(domainUser);
    }

    @Transactional
    public DomainUser findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery(DomainUser.QUERY_FIND_BY_USERNAME);
        query.setString("username", username);
        return (DomainUser) query.uniqueResult();
    }

}

