package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.entity.Activity;
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

public class AllDataLoaderWorker implements DataLoaderWorker {

    private static final Logger log = LoggerFactory.getLogger(AllDataLoaderWorker.class);

    private Map<String, DataLoader> dataLoaderMap;

    @Autowired
    private IActivityDto activityDto;

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
            log.info("Data reload complete.");
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
            Activity activity = generator.generate(Activity.class, ImmutableMap.<String, Callable>builder()
                    .put("setId", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.nextId();
                        }
                    })
                    .put("setUserId", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomLong(1, 500);
                        }
                    })
                    .put("setActivity", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.choose(new String[]{"Running", "Cycling", "Hiking", "Swimming"});
                        }
                    })
                    .put("setUnit", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.choose(new String[]{"Miles", "Kilometers", "Steps", "Laps"});
                        }
                    })
                    .put("setDistance", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomDouble(1.0, 100.0);
                        }
                    })
                    .put("setDistHour", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomLong(0, 12);
                        }
                    })
                    .put("setDistMin", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomLong(1, 59);
                        }
                    })
                    .put("setDistSec", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomLong(0, 59);
                        }
                    })
                    .put("setDate", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.randomDate("2000-01-01 00:00:00");
                        }
                    })
                    .put("setComment", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return generator.words(generator.randomInt(10, 100));
                        }
                    })
                    .build());
            activityDto.save(activity);
            if (l % tenPercent == 0) {
                log.debug("Indexed {} objects so far.", l);
            }
        }
        log.info("Done indexing random data.");
    }

//    public void indexABunchOfRandomData_JAVA8(long count) {
//        long tenPercent = (long) (count * 0.10);
//        IdAwareObjectGenerator generator = new IdAwareObjectGenerator();
//        for (long l = 0; l < count; l++) {
//            Activity activity = generator.generate(Activity.class, ImmutableMap.<String, Function>builder()
//                    .put("setId", () -> String.valueOf(generator.nextId()))
//                    .put("setUserId", () -> generator.randomLong(1, 500))
//                    .put("setActivity", () -> generator.choose(new String[]{ "Running", "Cycling", "Hiking", "Swimming" }))
//                    .put("setUnit", () -> generator.choose(new String[]{ "Miles", "Kilometers", "Steps", "Laps" }))
//                    .put("setDistance", () -> generator.randomDouble(1.0, 100.0))
//                    .put("setDistHour", () -> generator.randomLong(0, 12))
//                    .put("setDistMin", () -> generator.randomLong(1, 59))
//                    .put("setDistSec", () -> generator.randomLong(0, 59))
//                    .put("setDate", () -> generator.randomDate("2000-01-01 00:00:00"))
//                    .put("setComment", () -> generator.words(generator.randomInt(10, 100)))
//                    .build());
//            activityEsDto.save(activity);
//            if (l % tenPercent == 0) {
//                log.debug("Indexed {} objects so far.", l);
//            }
//        }
//        log.info("Done indexing random data.");
//    }

}
