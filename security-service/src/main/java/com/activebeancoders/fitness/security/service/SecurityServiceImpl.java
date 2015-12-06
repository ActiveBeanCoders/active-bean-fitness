package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.api.SecurityService;
import com.activebeancoders.fitness.security.dao.DomainUserCredentialsDao;
import com.activebeancoders.fitness.security.dao.DomainUserDao;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author Dan Barrese
 */
@Primary
@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);
    private DomainUserDao domainUserDao;
    private DomainUserCredentialsDao domainUserCredentialsDao;

    @Autowired
    public SecurityServiceImpl(DomainUserDao domainUserDao, DomainUserCredentialsDao domainUserCredentialsDao) {
        this.domainUserDao = domainUserDao;
        this.domainUserCredentialsDao = domainUserCredentialsDao;
    }

    @Override
    public DomainUser createUserAccount(DomainUserCredentials domainUserCredentials) {
        if (log.isInfoEnabled()) {
            log.info("Creating user with username '{}'", domainUserCredentials.getUsername());
        }
        domainUserCredentialsDao.save(domainUserCredentials);
        DomainUser domainUser = new DomainUser();
        domainUser.setUsername(domainUserCredentials.getUsername());
        domainUser.setSpaceDelimitedRoles("ROLE_DOMAIN_USER");
        domainUserDao.save(domainUser);
        return domainUserDao.findByUsername(domainUserCredentials.getUsername());
    }

}

