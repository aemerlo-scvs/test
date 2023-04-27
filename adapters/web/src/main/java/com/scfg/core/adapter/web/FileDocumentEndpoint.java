package com.scfg.core.adapter.web;

public interface FileDocumentEndpoint {
    String BASE = "/document";
    String GET_DOCUMENT = "/unsigned/{requestId}";
    String GET_SIGNED_DOCUMENT = "/signed/{requestId}";
    String GET_CERTIFICATE_COVERAGE_DOCUMENT = "/certificateCoverage/{policyItemId}";
    String REGISTER_DOCUMENT = "/register/{requestId}";
}
