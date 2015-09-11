package com.activebeancoders.entity;

import org.springframework.data.annotation.Id;

public class AbstractEsEntity {

    @Id
    protected String id;

    public AbstractEsEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
