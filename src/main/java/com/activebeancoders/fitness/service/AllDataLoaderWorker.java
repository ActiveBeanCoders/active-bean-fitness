package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.domain.Action;
import com.activebeancoders.fitness.domain.enumeration.ActionType;
import com.activebeancoders.fitness.repository.ActionRepository;
import com.google.common.collect.ImmutableMap;
import net.pladform.random.IdAwareObjectGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Dan Barrese
 */
public class AllDataLoaderWorker implements DataLoaderWorker {

    private static final Logger log = LoggerFactory.getLogger(AllDataLoaderWorker.class);

    private Map<String, DataLoader> dataLoaderMap;

    @Autowired
    private ActionRepository actionRepo;

    private String lastKnownStatus = "Inactive.";

    public AllDataLoaderWorker(Map<String, DataLoader> dataLoaderMap) {
        this.dataLoaderMap = dataLoaderMap;
    }

    @PostConstruct
    protected void init() {
        if (dataLoaderMap == null || dataLoaderMap.isEmpty()) {
            throw new IllegalArgumentException("At least one data loaded must be present.");
        }
    }

    @Override
    public String getLastKnownStatus() {
        return lastKnownStatus;
    }

    @Override
    public void setLastKnownStatus(String lastKnownStatus) {
        this.lastKnownStatus = lastKnownStatus;
    }

    @Override
    public boolean beforeLoad() {
        boolean allSucceeded = true;
        for (Map.Entry<String, DataLoader> entry : dataLoaderMap.entrySet()) {
            if (!entry.getValue().beforeLoad()) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    public boolean afterLoad() {
        boolean allSucceeded = true;
        for (Map.Entry<String, DataLoader> entry : dataLoaderMap.entrySet()) {
            if (!entry.getValue().afterLoad()) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    @Async
    public Future<Boolean> loadRandomRecords(long count) {
        try {
            beforeLoad();
            lastKnownStatus = "Loading...";
            indexABunchOfRandomData(Long.valueOf(count));
            log.info("Data reload complete.  Loaded " + count + " records.");
            lastKnownStatus = "Data reload complete.  Loaded " + count + " records.";
            return new AsyncResult<>(true);
        } catch (Exception e) {
            log.error("Failed to reload data.", e);
            lastKnownStatus = "Failed to reload data";
            return new AsyncResult<>(false);
        } finally {
            afterLoad();
        }
    }

    protected void indexABunchOfRandomData(long count) {
        long tenPercent = (long) (count * 0.10);
        if (tenPercent == 0) {
            tenPercent = 1;
        }
        final IdAwareObjectGenerator generator = new IdAwareObjectGenerator();
        for (long l = 0; l < count; l++) {
            Action action = generator.generate(Action.class, ImmutableMap.<String, Callable>builder()
                    .put("setUserId", () -> generator.randomLong(1, 500))
                    .put("setType", () -> generator.choose(ActionType.values()))
                    .put("setUnit", () -> generator.choose(new String[]{"Miles", "Kilometers", "Steps", "Laps"}))
                    .put("setDistance", () -> generator.randomDouble(1.0, 100.0))
                    .put("setDuration", () -> generator.randomInt(0, 300))
                    .put("setDate", () -> generator.randomDate("2000-01-01 00:00:00"))
                    .put("setComment", () -> generator.words(generator.randomInt(10, 100)))
                    .build());
            actionRepo.save(action);
            if (l % tenPercent == 0) {
                log.debug("Indexed {} objects so far.", l);
            }
        }
        log.info("Done indexing random data.");
    }

}

