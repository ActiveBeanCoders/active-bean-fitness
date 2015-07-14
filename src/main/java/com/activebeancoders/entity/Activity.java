package com.activebeancoders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Activity extends AbstractEsEntity {

    private Long userId;
    private String activity;
    private Date date;
    private String unit;
    private Double distance;
    private String comment;

    @JsonProperty("dist_hour")
    private Integer distHour;

    @JsonProperty("dist_min")
    private Integer distMin;

    @JsonProperty("dist_sec")
    private Integer distSec;

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

    public Integer getDistHour() {
        return distHour;
    }

    public void setDistHour(Integer distHour) {
        this.distHour = distHour;
    }

    public Integer getDistMin() {
        return distMin;
    }

    public void setDistMin(Integer distMin) {
        this.distMin = distMin;
    }

    public Integer getDistSec() {
        return distSec;
    }

    public void setDistSec(Integer distSec) {
        this.distSec = distSec;
    }
}
