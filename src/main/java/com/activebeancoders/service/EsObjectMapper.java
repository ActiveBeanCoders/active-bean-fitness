package com.activebeancoders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@Scope("singleton")
public class EsObjectMapper extends ObjectMapper {

    public EsObjectMapper() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setDateFormat(sdf);
    }

}
