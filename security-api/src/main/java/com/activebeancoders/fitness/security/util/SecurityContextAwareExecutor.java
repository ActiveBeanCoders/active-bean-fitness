package com.activebeancoders.fitness.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Dan Barrese
 */
public class SecurityContextAwareExecutor extends ThreadPoolTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(SecurityContextAwareExecutor.class);

    @Override
    public <T> Future<T> submit(final Callable<T> c) {
        final Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (log.isDebugEnabled()) {
            log.debug("Adding authentication '{}' to async thread local context.");
        }
        return super.submit(() -> {
            try {
                if (a != null) {
                    SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                    ctx.setAuthentication(a);
                    SecurityContextHolder.setContext(ctx);
                }
                return c.call();
            } catch (Exception e) {
                log.error("Error submitting security context aware callable.", e);
                return null;
            } finally {
                if (a != null) {
                    SecurityContextHolder.clearContext();
                }
            }
        });
    }

    @Override
    public void execute(final Runnable r) {
        final Authentication a = SecurityContextHolder.getContext().getAuthentication();
        super.execute(() -> {
            try {
                SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                ctx.setAuthentication(a);
                SecurityContextHolder.setContext(ctx);
                r.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
        });
    }

}

