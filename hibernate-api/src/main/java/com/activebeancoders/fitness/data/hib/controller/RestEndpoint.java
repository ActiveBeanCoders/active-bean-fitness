package com.activebeancoders.fitness.data.hib.controller;

/**
 * @author Dan Barrese
 */
public class RestEndpoint {

    public static final String ACTIVITY = "/activity";
    public static final String ACTIVITY_BY_ID = "/activity/{id}";
    public static final String ACTIVITY_LOG = "/activityLog";
    public static final String ACTIVITY_ADD = "/activity-add";
    public static final String ACTIVITY_SET_PRIMARY_DAO = "/activity-dao/set/{name}";
    public static final String ACTIVITY_GET_PRIMARY_DAO = "/activity-dao/get";
    public static final String RESOURCE = "/resource";
    public static final String RELOAD = "/reload";
    public static final String RELOAD_STATUS = "/reload-status";
    public static final String SEARCH = "/search";

}
