package com.activebeancoders.fitness.security.infrastructure;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Dan Barrese
 */
@Component
@Scope("singleton")
public class TokenService {

    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final Cache restApiAuthTokenCache = CacheManager.getInstance().getCache("restApiAuthTokenCache");

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        log.info("Evicting expired tokens");
        restApiAuthTokenCache.evictExpiredElements();
    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, AuthenticationWithToken authentication) {
        restApiAuthTokenCache.put(new Element(token, authentication));
    }

    public boolean contains(String token) {
        return restApiAuthTokenCache.get(token) != null;
    }

    public boolean remove(String token) {
        return restApiAuthTokenCache.remove(token);
    }

    public AuthenticationWithToken retrieve(String token) {
        return (AuthenticationWithToken) restApiAuthTokenCache.get(token).getObjectValue();
    }
}
