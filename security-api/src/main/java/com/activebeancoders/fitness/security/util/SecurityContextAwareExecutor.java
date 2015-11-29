package com.activebeancoders.fitness.security.util;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.security.infrastructure.ThreadLocalContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Responsible for propagating the user's session context into an asynchronous method
 * call.  If a remote method is called in a new thread, that thread will not contain the
 * user's session info, which means the remote method call will be unauthenticated.  What
 * this class does is copy the user's session context from the calling thread to the
 * called thread.
 *
 * @author Dan Barrese
 */
public class SecurityContextAwareExecutor extends ThreadPoolTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(SecurityContextAwareExecutor.class);

    @Override
    public <T> Future<T> submit(final Callable<T> c) {
        final AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        if (log.isDebugEnabled()) {
            log.debug("Adding authentication '{}' to async thread local context.");
        }
        return super.submit(() -> {
            try {
                ThreadLocalContext.addSessionContextToSecurityContext(authentication);
                ThreadLocalContext.addSessionContextToLogging(authentication);
                return c.call();
            } catch (Exception e) {
                log.error("Error submitting security context aware callable.", e);
                return null;
            } finally {
                ThreadLocalContext.removeSessionContextFromSecurityContext(authentication);
                ThreadLocalContext.removeSessionContextFromLogging(authentication);
            }
        });
    }

    @Override
    public void execute(final Runnable r) {
        final AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        super.execute(() -> {
            try {
                ThreadLocalContext.addSessionContextToSecurityContext(authentication);
                ThreadLocalContext.addSessionContextToLogging(authentication);
                r.run();
            } finally {
                ThreadLocalContext.removeSessionContextFromSecurityContext(authentication);
                ThreadLocalContext.removeSessionContextFromLogging(authentication);
            }
        });
    }

}

