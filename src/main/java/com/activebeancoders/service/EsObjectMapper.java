package com.activebeancoders.service;

import com.activebeancoders.entity.Activity;
import com.activebeancoders.entity.ActivityEs;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.text.SimpleDateFormat;

public class EsObjectMapper extends ObjectMapper {

    public EsObjectMapper() {
        // TODO: SimpleDateFormat is not thread-safe.  Use joda-time class instead.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setDateFormat(sdf);
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        setSerializationInclusion(JsonInclude.Include.ALWAYS);
        disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        Module module = new ActiveBeanFitnessAppModule();
        registerModule(module);
    }

    public class ActiveBeanFitnessAppModule extends SimpleModule {

        public ActiveBeanFitnessAppModule() {
            super("ActiveBeanFitnessAppModule", new Version(0,0,1,null));
        }

        @Override
        public void setupModule(SetupContext context) {
            context.setMixInAnnotations(Activity.class, ActivityEs.class);
        }

    }

}
