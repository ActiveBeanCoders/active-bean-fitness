package com.activebeancoders.entity;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public abstract class ActivityEs extends Activity {

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
    public abstract Long getUserId();

    @JsonView({ View.All.class })
    public abstract String getActivity();

    @JsonView({ View.All.class })
    public abstract Date getDate();

    @JsonView({ View.All.class })
    public abstract String getUnit();

    @JsonView({ View.All.class })
    public abstract Double getDistance();

    @JsonView({ View.All.class, View.Comment.class })
    public abstract String getComment();

    @JsonProperty(_distHour)
    @JsonView({ View.All.class })
    public abstract Long getDistHour();

    @JsonProperty(_distMin)
    @JsonView({ View.All.class })
    public abstract Long getDistMin();

    @JsonProperty(_distSec)
    @JsonView({ View.All.class })
    public abstract Long getDistSec();

}
