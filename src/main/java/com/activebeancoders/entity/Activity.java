package com.activebeancoders.entity;

import java.util.Date;

public class Activity extends AbstractEsEntity {

    protected Long userId;
    protected String activity;
    protected Date date;
    protected String unit;
    protected Double distance;
    protected String comment;
    protected Long distHour;
    protected Long distMin;
    protected Long distSec;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDistHour() {
        return distHour;
    }

    public void setDistHour(Long distHour) {
        this.distHour = distHour;
    }

    public Long getDistMin() {
        return distMin;
    }

    public void setDistMin(Long distMin) {
        this.distMin = distMin;
    }

    public Long getDistSec() {
        return distSec;
    }

    public void setDistSec(Long distSec) {
        this.distSec = distSec;
    }
}
