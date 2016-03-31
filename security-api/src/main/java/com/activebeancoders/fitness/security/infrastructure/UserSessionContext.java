package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.google.common.base.Strings;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Wrapper around Spring's SessionContextHolder, which holds the user's session info in a
 * ThreadLocal context.
 *
 * @author Dan Barrese
 */
public class UserSessionContext {

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";

    /**
     * @return the user's session info from the thread local context.  If no session
     * context exists yet, a dummy non-authenticated context will be returned.
     */
    public static UserSession get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return UserSession.nonAuthenticatedInstance();
        }
        return (UserSession) authentication;
    }

    /**
     * @return the currently logged in user.
     */
    public static DomainUser getUser() {
        return get().getPrincipal();
    }

    /**
     * @return the currently logged in user.
     */
    public static String getUsername() {
        UserSession session = get();
        if (session.isAuthenticated()) {
            return session.getPrincipal().getUsername();
        }
        return null;
    }

    /**
     * Sets the user's session info into the thread local context.
     *
     * @param authentication The authentication object that represents a user's active
     *                       session.
     */
    public static void set(UserSession authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            if (!Strings.isNullOrEmpty(authentication.getUsername())) {
                MDC.put(USER_SESSION_KEY, authentication.getUsername());
            }
        }
    }

    /**
     * Removes the user's session info from the thread local context.
     */
    public static void clear() {
        SecurityContextHolder.clearContext();
        MDC.remove(TOKEN_SESSION_KEY);
        MDC.remove(USER_SESSION_KEY);
    }

}

