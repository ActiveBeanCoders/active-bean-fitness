package com.activebeancoders.entity;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public abstract class ActivityEs extends Activity {

    public static final String _userId = "userId";
    public static final String _activity = "activity";
    public static final String _date = "date";
    public static final String _unit = "unit";
    public static final String _distance = "distance";
    public static final String _comment = "comment";
    public static final String _distHour = "distHour";
    public static final String _distMin = "distMin";
    public static final String _distSec = "distSec";

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

    @JsonView({ View.All.class })
    public abstract Long getDistHour();

    @JsonView({ View.All.class })
    public abstract Long getDistMin();

    @JsonView({ View.All.class })
    public abstract Long getDistSec();

}
