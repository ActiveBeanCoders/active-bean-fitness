package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.RootObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.pladform.elasticsearch.entity.IdAware;
import net.pladform.elasticsearch.service.EsObjectMapper;
import net.pladform.elasticsearch.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private EsService esService;

    @Autowired
    private EsObjectMapper mapper;

    public <T extends IdAware> List<T> loadDataFromJsonFile(String pathRelativeToRootObject, Class<T> clazz) throws IOException {
        String fileContents = readFileContents(pathRelativeToRootObject);
        List<T> activities = new ArrayList<>();
        if (fileContents != null) {
            JsonNode node = mapper.readTree(fileContents);
            for (Iterator<JsonNode> iter = node.elements(); iter.hasNext(); ) {
                T t = esService.toObject(iter.next().toString(), clazz);
                activities.add(t);
            }
        }
        return activities;
    }

    public String readFileContents(String pathRelativeToRootObject) {
        try {
            InputStream fis = RootObject.class.getResourceAsStream(pathRelativeToRootObject);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuffer fileContents = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                fileContents.append(line);
                line = reader.readLine();
            }
            return fileContents.toString();
        } catch (IOException e) {
            log.error("Could not read file contents.", e);
            return null;
        }
    }

}
