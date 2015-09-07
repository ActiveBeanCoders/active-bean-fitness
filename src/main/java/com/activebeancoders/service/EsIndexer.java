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
import java.util.function.Function;

@Component
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
            Activity activity = generator.generate(Activity.class, ImmutableMap.<String, Function>builder()
                    .put("setId", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return String.valueOf(generator.nextId());
                        }
                    })
                    .put("setUserId", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomLong(1, 500);
                        }
                    })
                    .put("setActivity", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.choose(new String[]{"Running", "Cycling", "Hiking", "Swimming"});
                        }
                    })
                    .put("setUnit", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.choose(new String[]{"Miles", "Kilometers", "Steps", "Laps"});
                        }
                    })
                    .put("setDistance", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomDouble(1.0, 100.0);
                        }
                    })
                    .put("setDistHour", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomLong(0, 12);
                        }
                    })
                    .put("setDistMin", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomLong(1, 59);
                        }
                    })
                    .put("setDistSec", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomLong(0, 59);
                        }
                    })
                    .put("setDate", new Function() {
                        @Override
                        public Object apply(Object o) {
                            return generator.randomDate("2000-01-01 00:00:00");
                        }
                    })
                    .put("setComment", new Function() {
                        @Override
                        public Object apply(Object o) {
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
//                    .put("setId", o -> String.valueOf(generator.nextId()))
//                    .put("setUserId", o -> generator.randomLong(1, 500))
//                    .put("setActivity", o -> generator.choose(new String[]{ "Running", "Cycling", "Hiking", "Swimming" }))
//                    .put("setUnit", o -> generator.choose(new String[]{ "Miles", "Kilometers", "Steps", "Laps" }))
//                    .put("setDistance", o -> generator.randomDouble(1.0, 100.0))
//                    .put("setDistHour", o -> generator.randomLong(0, 12))
//                    .put("setDistMin", o -> generator.randomLong(1, 59))
//                    .put("setDistSec", o -> generator.randomLong(0, 59))
//                    .put("setDate", o -> generator.randomDate("2000-01-01 00:00:00"))
//                    .put("setComment", o -> generator.words(generator.randomInt(10, 100)))
//                    .build());
//            activityEsDao.save(activity);
//            if (l % tenPercent == 0) {
//                log.debug("Indexed {} objects so far.", l);
//            }
//        }
//        log.info("Done indexing random data.");
//    }

}
