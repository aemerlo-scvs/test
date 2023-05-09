package com.scfg.core.adapter.web;

public interface CoverageEndPoint {
    String BASE = "/coverage";
    String SAVE = "/save";
    String UPDATE = "/update";
    String DELETE = "/delete/{coverageId}";

    String GETALL = "/all";
    String FILTER = "/filter";
    String BASE_PARAM_PRODUCT = "/product";
    String BASE_PARAM_COVERAGE_POLICY_ITEM = "/coveragePolicyItem";
}
