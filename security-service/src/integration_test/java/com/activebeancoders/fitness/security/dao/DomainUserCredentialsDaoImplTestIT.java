package com.activebeancoders.fitness.security.dao;

import com.activebeancoders.fitness.security.BaseTestIT;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DomainUserCredentialsDaoImplTestIT extends BaseTestIT {

    @Autowired
    DomainUserCredentialsDaoImpl domainUserCredentialsDaoImpl;

    @Test
    public void testSave() throws Exception {
        String username = "user";
        DomainUserCredentials domainUserCredentials = new DomainUserCredentials(username, "password");
        domainUserCredentialsDaoImpl.save(domainUserCredentials);
        DomainUserCredentials saved = domainUserCredentialsDaoImpl.findByUsername(username);
        Assert.assertNotNull(saved);
    }

}

