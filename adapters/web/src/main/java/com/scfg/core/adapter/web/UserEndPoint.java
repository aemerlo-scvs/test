package com.scfg.core.adapter.web;

public interface UserEndPoint {
    String BASE = "/user";
    String PARAM_ID = "/{userId}";
    String PARAM_PAGE = "/{page}/{size}";
    String AUTH = "/auth";
    String AUTH_LDAP = "/auth/ldap";
    String AUTH_LDAP_AUTOMATIC = "/auth/ldap/automatic";
    String AUTH_VALIDATE = "/auth/validate";
    String CHANGE_PASSWORD = "/changePassword";
}
