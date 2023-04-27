package com.scfg.core.adapter.web;

public interface MenuEndPoint {

    String BASE = "/menu";
    String PARAM_DETAIL = "/detail";
    String PARAM_ID = "/{menuId}";
    String GET_BY_ROLEID = "/getByRoleId/{roleId}"; //Obtiene to_do el menu de acuerdo al role id
}
