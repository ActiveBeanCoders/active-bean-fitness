package com.activebeancoders.fitness.security.config;

import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Properties;

/**
 * Responsible for setting up HTTPS in Tomcat.
 *
 * @author Dan Barrese
 */
@Configuration
public class TomcatHttpsConfiguration {

    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer(
            @Value("${server.port}") final String serverPort,
            @Value("${zserver.ssl.key-store}") String keyStoreFile,
            @Value("${zserver.ssl.key-store-password}") String keyStorePassword,
            @Value("${zserver.ssl.keyStoreType}") String keyStoreType,
            @Value("${zserver.ssl.keyAlias}") String keyAlias,
            @Value("${zserver.ssl.trust-store}") String trustStoreFile,
            @Value("${zserver.ssl.trust-store-password}") String trustStorePassword,
            @Value("${zserver.ssl.trustStoreType}") String trustStoreType)
            throws Exception {

        // This is boiler plate code to setup https on embedded Tomcat
        // with Spring Boot:

        Properties properties = System.getProperties();
        properties.put("javax.net.ssl.keyStore", keyStoreFile);
        properties.put("javax.net.ssl.keyStorePassword", keyStorePassword);
        properties.put("javax.net.ssl.keyStoreType", keyStoreType);
        properties.put("javax.net.ssl.keyAlias", keyAlias);
        properties.put("javax.net.ssl.trustStore", trustStoreFile);
        properties.put("javax.net.ssl.trustStorePassword", trustStorePassword);
        properties.put("javax.net.ssl.trustStoreType", trustStoreType);

        final String absoluteKeyStoreFile = new File(keyStoreFile).getAbsolutePath();
        final String absoluteTrustStoreFile = new File(trustStoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(connector -> {
                    connector.setPort(Integer.parseInt(serverPort));
                    connector.setSecure(true);
                    connector.setScheme("https");

                    Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
                    proto.setSSLEnabled(true);

                    proto.setKeystoreFile(absoluteKeyStoreFile);
                    proto.setKeystorePass(keyStorePassword);
                    proto.setKeystoreType(keyStoreType);
                    proto.setTruststoreFile(absoluteTrustStoreFile);
                    proto.setTruststorePass(trustStorePassword);
                    proto.setTruststoreType(trustStoreType);
                    proto.setKeyAlias(keyAlias);
                });
            }
        };
    }
}

