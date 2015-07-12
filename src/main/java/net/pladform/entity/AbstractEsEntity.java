package net.pladform.entity;

import net.pladform.service.EsService;

public class AbstractEsEntity {

    public String id;

    public AbstractEsEntity() {
    }

    public String toString() {
        return EsService.toJson(this);
    }

}
