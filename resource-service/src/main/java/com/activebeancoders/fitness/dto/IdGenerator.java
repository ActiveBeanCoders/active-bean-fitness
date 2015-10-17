package com.activebeancoders.fitness.dto;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    @GuardedBy("idGeneratorLock")
    private AtomicLong nextId;

    @Autowired
    private ActivityDto activityDto;

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
        Long maxId = activityDto.findMaxId(); // TODO: ignore failures as long as 1 succeeds?
        nextId = new AtomicLong(maxId + 1);
    }

}
