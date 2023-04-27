package com.scfg.core.adapter.web;

public interface SMVSEndpoint {
    String BASE = "/smvs";
    String BASE_REPORT = "/smvsReport";
    String BASE_VERIFY_ACTIVATION_CODE = "/verifyActivationCode";
    String BASE_PERSON = "/person";
    String BASE_PARAM_PERSON_ID = BASE_PERSON + "/{personId}";
    String BASE_PARAM_CLASSIFIER_REFERENCE_ID = "classifier/{referenceId}";
    String BASE_MAKE_PAYMENT = "/makePayment";
    String BASE_REVERSE_PAYMENT = "/reversePayment";
    String BASE_GET_PLANS = "/plans";
    String BASE_SAVE_POLICY="/policy";
    String BASE_REPORT_ACTIVE_LIST="/activePendingList";
    String BASE_REPORT_ACTIVE_FILE="/fileReport";
    String BASE_REPORT_COMMERCIALS ="/reportCommercials";
    String BASE_REPORT_LOAD_CASHIERS="/cargarCajeros";

    String LIST_CASHIERS_DETAILS ="/findListCashiers";
    String FORMAT_FILE_LOAD_SALES="formatLoadFileSales";

    String ACTIVATE_PENDING_IN_PERIOD="/activatePendingPeriodPol";

}
