package com.activebeancoders.fitness.data.es.service.es;

import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.es.entity.mixin.ActivityMixin;
import net.pladform.elasticsearch.service.EsObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public DateFormat getDateFormat() {
        return new SimpleDateFormat(dateFormat);
    }

}
