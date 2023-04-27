package com.scfg.core.adapter.web;

public interface QuestionnaireEndPoint {

    String BASE = "/questionnaire";
    String PARAM_ID = "/{questionnaireId}";
    String PARAM_IDENTIFICATION = "/{identificationNumber}/{documentType}";
    String PARAM_QUESTIONNAIRE_CLF = "/clf" + PARAM_ID;
}
