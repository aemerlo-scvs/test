package com.scfg.core.adapter.web;

public interface ProductEndPoint {
    String BASE = "/product";
    String SAVE = "/save";
    String UPDATE = "/update";
    String DELETE = "/delete/{productId}";
    String GETALL = "/all";
    String PARAMETERFILTER = "/filter";
    String BASE_PARAM_PLANS = "/plans/{agreementCode}";
    String GETALLBYBRANCH = "/getProductsByBranchId/{branchId}";
    String GETALLPRODUCTWITHBRANCH = "/productallbr";
}
