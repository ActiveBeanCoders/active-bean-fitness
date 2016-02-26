package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.util.SecurityContextAwareExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * Allows asynchronous remote method calls by propagating the session context to the
 * asynchronous thread.
 *
 * @author Dan Barrese
 */
@Configuration
@EnableAsync
public class AsyncRemoteMethodCallConfig implements AsyncConfigurer {

    @Value("${async.core-pool-size}") private Integer corePoolSize;
    @Value("${async.max-pool-size}") private Integer maxPoolSize;
    @Value("${async.queue-capacity}") private Integer queueCapacity;
    @Value("${async.thread-name-prefix}") private String threadNamePrefix;

    @PostConstruct
    protected void init() {
        Assert.notNull(corePoolSize);
        Assert.notNull(maxPoolSize);
        Assert.notNull(queueCapacity);
        Assert.isTrue(!(threadNamePrefix.startsWith("${")));
        Assert.isTrue(!(threadNamePrefix.endsWith("}")));
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new SecurityContextAwareExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}

