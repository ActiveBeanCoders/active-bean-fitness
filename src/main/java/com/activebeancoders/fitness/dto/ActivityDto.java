package com.activebeancoders.fitness.dto;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import com.activebeancoders.fitness.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;

public class ActivityDto extends RotatableDto<IActivityDto> implements IActivityDto {

    private static final Logger log = LoggerFactory.getLogger(ActivityDto.class);

    @Value("${dto.failure.timeout.millis}")
    private long millisAfterWhichToDiscardFailures;

    @Value("${dto.failure.rotate}")
    private int failureGiveupCount;

    @Autowired
    @Lazy
    private IdGenerator idGenerator;

    public ActivityDto(List<IActivityDto> dtos) {
        super(dtos);
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    @Override
    public Activity get(Object id) {
        try {
            return getPrimaryDto().get(id);
        } catch (Exception e) {
            handleError();
            throw e;
        }
    }

    @Override
    public boolean save(Activity activity) {
        // Setting the ID here in the abstracted DTO because at some point each
        // DTO's save method will be asynchronous, and then the same object could
        // end up with different IDs in different data sources.
        if (activity.getId() == null) {
            activity.setId(idGenerator.getNextId());
        }

        Exception exception = null;

        // do work with primary DTO.
        int i = primaryDtoIndex;
        boolean allSucceeded;
        try {
            allSucceeded = dtos.get(i).save(activity);
        } catch (Exception e) {
            allSucceeded = false;
            exception = e;
            handleError();
        }

        // do work with all other DTO's.
        i++;
        for (int n = 1; n < dtos.size(); n++, i++) {
            if (i >= dtos.size()) {
                i -= dtos.size();
            }
            try {
                if (!dtos.get(i).save(activity)) {
                    allSucceeded = false;
                }
            } catch (Exception e) {
                log.error("Non-primary dto '{}' could not save activity with id '{}'.  Message: {}", dtos.get(i), activity.getId(), e.getMessage());
            }
        }

        if (exception != null) {
            throw new RuntimeException("Could not save activity with id '" + activity.getId() + "'.", exception);
        }
        return allSucceeded;
    }

    // TODO: should not accept jsonview?
    @Override
    public boolean update(Activity activity, Class<?> jsonView) {
        boolean allSucceeded;
        Exception exception = null;

        // do work with primary DTO.
        int i = primaryDtoIndex;
        try {
            allSucceeded = dtos.get(i).update(activity, jsonView);
        } catch (Exception e) {
            allSucceeded = false;
            exception = e;
            handleError();
        }

        // do work with all other DTO's.
        i++;
        for (int n = 1; n < dtos.size(); n++, i++) {
            if (i >= dtos.size()) {
                i -= dtos.size();
            }
            try {
                if (!dtos.get(i).update(activity, jsonView)) {
                    allSucceeded = false;
                }
            } catch (Exception e) {
                log.error("Non-primary dto '{}' could not update activity with id '{}'.  Message: {}", dtos.get(i), activity.getId(), e.getMessage());
            }
        }

        if (exception != null) {
            throw new RuntimeException("Could not update activity with id '" + activity.getId() + "'.", exception);
        }
        return allSucceeded;
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        try {
            return getPrimaryDto().search(activitySearchCriteria);
        } catch (Exception e) {
            handleError();
            throw e;
        }
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        try {
            return getPrimaryDto().findMostRecentActivities(size);
        } catch (Exception e) {
            handleError();
            throw e;
        }
    }

    @Override
    public Long findMaxId() {
        Long maxId = -1L;
        for (IActivityDto dto : dtos) {
            try {
                Long possibleMax = dto.findMaxId();
                if (possibleMax > maxId) {
                    maxId = possibleMax;
                }
            } catch (Exception e) {
                log.warn("Could not find max ID from activity dto '{}'.", dto);
            }
        }
        if (maxId == -1L) {
            throw new RuntimeException("Could not find max ID from any data source!");
        }
        return maxId;
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        super.init();
        Assert.assertTrue(millisAfterWhichToDiscardFailures > 0L);
        Assert.assertTrue(failureGiveupCount > 0L);
    }

    @Override
    protected long getMillisAfterWhichToDiscardFailures() {
        return millisAfterWhichToDiscardFailures;
    }

    @Override
    protected int getFailureGiveupCount() {
        return failureGiveupCount;
    }

    // private methods
    // ````````````````````````````````````````````````````````````````````````

}

