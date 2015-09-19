package com.activebeancoders.fitness.dao;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    @GuardedBy("idGeneratorLock")
    private AtomicLong nextId;

    @Autowired
    private IActivityDao activityDao;

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    public Long getNextId() {
        if (nextId == null) {
            init();
        }
        return nextId.getAndIncrement();
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        Long maxId = activityDao.findMaxId();
        nextId = new AtomicLong(maxId + 1);
    }

}