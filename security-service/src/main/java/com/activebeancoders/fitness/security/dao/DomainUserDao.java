package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.domain.DomainUser;

/**
 * @author Dan Barrese
 */
public interface DomainUserDao {

    void save(DomainUser domainUser);

    DomainUser findByUsername(String username);

}

