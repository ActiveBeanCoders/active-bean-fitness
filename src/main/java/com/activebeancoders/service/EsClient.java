package com.activebeancoders.service;

import com.activebeancoders.entity.Activity;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class EsClient extends TransportClient {

    private static String esHostname = "localhost";
    private static int esPort = 9300;
    private static Logger log = LoggerFactory.getLogger(EsClient.class);

    public EsClient() {
        addTransportAddress(new InetSocketTransportAddress(esHostname, esPort));
    }

    @PostConstruct
    private void init() {
        try {
            if (!admin().indices().prepareExists(Activity.class.getPackage().getName()).execute().actionGet().isExists()) {
                admin().indices().prepareCreate(Activity.class.getPackage().getName()).execute().actionGet();
            }
        } catch (NoNodeAvailableException e) {
            log.warn("Could not connect to Elasticsearch.  Is it running?");
        }
    }

}
