package com.activebeancoders.fitness.data.hib;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.spring.util.Http;
import com.activebeancoders.fitness.spring.util.header.AuthcTokenHttpHeader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dan Barrese
 */
public class RestTemplateTestIT extends BaseTestIT {

    @Autowired
    @Qualifier("securityServiceBaseUrl")
    private String securityServiceBaseUrl;

    @Test
    public void restcall() throws Exception {
        ResponseEntity<AuthenticationWithToken> responseEntity = Http.post(securityServiceBaseUrl + "/public/token/verify",
                AuthenticationWithToken.class, new AuthcTokenHttpHeader("28dbc4c8-48fd-4d25-8ed9-55a4924f202d"));
        AuthenticationWithToken auth = responseEntity.getBody();
        System.out.println(auth);
    }

    public static class Userr {
        public String username;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Userr{");
            sb.append("username='").append(username).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}

