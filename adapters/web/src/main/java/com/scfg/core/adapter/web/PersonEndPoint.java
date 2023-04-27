package com.scfg.core.adapter.web;

public interface PersonEndPoint {

    String BASE = "/person";
    String PARAM_ID = "/{personId}";
    String PARAM_IDENTIFICATION = "/{identificationNumber}/{documentType}";
    String PARAM_IDENTIFICATION_CLF = "/clf";
    String PARAM_IDENTIFICATION_CLF_BY_ID = "/clf/single";
    String PARAM_ASSIGNEDGROUPIDC = "/{assignedGroup}";
}
