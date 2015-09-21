package com.activebeancoders.fitness.dto;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;

import java.util.List;

public interface IActivityDto {

    public Activity get(Object id);
    public boolean save(Activity activity);
    public boolean update(Activity activity, Class<?> jsonView);
    public List<Activity> search(ActivitySearchCriteria activitySearchCriteria);
    public List<Activity> findMostRecentActivities(int size);
    public Long findMaxId();
    public String getPrimaryActivityDtoName();
    public void setPrimaryActivityDtoName(String primaryActivityDtoName);

}

