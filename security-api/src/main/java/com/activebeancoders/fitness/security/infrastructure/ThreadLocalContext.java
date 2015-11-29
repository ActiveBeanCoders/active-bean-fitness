package com.activebeancoders.fitness.security.infrastructure;

import com.google.common.base.Strings;
import org.slf4j.MDC;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Dan Barrese
 */
public class ThreadLocalContext {

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";

    /**
     * Adds the user's session context to the security context of the thread which calls
     * this method.
     *
     * @param authentication The authentication object that represents a user's active
     *                       session.
     */
    public static void addSessionContextToSecurityContext(Authentication authentication) {
        if (authentication != null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }
    }

    /**
     * Removes the user's session context from the security context of the thread which
     * calls this method.
     *
     * @param authentication The authentication object that represents a user's active
     *                       session.
     */
    public static void removeSessionContextFromSecurityContext(Authentication authentication) {
        SecurityContextHolder.clearContext();
    }

    /**
     * Removes the user's session context from the logging context of the thread which
     * calls this method.
     *
     * @param authentication The authentication object that represents a user's active
     *                       session.
     */
    public static void removeSessionContextFromLogging(Authentication authentication) {
        MDC.remove(TOKEN_SESSION_KEY);
        MDC.remove(USER_SESSION_KEY);
    }

    /**
     * Adds the user's session context to the logging context of the thread which calls
     * this method.
     *
     * @param authentication The authentication object that represents a user's active
     *                       session.
     */
    public static void addSessionContextToLogging(AuthenticationWithToken authentication) {
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getToken())) {
            // TODO: SHA-1 ok?
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            // TODO: redo salt?
            tokenValue = encoder.encodePassword(authentication.getToken(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getUsername())) {
            userValue = authentication.getUsername();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }

}
