package com.scfg.core.adapter.web;

public interface NotificationEndpoint {
    String BASE = "/notification";
    String PARAM_ID = "/{toUserId}";
    String PARAM_READ = "/read";
    String PARAM_NOTE = "/note";
}
