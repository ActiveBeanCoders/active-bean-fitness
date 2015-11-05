package com.activebeancoders.fitness.service.es;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.entity.es.mixin.ActivityMixin;
import net.pladform.elasticsearch.service.EsObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Jackson object mapper settings for this project.
 *
 * @author Dan Barrese
 */
@Component
@Scope("singleton")
public class FitnessObjectMapper extends EsObjectMapper {

    private static final Logger log = LoggerFactory.getLogger(FitnessObjectMapper.class);

    @Value("${elasticsearch.field.format.date}")
    private String dateFormat;

    @Override
    public Map<Class<?>, Class<?>> getMixinClasses() {
        Map<Class<?>, Class<?>> mixins = new HashMap<>();
        mixins.put(Activity.class, ActivityMixin.class);
        return mixins;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

}
