package com.activebeancoders.service;

import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.entity.ActivityEs;
import com.google.common.collect.ImmutableMap;
import net.pladform.random.IdAwareObjectGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class EsIndexer {

    private static final Logger log = LoggerFactory.getLogger(EsIndexer.class);

    @Autowired
    private EsService esService;

    @Autowired
    private ActivityEsDao activityEsDao;

    @Autowired
    private DataLoader dataLoader;

    /**
     * Initializes all indexes.  Deletes all indexes, then rebuilds their structures without any data.
     */
    public void rebuildAllIndexStructures() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> type = new HashMap<>();

        type.put("type", "date");
        type.put("format", "yyyy-MM-dd HH:mm:SS");
        properties.put(ActivityEs._date, type);
        mapping.put("properties", properties);

        esService.buildIndex(Activity.class.getPackage().getName(), Activity.class.getSimpleName(), mapping);
    }

    /**
     * Populates index with all data found in a JSON file within the project.
     */
    public void indexAllData() {
        try {
            List<Activity> activities = dataLoader.loadDataFromJsonFile("activity-log.json", Activity.class);
            long id = 0;
            for (Activity a : activities) {
                a.setId(String.valueOf(++id));
                activityEsDao.save(a);
            }
        } catch (IOException e) {
            log.error("Error while indexing all data from source file.", e);
        }
    }

    public void indexABunchOfRandomData(long count) {
        long tenPercent = (long) (count * 0.10);
        final IdAwareObjectGenerator generator = new IdAwareObjectGenerator();
        for (long l = 0; l < count; l++) {
            Activity activity = generator.generate(Activity.class, ImmutableMap.<String, Callable>builder()
                    .put("setId", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return String.valueOf(generator.nextId());
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
