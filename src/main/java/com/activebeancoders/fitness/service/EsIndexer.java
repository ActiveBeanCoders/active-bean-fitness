package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.dao.es.ActivityEsDao;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import com.google.common.collect.ImmutableMap;
import net.pladform.random.IdAwareObjectGenerator;
import net.pladform.elasticsearch.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class EsIndexer {

    private static final Logger log = LoggerFactory.getLogger(EsIndexer.class);

    @Autowired
    private EsService esService;

    @Autowired
    private ActivityEsDao activityEsDao;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private ActivityIndexManager activityIndexManager;

    @Value("${elasticsearch.activity.refresh_interval}")
    private String refreshInterval;

    private String lastKnownStatus = "Inactive.";

    public String getLastKnownStatus() {
        return lastKnownStatus;
    }

    public void setLastKnownStatus(String lastKnownStatus) {
        this.lastKnownStatus = lastKnownStatus;
    }

    @Async
    public Future<Boolean> loadRandomRecords(long count) {
        try {
            lastKnownStatus = "Loading...";
            esService.setVerbose(false);
            activityIndexManager.rebuildIndex();
            esService.setRefreshInterval(ActivityEsDao.INDEX_NAME, "-1");
            indexABunchOfRandomData(Long.valueOf(count));
            log.info("Data reload complete.");
            lastKnownStatus = "Data reload complete.  Loaded " + count + " records.";
            return new AsyncResult<>(true);
        } catch (Exception e) {
            log.error("Failed to reload data.", e);
            lastKnownStatus = "Failed to reload data";
            return new AsyncResult<>(false);
        } finally {
            esService.setVerbose(true);
            activityIndexManager.resetRefreshInterval();
        }
    }

    protected void indexABunchOfRandomData(long count) {
        long tenPercent = (long) (count * 0.10);
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
            activityEsDao.save(activity);
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
//            activityEsDao.save(activity);
//            if (l % tenPercent == 0) {
//                log.debug("Indexed {} objects so far.", l);
//            }
//        }
//        log.info("Done indexing random data.");
//    }

}
