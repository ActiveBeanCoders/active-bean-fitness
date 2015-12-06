package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.domain.DomainUserCredentials;

/**
 * @author Dan Barrese
 */
public interface DomainUserCredentialsDao {

    void save(DomainUserCredentials domainUserCredentials);

    DomainUserCredentials findByUsername(String username);

}

