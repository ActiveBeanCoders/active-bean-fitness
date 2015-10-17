package com.activebeancoders.fitness.service;

import java.util.concurrent.Future;

public interface DataLoaderWorker extends DataLoader {

    public Future<Boolean> loadRandomRecords(long count);

    public String getLastKnownStatus();

    public void setLastKnownStatus(String lastKnownStatus);

}
