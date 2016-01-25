package com.activebeancoders.fitness.data.service;

/**
 * @author Dan Barrese
 */
public interface DataLoader {

    /**
     * Always executes before indexing starts.
     *
     * @return True if the method completes without error.
     */
    public boolean beforeLoad();

    /**
     * Always executes when indexing is done, even if it fails to start or complete.
     *
     * @return True if the method completes without error.
     */
    public boolean afterLoad();

}
