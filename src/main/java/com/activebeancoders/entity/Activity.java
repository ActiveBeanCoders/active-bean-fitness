package com.activebeancoders.entity;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public class Activity extends AbstractEsEntity {

    @JsonView({ View.All.class })
    private Long userId;

    @JsonView({ View.All.class })
    private String activity;

    @JsonView({ View.All.class })
    private Date date;

    @JsonView({ View.All.class })
    private String unit;

    @JsonView({ View.All.class })
    private Double distance;

    @JsonView({ View.All.class, View.Comment.class })
    private String comment;

    @JsonProperty("dist_hour")
    @JsonView({ View.All.class })
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
