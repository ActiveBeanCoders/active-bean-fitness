package com.activebeancoders.fitness.data.service;

import java.util.concurrent.Future;

/**
 * @author Dan Barrese
 */
public interface DataLoaderWorker extends DataLoader {

    Future<Boolean> loadRandomRecords(long count);

    String getLastKnownStatus();

    void setLastKnownStatus(String lastKnownStatus);

}
