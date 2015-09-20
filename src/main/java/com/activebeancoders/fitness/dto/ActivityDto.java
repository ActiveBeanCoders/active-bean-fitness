package com.activebeancoders.fitness.dto;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDto implements IActivityDto {

    public static String primaryActivityDtoName;

    private static final Logger log = LoggerFactory.getLogger(ActivityDto.class);
    private Map<String, IActivityDto> daos;

    @Autowired
    @Qualifier("activityEsDto")
    private IActivityDto activityEsDto;

    @Autowired
    @Qualifier("activityHibDto")
    private IActivityDto activityHibDto;

    @Autowired
    @Lazy
    private IdGenerator idGenerator;

    public ActivityDto(String primaryActivityDtoName, Map<String, IActivityDto> daos) {
        this.daos = daos;
        ActivityDto.setPrimaryActivityDtoName(primaryActivityDtoName);
    }

    // static methods
    // ````````````````````````````````````````````````````````````````````````

    public static void setPrimaryActivityDtoName(String primaryActivityDtoName) {
        ActivityDto.primaryActivityDtoName = primaryActivityDtoName;
    }

    // public methods
    // ````````````````````````````````````````````````````````````````````````

    // TODO: provide mechanism to change primary dto.

    @Override
    public Activity get(Object id) {
        return getPrimaryActivityDto().get(id);
    }

    @Override
    public boolean save(Activity activity) {
        // Setting the ID here in the abstracted DTO because at some point each
        // DTO's save method will be asynchronous, and then the same object could
        // end up with different IDs in different data sources.
        if (activity.getId() == null) {
            activity.setId(idGenerator.getNextId());
        }

        boolean allSucceeded = getPrimaryActivityDto().save(activity);
        for (Map.Entry<String, IActivityDto> entry : daos.entrySet()) {
            boolean thisDtoIsPrimary = entry.getKey().equals(ActivityDto.primaryActivityDtoName);
            if (!thisDtoIsPrimary && !entry.getValue().save(activity)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    // TODO: should not accept jsonview?
    @Override
    public boolean update(Activity activity, Class<?> jsonView) {
        boolean allSucceeded = getPrimaryActivityDto().update(activity, jsonView);
        for (Map.Entry<String, IActivityDto> entry : daos.entrySet()) {
            if (!entry.getKey().equals(ActivityDto.primaryActivityDtoName)
                    && entry.getValue().update(activity, jsonView)) {
                allSucceeded = false;
            }
        }
        return allSucceeded;
    }

    @Override
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria) {
        return getPrimaryActivityDto().search(activitySearchCriteria);
    }

    @Override
    public List<Activity> findMostRecentActivities(int size) {
        return getPrimaryActivityDto().findMostRecentActivities(size);
    }

    @Override
    public Long findMaxId() {
        Long maxId = 0L;
        for (Map.Entry<String, IActivityDto> entry : daos.entrySet()) {
            Long possibleMax = entry.getValue().findMaxId();
            if (possibleMax > maxId) {
                maxId = possibleMax;
            }
        }
        return maxId;
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        if (daos == null || daos.isEmpty()) {
            throw new IllegalArgumentException("ActivityDto must have at least one platform-specific DTO inside of it.");
        }
        daos = new HashMap<>();
        daos.put("es", activityEsDto);
        daos.put("hib", activityHibDto);
        ActivityDto.setPrimaryActivityDtoName("es");
    }

    // private methods
    // ````````````````````````````````````````````````````````````````````````

    private IActivityDto getPrimaryActivityDto() {
        return daos.get(ActivityDto.primaryActivityDtoName);
    }

}

