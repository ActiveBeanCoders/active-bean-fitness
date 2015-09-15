package com.activebeancoders.fitness.entity.es.mixin;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.dao.es.util.EsJsonView;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public abstract class ActivityMixin extends Activity {

    public static final String _userId = "userId";
    public static final String _activity = "activity";
    public static final String _date = "date";
    public static final String _unit = "unit";
    public static final String _distance = "distance";
    public static final String _comment = "comment";
    public static final String _distHour = "distHour";
    public static final String _distMin = "distMin";
    public static final String _distSec = "distSec";

    @JsonView({ EsJsonView.All.class })
    public abstract Long getUserId();

    @JsonView({ EsJsonView.All.class })
    public abstract String getActivity();

    @JsonView({ EsJsonView.All.class })
    public abstract Date getDate();

    @JsonView({ EsJsonView.All.class })
    public abstract String getUnit();

    @JsonView({ EsJsonView.All.class })
    public abstract Double getDistance();

    @JsonView({ EsJsonView.All.class, EsJsonView.Comment.class })
    public abstract String getComment();

    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistHour();

    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistMin();

    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistSec();

}
