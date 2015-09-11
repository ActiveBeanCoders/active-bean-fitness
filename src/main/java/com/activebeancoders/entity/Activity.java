package com.activebeancoders.entity;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static com.activebeancoders.entity.Activity.INDEX_NAME;
import static com.activebeancoders.entity.Activity.INDEX_TYPE;

@Document(indexName = INDEX_NAME, type = INDEX_TYPE, shards = 1, replicas = 0, refreshInterval = "1s")
public class Activity extends AbstractEsEntity {

    public static final String INDEX_NAME = "com.activebeancoders.entity";
    public static final String INDEX_TYPE = "Activity";

    @Field(type = FieldType.Long)
    protected Long userId;

    @Field(type = FieldType.String)
    protected String activity;

    @Field(type = FieldType.Date, format = DateFormat.ordinal_date_time)
    protected Date date;

    @Field(type = FieldType.String)
    protected String unit;

    @Field(type = FieldType.Double)
    protected Double distance;

    @Field(type = FieldType.String, indexAnalyzer = "standard")
    protected String comment;

    @Field(type = FieldType.Long)
    protected Long distHour;

    @Field(type = FieldType.Long)
    protected Long distMin;

    @Field(type = FieldType.Long)
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
