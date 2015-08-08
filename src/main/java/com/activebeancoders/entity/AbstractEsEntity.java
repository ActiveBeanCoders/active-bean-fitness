package com.activebeancoders.entity;

import com.activebeancoders.entity.util.View;
import com.fasterxml.jackson.annotation.JsonView;

public class AbstractEsEntity {

//    @JsonView({ View.All.class })
    private String id;

    public AbstractEsEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
