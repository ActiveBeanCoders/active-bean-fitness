package com.activebeancoders.entity;

import com.activebeancoders.service.EsService;

public class AbstractEsEntity {

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
