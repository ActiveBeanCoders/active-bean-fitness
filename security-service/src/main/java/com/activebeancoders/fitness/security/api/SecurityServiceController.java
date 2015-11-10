package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.api.SecurityClientController;

/**
 * @author Dan Barrese
 */
public abstract class SecurityServiceController extends SecurityClientController {

    public static final String STUFF_URL = API_PATH + "/stuff";

    // Spring Boot Actuator services
    public static final String AUTOCONFIG_ENDPOINT = "/autoconfig";
    public static final String BEANS_ENDPOINT = "/beans";
    public static final String CONFIGPROPS_ENDPOINT = "/configprops";
    public static final String ENV_ENDPOINT = "/env";
    public static final String MAPPINGS_ENDPOINT = "/mappings";
    public static final String METRICS_ENDPOINT = "/metrics";
    public static final String SHUTDOWN_ENDPOINT = "/shutdown";
}
