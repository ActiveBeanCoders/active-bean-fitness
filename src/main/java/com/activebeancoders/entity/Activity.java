package com.activebeancoders.entity;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public class Activity extends AbstractEsEntity {

    public static final String _userId = "userId";
    public static final String _activity = "activity";
    public static final String _date = "date";
    public static final String _unit = "unit";
    public static final String _distance = "distance";
    public static final String _comment = "comment";
    public static final String _distHour = "dist_hour";
    public static final String _distMin = "dist_min";
    public static final String _distSec = "dist_sec";

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

    @JsonProperty(_distHour)
    @JsonView({ View.All.class })
    private Long distHour;

    @JsonProperty(_distMin)
    @JsonView({ View.All.class })
    private Long distMin;

    @JsonProperty(_distSec)
    @JsonView({ View.All.class })
    private Long distSec;

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
