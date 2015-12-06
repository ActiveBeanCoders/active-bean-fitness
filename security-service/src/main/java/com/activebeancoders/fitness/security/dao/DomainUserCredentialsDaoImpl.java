package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.domain.DomainUserCredentials;
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
public class DomainUserCredentialsDaoImpl implements DomainUserCredentialsDao {

    private static final Logger log = LoggerFactory.getLogger(DomainUserCredentialsDaoImpl.class);
    private SessionFactory sessionFactory;
    //TODO: have security data in separate mysql server?

    @Autowired
    public DomainUserCredentialsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    @Transactional
    public void save(DomainUserCredentials domainUserCredentials) {
        Session session = sessionFactory.getCurrentSession();
        session.save(domainUserCredentials);
    }

    @Transactional
    public DomainUserCredentials findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery(DomainUserCredentials.QUERY_FIND_BY_USERNAME);
        query.setString("username", username);
        return (DomainUserCredentials) query.uniqueResult();
    }

}

