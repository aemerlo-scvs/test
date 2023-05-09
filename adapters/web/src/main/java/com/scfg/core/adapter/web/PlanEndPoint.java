package com.scfg.core.adapter.web;

public interface PlanEndPoint {
    String BASE = "/plan";
    String SAVE = "/save";
    String UPDATE = "/update";
    String DELETE = "/delete/{id}";
    String GETBYID = "/findById/{id}";
    String GETALL = "/all";
    String FILTER = "/filter";
    String CLF = "/clf";
    String GEL = "/gel";

    String GETPLANBYPRODUCTID = "/findPlanByProductI/{productId}";
    String GETPLANBYREQUESTID = "/findPlanByRequestId";

}
