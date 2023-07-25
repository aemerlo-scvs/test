package com.scfg.core.adapter.web;

public interface ProductDocumentEndPoint {
    String BASE = "/productDocument";
    String SAVE = "/save";
    String UPDATE = "/update";
    String DELETE = "/delete/{id}";
    String GETBYID = "/findById/{id}";
    String GETALL = "/all";
}
