package com.activebeancoders.fitness.security.infrastructure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Dan Barrese
 */
public class GrantedAuthoritySerializer extends JsonDeserializer<Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        if (node == null) {
            return null;
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (int i = 0; i < node.size(); i++) {
            JsonNode n = node.get(i);
            authorities.add(new SimpleGrantedAuthority(node.get(i).get("authority").asText()));
        }
        return authorities;
    }

}

