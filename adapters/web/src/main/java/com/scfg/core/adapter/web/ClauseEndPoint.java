package com.scfg.core.adapter.web;

public interface ClauseEndPoint {
    String BASE = "/clause";
    String SAVE = "/save";
    String UPDATE = "/update";
    String DELETE = "/delete/{id}";
    String GETBYID = "/findById/{id}";
    String GETALL = "/all";
    String GETALLBYPRODUCT = "/findClausesByProductId/{productId}";



}
