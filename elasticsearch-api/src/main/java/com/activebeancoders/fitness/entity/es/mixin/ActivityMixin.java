package com.activebeancoders.fitness.entity.es.mixin;

import com.activebeancoders.fitness.dto.es.util.EsJsonView;
import com.activebeancoders.fitness.entity.Activity;
import com.fasterxml.jackson.annotation.JsonView;
import net.pladform.elasticsearch.entity.util.RootEsJsonView;

import java.util.Date;

/**
 * Annotations for Jackson to convert this object to/from JSON.
 *
 * @author Dan Barrese
 */
public abstract class ActivityMixin extends Activity {

    public static final String _id = "id";
    public static final String _userId = "userId";
    public static final String _activity = "activity";
    public static final String _date = "date";
    public static final String _unit = "unit";
    public static final String _distance = "distance";
    public static final String _comment = "comment";
    public static final String _distHour = "distHour";
    public static final String _distMin = "distMin";
    public static final String _distSec = "distSec";

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract Long getId();

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract Long getUserId();

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract String getActivity();

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract Date getDate();

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract String getUnit();

    @Override
    @JsonView({ RootEsJsonView.All.class })
    public abstract Double getDistance();

    @Override
    @JsonView({ RootEsJsonView.All.class, EsJsonView.Comment.class })
    public abstract String getComment();

    @Override
    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistHour();

    @Override
    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistMin();

    @Override
    @JsonView({ EsJsonView.All.class })
    public abstract Long getDistSec();

}
