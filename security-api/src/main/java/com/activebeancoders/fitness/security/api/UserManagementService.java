package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;

/**
 * @author Dan Barrese
 */
public interface UserManagementService {

    DomainUser createUserAccount(DomainUserCredentials domainUser);

}
