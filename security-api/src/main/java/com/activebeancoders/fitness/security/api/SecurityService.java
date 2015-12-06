package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;

/**
 * @author Dan Barrese
 */
public interface SecurityService {

    DomainUser createUserAccount(DomainUserCredentials domainUser);

}
