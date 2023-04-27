package com.scfg.core.adapter.web;

public interface PlanEndPoint {
    String BASE = "/plan";
    String CLF = "/clf";
    String GEL = "/gel";

    String GETPLANBYPRODUCTID = "/findPlanByProductI/{productId}";
    String GETPLANBYREQUESTID = "/findPlanByRequestId";

}
