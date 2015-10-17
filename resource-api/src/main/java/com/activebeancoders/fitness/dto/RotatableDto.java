package com.activebeancoders.fitness.dto;

import com.activebeancoders.fitness.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class RotatableDto<T> {

    protected int primaryDtoIndex;
    protected List<T> dtos;
    protected static final Object rotationLock = new Object();
    protected volatile int primaryDtoFailures;
    protected volatile long lastFailureTime;

    private final Logger log = LoggerFactory.getLogger(RotatableDto.class);


    /**
     * @param dtos Ordered map of DTOs to use.  Key is name, value is the actual DTO.
     *             The order in which the DTO's appear in the map is the order in which
     *             they will be used in case of failure.
     */
    public RotatableDto(List<T> dtos) {
        Assert.assertNotEmpty(dtos);
        this.dtos = dtos;
        primaryDtoFailures = 0;
        lastFailureTime = 0L;
        primaryDtoIndex = 0;
    }

    // abstract methods
    // ````````````````````````````````````````````````````````````````````````

    /**
     * @return The number of milliseconds after which if no exception is thrown,
     * all previous exceptions by the primary DTO will be ignored.
     */
    protected abstract long getMillisAfterWhichToDiscardFailures();

    /**
     * @return The number of times the primary DTO must fail before giving
     * up and moving on to next DTO.
     */
    protected abstract int getFailureGiveupCount();

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    public int getPrimaryDtoIndex() {
        return primaryDtoIndex;
    }

    public void setPrimaryDtoIndex(int primaryDtoIndex) {
        if (primaryDtoIndex >= dtos.size()) {
            throw new IndexOutOfBoundsException(String.format("Supplied %d.  Expected [0, %d]", primaryDtoIndex, dtos.size() - 1));
        }
        this.primaryDtoIndex = primaryDtoIndex;
    }

    public T getPrimaryDto() {
        return dtos.get(primaryDtoIndex);
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    protected void init() {
        Assert.assertNotEmpty(dtos, "DTO map must have at least one platform-specific DTO inside of it.");
    }

    // TODO: probably needs better synchronization to work correctly under heavy load with lots of errors. -dbarrese
    protected void handleError() {
        long now = System.currentTimeMillis();
        if (now - lastFailureTime > getMillisAfterWhichToDiscardFailures()) {
            primaryDtoFailures = 1;
        } else {
            primaryDtoFailures++;
        }
        lastFailureTime = now;
        if (primaryDtoFailures > getFailureGiveupCount()) {
            rotatePrimaryDto();
        }
    }

    /**
     * Find the active DTO name in the array of all available DTO names,
     * then choose the next one (or the first one if we're at the end of the list).
     */
    protected void rotatePrimaryDto() {
        synchronized (rotationLock) {
            T oldPrimary = dtos.get(primaryDtoIndex);
            int newPrimaryIndex = primaryDtoIndex + 1;
            if (newPrimaryIndex >= dtos.size()) {
                newPrimaryIndex = 0;
            }
            primaryDtoIndex = newPrimaryIndex;
            primaryDtoFailures = 0;
            lastFailureTime = 0L;
            if (log.isInfoEnabled()) {
                log.info("Rotated primary DTO from '{}' to '{}'.", oldPrimary, dtos.get(newPrimaryIndex));
            }
        }
    }

}

