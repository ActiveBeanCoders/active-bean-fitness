package com.activebeancoders.fitness.spring.util;

import com.activebeancoders.fitness.spring.util.header.HttpHeader;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author Dan Barrese
 */
@SuppressWarnings("unused")
public class Http {

    public static <T> ResponseEntity<T> get(String url, Class<T> clazz, HttpHeader... headers) {
        return execute(new RestTemplate(), url, clazz, HttpMethod.GET, headers);
    }

    public static <T> ResponseEntity<T> post(String url, Class<T> clazz, HttpHeader... headers) {
        return execute(new RestTemplate(), url, clazz, HttpMethod.POST, headers);
    }

    public static <T> ResponseEntity<T> delete(String url, Class<T> clazz, HttpHeader... headers) {
        return execute(new RestTemplate(), url, clazz, HttpMethod.DELETE, headers);
    }

    protected static <T> ResponseEntity<T> execute(RestTemplate restTemplate, String url,
                                                Class<T> clazz, HttpMethod method,
                                                HttpHeader... headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        if (headers != null) {
            for (HttpHeader header : headers) {
                httpHeaders.set(header.name, header.value);
            }
        }
        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, entity, clazz);
        return responseEntity;
    }

}

