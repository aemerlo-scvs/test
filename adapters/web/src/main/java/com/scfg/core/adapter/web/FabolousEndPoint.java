package com.scfg.core.adapter.web;

import org.springframework.http.ResponseEntity;

import org.springframework.core.io.Resource;

public interface FabolousEndPoint {

    String FABOLOUS_BASE_ROUTE = "fabolous";

    String GET_ALL_UPLOAD = "/getall";

    String LIQUIDATION_DONWLOAD_REPORT = "/liquidation-report";

    String LIQUITION_DOWNLOAD_DUPLICATE_REPORT = "/liquidation-duplicate-report";

    String DELETE_UPLOAD = "/{deleteId}";

    String DONWLOAD_FORMAT = "/download-format";

    String SEARCH_CLIENT = "/search-client";

    ResponseEntity getAllUploads();

    ResponseEntity<Resource> downloadFormat();
}
