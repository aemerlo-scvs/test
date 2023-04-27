package com.scfg.core.adapter.web;

public interface PepEndpoint {
    String BASE = "/pep";
    String PARAM_PAGE = "/{page}/{size}";
    String PARAM_ID = "/{pepId}";
    String PARAM_SEARCH_KEY_WORD = "/search/{keyWord}";

    String PARAM_EXISTS= "/exists";
    String BASE_IMPORT = "/import";
}
