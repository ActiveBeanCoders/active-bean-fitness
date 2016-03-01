package com.activebeancoders.fitness.data.dao;

import com.activebeancoders.fitness.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Dan Barrese
 */
public abstract class RotatableDao<T> {

    protected int primaryDaoIndex;
    protected List<T> daos;
    protected static final Object rotationLock = new Object();
    protected volatile int primaryDaoFailures;
    protected volatile long lastFailureTime;

    private final Logger log = LoggerFactory.getLogger(RotatableDao.class);


    /**
     * @param daos Ordered map of DAOs to use.  Key is name, value is the actual DAO.
     *             The order in which the DAO's appear in the map is the order in which
     *             they will be used in case of failure.
     */
    public RotatableDao(List<T> daos) {
        Assert.assertNotEmpty(daos);
        this.daos = daos;
        primaryDaoFailures = 0;
        lastFailureTime = 0L;
        primaryDaoIndex = 0;
    }

    // abstract methods
    // ````````````````````````````````````````````````````````````````````````

    /**
     * @return The number of milliseconds after which if no exception is thrown,
     * all previous exceptions by the primary DAO will be ignored.
     */
    protected long getMillisAfterWhichToDiscardFailures() {
        return 0; // i.e. do not rotate
    }

    /**
     * @return The number of times the primary DAO must fail before giving
     * up and moving on to next DAO.
     */
    protected int getFailureGiveupCount() {
        return Integer.MAX_VALUE;
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    public int getPrimaryDaoIndex() {
        return primaryDaoIndex;
    }

    public void setPrimaryDaoIndex(int primaryDaoIndex) {
        if (primaryDaoIndex >= daos.size()) {
            throw new IndexOutOfBoundsException(String.format("Supplied %d.  Expected [0, %d]", primaryDaoIndex, daos.size() - 1));
        }
        this.primaryDaoIndex = primaryDaoIndex;
    }

    public T getPrimaryDao() {
        return daos.get(primaryDaoIndex);
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        Assert.assertNotEmpty(daos, "DAO map must have at least one platform-specific DAO inside of it.");
    }

    // TODO: probably needs better synchronization to work correctly under heavy load with lots of errors. -dbarrese
    protected void handleError() {
        long now = System.currentTimeMillis();
        if (now - lastFailureTime > getMillisAfterWhichToDiscardFailures()) {
            primaryDaoFailures = 1;
        } else {
            primaryDaoFailures++;
        }
        lastFailureTime = now;
        if (primaryDaoFailures > getFailureGiveupCount()) {
            rotatePrimaryDao();
        }
    }

    /**
     * Find the active DAO name in the array of all available DAO names,
     * then choose the next one (or the first one if we're at the end of the list).
     */
    protected void rotatePrimaryDao() {
        synchronized (rotationLock) {
            T oldPrimary = daos.get(primaryDaoIndex);
            int newPrimaryIndex = primaryDaoIndex + 1;
            if (newPrimaryIndex >= daos.size()) {
                newPrimaryIndex = 0;
            }
            primaryDaoIndex = newPrimaryIndex;
            primaryDaoFailures = 0;
            lastFailureTime = 0L;
            if (log.isInfoEnabled()) {
                log.info("Rotated primary DAO from '{}' to '{}'.", oldPrimary, daos.get(newPrimaryIndex));
            }
        }
    }

}

