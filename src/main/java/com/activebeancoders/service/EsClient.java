package com.activebeancoders.service;

import com.activebeancoders.entity.Activity;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("singleton")
public class EsClient extends TransportClient {

    private static String esHostname = "localhost";
    private static int esPort = 9300;

    public EsClient() {

        addTransportAddress(new InetSocketTransportAddress(esHostname, esPort));
    }

    @PostConstruct
    private void init() {
        if (!admin().indices().prepareExists(Activity.class.getPackage().getName()).execute().actionGet().isExists()) {
            admin().indices().prepareCreate(Activity.class.getPackage().getName()).execute().actionGet();
        }
    }

}
