package com.activebeancoders.fitness.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Dan Barrese
 */
@Document(collection = "activity")
public class Activity extends AbstractAuditingEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id protected String id;
    @Field("user_id") @NotNull protected Long userId;
    @Field("activity") protected String activity;
    @Field("date") protected Date date;
    @Field("unit") protected String unit;
    @Field("distance") protected Double distance;
    @Field("comment") @Size(max = 4000) protected String comment;
    @Field("dist_hour") protected Long distHour;
    @Field("dist_min") protected Long distMin;
    @Field("dist_sec") protected Long distSec;

    public Activity() {
        userId = -1L;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getDistHour() { return distHour; }
    public void setDistHour(Long distHour) { this.distHour = distHour; }

    public Long getDistMin() { return distMin; }
    public void setDistMin(Long distMin) { this.distMin = distMin; }

    public Long getDistSec() { return distSec; }
    public void setDistSec(Long distSec) { this.distSec = distSec; }

}
