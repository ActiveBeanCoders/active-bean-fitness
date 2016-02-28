package com.activebeancoders.fitness.data.controller;

/**
 * @author Dan Barrese
 */
public class RestEndpoint {

    public static final String ACTIVITY_BY_ID = "/api/activity/{id}";
    public static final String ACTIVITY_LOG = "/api/activity/recent/{count}";
    public static final String ACTIVITY_LOG_DEFAULT = "/api/activity/recent";
    public static final String ACTIVITY_ADD = "/api/activity/add";
    public static final String ACTIVITY_SEARCH = "/api/activity/search";
    public static final String ACTIVITY_SET_PRIMARY_DAO = "/activity-dao/set/{name}";
    public static final String ACTIVITY_GET_PRIMARY_DAO = "/activity-dao/get";
    public static final String RELOAD = "/api/activity/reload";
    public static final String RELOAD_STATUS = "/api/activity/reload/status";

}
