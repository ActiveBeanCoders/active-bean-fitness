package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.BaseTestIT;
import com.activebeancoders.fitness.security.domain.DomainUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Dan Barrese
 */
public class DomainUserDaoImplTestIT extends BaseTestIT {

    @Autowired
    DomainUserDaoImpl domainUserDaoImpl;

    @Test
    public void testSave() throws Exception {
        String username = "user";
        DomainUser domainUser = new DomainUser();
        domainUser.setUsername(username);
        domainUser.setNickname("yoyo");
        domainUser.setSpaceDelimitedRoles("ROLE_DOMAIN_USER");
        domainUserDaoImpl.save(domainUser);
        DomainUser saved = domainUserDaoImpl.findByUsername(username);
        Assert.assertNotNull(saved);
    }

}

