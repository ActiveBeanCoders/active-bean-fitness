package com.activebeancoders.fitness.data.dao;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Responsible for generating IDs taking into account multiple data sources.
 *
 * @author Dan Barrese
 */
public class IdGenerator {

    @GuardedBy("idGeneratorLock")
    private AtomicLong nextId;

    @Autowired
    private ActivityDaoImpl activityDao;

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
        Long maxId = activityDao.findMaxId(); // TODO: ignore failures as long as 1 succeeds?
        nextId = new AtomicLong(maxId + 1);
    }

}
