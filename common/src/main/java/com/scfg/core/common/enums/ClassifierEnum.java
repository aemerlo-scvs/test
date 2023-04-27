package com.scfg.core.common.enums;

public enum ClassifierEnum {

    // Policy Type
    RegulatedDH_PolicyType(1,1 ),
    UnregulatedDH_PolicyType(2,1 ),

    // Repprts Type
    BrokerSettlementCalculationsDHL_ReportType(1, 2),
    BrokerSettlementCalculationsDHN_ReportType(7, 2),

    ConsolidatedObservedCaseDHL_ReportType(2, 2),
    ConsolidatedObservedCaseDHN_ReportType(8, 2),

    LastObservedCaseDHL_ReportType(3, 2),
    LastObservedCaseDHN_ReportType(9, 2),

    ManualCertificateDHL_ReportType(4, 2),
    ManualCertificateDHN_ReportType(10, 2),

    MonthlyDisbursementsDHL_ReportType(5, 2),
    MonthlyDisbursementsDHN_ReportType(11, 2),

    SubscriptionTrackingDHL_ReportType(6, 2),
    SubscriptionTrackingDHN_ReportType(12, 2),

    MonthlyDisbursementsCreditLineDHN_ReportType(13, 2),

    SinisterDHL_ReportType(14, 2),
    SinisterDHN_ReportType(15, 2),

    PastMonthlyDisbursementsDHL_ReportType(16, 2),
    PastMonthlyDisbursementsDHN_ReportType(17, 2),

    // Coverage Type
    Normal_Coverage(1, 6),
    Limited_Coverage(2,6),
    Accidents016_Coverage(3,6),
    Accidents069_Coverage(4,6),
    FreeCover_Coverage(5,6),
    PremiumRate_Coverage(6,6),
    CreditCard_Coverage(7,6),
    Unemployment_Coverage(8,6),

    // Currency
    Bs_Currency(1, 11),
    USD_Currency(2, 11),

    // Identification Type
    IdentityCard_IdentificationType(1, 12),
    TaxIdentificationNumber_IdentificationType(6, 12),


    // Insurance Request Status DH
    Registered_InsuranceRequestStatus(1, 21),
    Active_InsuranceRequestStatus(2, 21),
    Inactive_InsuranceRequestStatus(3, 21),
    Pending_InsuranceRequestStatus(4, 21),
    PendingReviewsOfCumuls_InsuranceRequestStatus(5, 21),
    Rejected_InsuranceRequestStatus(6, 21),
    Canceled_InsuranceRequestStatus(7, 21),
    CanceledForReplacement_InsuranceRequestStatus(8, 21),
    Timeout_InsuranceRequestStatus(9,21),


    // Credit Type
    LivingPlace_CreditType(1, 19),
    Consumer_CreditType(2, 19),
    Microcredits_CreditType(3, 19),
    Pymes_CreditType(4, 19),

    // Borrower Type
    Holder_BorrowerType(1, 18),
    CosignerOne_BorrowerType(2, 18),
    CosignerTwo_BorrowerType(3, 18),
    CosignerThree_BorrowerType(4, 18),
    CosignerFour_BorrowerType(5, 18),

    // Natural Person - Document Type
    CI_NATIONAL(1,12),
    CI_EXT(2,12),
    CI_EM_EXT(3,12),

    // Natural Persona - Marital Status
    MARRIED_STATUS(1,14), // Casado
    DIVORCED_STATUS(2,14), // Divorciado
    SINGLE_STATUS(3,14), // Soltero
    WIDOWED_STATUS(4,14), // Viudo
    MARITAL_OTHER_STATUS(5,14), // Otro

    // Coverage Type
    COV_PRINCIPAL(1,32),
    COV_COMP(2,32),
    COV_FIXED_CAPITAL(3,32), //Con capital fijo
    COV_ALTERN(4,32),

    // Work Type
    INDEPENDENT(1,44),
    DEPENDENT(2,44),

    // Insurer Type
    TITULAR(1,48),
    CONCOD(2,48), //CONYUGE/CODEUDOR

    // Reject Type
    REJECT_CUMULUS(1,51), // RECHAZO POR CUMULO
    REJECT_LIMITAGE(2,51), // RECHAZO POR LIMITE DE EDAD
    REJECT_IMC(3,51), // RECHAZO POR IMC ALTO
    REJECT_PATOLOGY(4,51), // RECHAZO POR PATOLOGIAS MEDICAS

    R_PEP_CLIENT(5, 51), // CLIENTE PEP
    REJECT_IMC_LESS(6, 51), // RECHAZO POR IMC BAJO

    REJECT_HARD_RISK(7, 51), // RECHAZO POR ACTIVIDAD DE ALTO RIESGO

    PENDING_IMC_OBSERVED(8, 51), // PEDIENTE POR IMC OBSERVADO

    // Acceptance Type
    ACCEPT_NORMAL(1,52), // ACEPTACION NORMAL
    ACCEPT_EXTRAPREMIUM(2,52), // ACEPTACION CON EXTRA PRIMA
    ACCEPT_EXCLUSION(3,52), // ACEPTACION CON EXCLUSION
    ACCEPT_EXTRAPREMIUN_AND_EXCLUSION(4,52), // ACEPTACION CON EXTRA PRIMA Y EXCLUSION
    ACCEPT_AUTOMATIC_SMVS_SYSTEM(5,52), // ACEPTACION AUTOMATICA POR SISTEMAS - SMVS

    //Business Groups
    GRUPO_EMPRESARIAL_LAFUENTE(1, 41),

    //Request Type
    TYPE_REQUEST_REQUEST(1,63), // Tipo de solicitud de formato Solicitud
    TYPE_REQUEST_PROPOSAL(2,63), // Tipo de solicitud de formato Propuesta

    //Transaction account types
    TYPE_TRAN_CLT_CHARGING(1,64), // Tipo de transacción de cobro del cliente
    TYPE_TRAN_CLT_PAYMENT(2,64), // Tipo de transacción de pago del cliente

    //Number Types
    NUMBER_TYPE_WHATSAPP(1,67), // Tipo de número WhatsApp
    NUMBER_TYPE_FIJO(2,67), // Tipo de número Fijo
    NUMBER_TYPE_FREE(3,67), //Tipo de número Linea Gratuita
    NUMBER_TYPE_CEL(4,67), // Tipo de número Celular

    //References Table's
    REFERENCE_TABLE_GENERALREQUEST(1,68), //Tabla de referencia mensajeria - GeneralRequest
    REFERENCE_TABLE_MESSAGERESPONSE(2,68); //Tabla de referencia respuesta propuesta - MessageResponse

    private long referenceCode;
    private long referenceCodeType;


    ClassifierEnum(long referenceCode, long referenceCodeType) {
        this.referenceCode = referenceCode;
        this.referenceCodeType = referenceCodeType;
    }

    public long getReferenceCode() {
        return referenceCode;
    }

    public long getReferenceCodeType() {
        return referenceCodeType;
    }
}
