package com.scfg.core.common.util;

import com.scfg.core.common.enums.ClassifierEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpersConstants {

    public static final String TABLE_CLASSIFIER = "Classifier";
    public static final String TABLE_CLASSIFIER_TYPE = "ClassifierType";
    public static final String TABLE_CLIENT = "Client";
    public static final String TABLE_INSURANCE_REQUEST = "InsuranceRequest";
    public static final String TABLE_CREDIT_OPERATION = "CreditOperation";
    public static final String TABLE_OBSERVED_CASE = "ObservedCase";
    public static final String TABLE_CONSOLIDATED_OBSERVED_CASE = "ConsolidatedObservedCase";
    public static final String TABLE_BROKER_SETTLEMENT_CALCULATIONS = "BrokerSettlementCalculations";
    public static final String TABLE_LAST_OBSERVED_CASE = "LastObservedCase";
    public static final String TABLE_MORTGAGE_RELIEF_ITEM = "MortgageReliefItem";
    public static final String TABLE_INSURANCE_POLICY_HOLDER = "InsurancePolicyHolder";
    public static final String TABLE_MANUAL_CERTIFICATE = "ManualCertificate";
    public static final String TABLE_MONTHLY_DISBURSEMENT = "MonthlyDisbursement";
    public static final String TABLE_PAST_MONTHLY_DISBURSEMENT = "PastMonthlyDisbursement";
    public static final String TABLE_OLD_MONTHLY_DISBURSEMENT = "OldMonthlyDisbursement";
    public static final String TABLE_SUBSCRIPTION_TRACKING = "SubscriptionTracking";
    public static final String TABLE_SINISTER = "Sinister";
    public static final String TABLE_SUBSCRIPTION_REPORT = "SubscriptionReport";

    public static final String TABLE_FBS_BRANCH = "FabolousBranch";
    public static final String TABLE_FBS_ZONE = "FabolousZone";
    public static final String TABLE_FBS_AGENCY = "FabolousAgency";
    public static final String TABLE_FBS_MANAGER = "FabolousManager";
    public static final String TABLE_FBS_MANAGERAGENCY = "FabolousManagerAgency";
    public static final String TABLE_FBS_CLIENT = "FabolousClient";
    public static final String TABLE_FBS_BENEFICIARY = "FabolousBeneficiary";
    public static final String TABLE_FBS_INSURANCE = "FabolousInsurance";
    public static final String TABLE_FBS_UPLOAD_REPORT = "FabolousUploadReport";

    public static final String EXCEL="application/vnd.ms-excel";
    public static final String EXCELEXTENDIDA="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PDF="application/pdf";
    public static final String JPEG="image/jpeg";
    public static final String JPG="image/jpg";
    public static final String PNG="image/png";
    public static final String WORD="application/msword";
    public static final String TEXT="text/plain";

    public static final String TABLE_MANAGER = "MANAGER";
    public static final String TABLE_AGENCY = "AGENCY";

    //public static final String TABLE_INTEMERDIARY_MORTGAGE_RELIEF_ITEM_USER = "MortgageReliefItemUser";




    public static final String PROPERTY_STATUS_RECORDS_FOR_PERSIST = "status";
    public static final String PROPERTY_ACTIVE_RECORDS = "activeRecord";


    public static final String VALUE_ACTIVE = "1";


    public static final String FILTER_ACTIVE_RECORDS_FOR_PERSIST = PROPERTY_STATUS_RECORDS_FOR_PERSIST + "=" + VALUE_ACTIVE;
    public static final String FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE = PROPERTY_ACTIVE_RECORDS + "=" + VALUE_ACTIVE;


    public static final String CONSOLIDATED_OBSERVED_CASE_STATUS_DEBUG = PROPERTY_STATUS_RECORDS_FOR_PERSIST + "=" + VALUE_ACTIVE;

    // STATES FOR CONSOLIDATED OBSERVED CASES
    public static final String CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE = "DEPURAR";
    public static final String CONSOLIDATED_OBSERVED_CASE_STATUS_RECONSIDERER = "RECONSIDERAR";

    public static final int CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE_NUMBER = 0;
    public static final int CONSOLIDATED_OBSERVED_CASE_STATUS_RECONSIDERER_NUMBER = 1;


    // Depends classifier Table (Modifier), future Deprecated
    public static final int REGUALTED_POLICY_ID =  1;
    public static final int NOT_REGUALTED_POLICY_ID =  2;


    // States for save registers on db
    public static final int ACCEPT_OVERWRITE_INFORMATION =  1;

    //#region Constantes para convertir un número en literal
    public static final String[] UNITS = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "}; //UNITS
    public static final String[] TENS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ", //TENS
            "diecisiete ", "dieciocho ", "diecinueve ", "veinte ", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    public static final String[] HUNDREDS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ", //HUNDREDS
            "setecientos ", "ochocientos ", "novecientos "};
    //#endregion


    // Key/value
    public static final String KEY_ACTION_ENTITY = "actionEntity";
    public static final String KEY_CONTENT_ENTITY = "contentEntity";

    public static final String CREATE_ACTION = "save";
    public static final String UPDATE_ACTION = "update";


    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";

    public static final Integer LAST_NUMBER_MONTH = 12;
    public static final Integer FIRST_NUMBER_MONTH = 1;
    public static final Integer INVALID_NUMBER_MONTH = 0;


    // FILE PRELIMINARY OBSERVED CASE
    public static final String FILENAME_PRELIMINARY_OBSERVED_CASE = "casos-observados-preliminares";
    public static final String FIRST_COLUMN_FILE_PRELIMINARY_OBSERVED_CASE = "A";
    public static final String LAST_COLUMN_FILE_PRELIMINARY_OBSERVED_CASE = "H";

    public static final List<String> COLUMN_NAMES_PRELIMINARY_OBSERVED_CASE = new ArrayList<>();
    public static final String COLUMN_01_FILE_PRELIMINARY_OBSERVED_CASE = "NUMERO_OPERACION";
    public static final String COLUMN_02_FILE_PRELIMINARY_OBSERVED_CASE = "NOMBRE_COMPLETO_ASEGURADO";
    public static final String COLUMN_03_FILE_PRELIMINARY_OBSERVED_CASE = "CEDULA_ASEGURADO";
    public static final String COLUMN_04_FILE_PRELIMINARY_OBSERVED_CASE = "MONTO_ACUMULADO";
    public static final String COLUMN_05_FILE_PRELIMINARY_OBSERVED_CASE = "COMENTARIOS_MES_ACTUAL";
    public static final String COLUMN_06_FILE_PRELIMINARY_OBSERVED_CASE = "DESEMBOLSO_MES_ACTUAL";
    public static final String COLUMN_07_FILE_PRELIMINARY_OBSERVED_CASE = "DESEMBOLSO_MES_ANTERIOR";
    public static final String COLUMN_08_FILE_PRELIMINARY_OBSERVED_CASE = "FECHA_DESEMBOLSO";

    // INSURED IN ORDER DHL FILE
    public static final String FILENAME_INSURED_IN_ORDER_DHL = "asegurados-en-regla-dhl";
    public static final String FIRST_COLUMN_FILE_INSURED_IN_ORDER_DHL = "A";
    public static final String LAST_COLUMN_FILE_INSURED_IN_ORDER_DHL = "AD";
    public static final List<String> COLUMN_NAMES_INSURED_IN_ORDER_DHL = new ArrayList<>();
    public static final String COLUMN_01_FILE_INSURED_IN_ORDER_DHL = "NRO_OPERACION";
    public static final String COLUMN_02_FILE_INSURED_IN_ORDER_DHL = "NOMBRES";
    public static final String COLUMN_03_FILE_INSURED_IN_ORDER_DHL = "APELLIDO_PATERNO";
    public static final String COLUMN_04_FILE_INSURED_IN_ORDER_DHL = "APELLIDO_MATERNO";
    public static final String COLUMN_05_FILE_INSURED_IN_ORDER_DHL = "APELLIDO_CASADA";
    public static final String COLUMN_06_FILE_INSURED_IN_ORDER_DHL = "TIPO_DOCUMENTO";
    public static final String COLUMN_07_FILE_INSURED_IN_ORDER_DHL = "NRO_DOCUMENTO";
    public static final String COLUMN_08_FILE_INSURED_IN_ORDER_DHL = "COPIA_DUPLICADO";
    public static final String COLUMN_09_FILE_INSURED_IN_ORDER_DHL = "EXTENSION";
    public static final String COLUMN_10_FILE_INSURED_IN_ORDER_DHL = "PLAZA";
    public static final String COLUMN_11_FILE_INSURED_IN_ORDER_DHL = "FECHA_DESEMBOLSO";
    public static final String COLUMN_12_FILE_INSURED_IN_ORDER_DHL = "VALOR_ASEGURADO";
    public static final String COLUMN_13_FILE_INSURED_IN_ORDER_DHL = "TASAX";
    public static final String COLUMN_14_FILE_INSURED_IN_ORDER_DHL = "FECHA_NACIMIENTO";
    public static final String COLUMN_15_FILE_INSURED_IN_ORDER_DHL = "MONTO_DESEMBOLSADO";
    public static final String COLUMN_16_FILE_INSURED_IN_ORDER_DHL = "FECHA_VENCIMIENTO";
    public static final String COLUMN_17_FILE_INSURED_IN_ORDER_DHL = "MONEDA";
    public static final String COLUMN_18_FILE_INSURED_IN_ORDER_DHL = "TIPO_CREDITO";
    public static final String COLUMN_19_FILE_INSURED_IN_ORDER_DHL = "ASEGURADO";
    public static final String COLUMN_20_FILE_INSURED_IN_ORDER_DHL = "COBERTURA";
    public static final String COLUMN_21_FILE_INSURED_IN_ORDER_DHL = "SEXO";
    public static final String COLUMN_22_FILE_INSURED_IN_ORDER_DHL = "PERIODO";
    public static final String COLUMN_23_FILE_INSURED_IN_ORDER_DHL = "LINEA_CREDITO";
    public static final String COLUMN_24_FILE_INSURED_IN_ORDER_DHL = "PLAZO_CREDITO_DIAS";
    public static final String COLUMN_25_FILE_INSURED_IN_ORDER_DHL = "EXTRAPRIMA";
    public static final String COLUMN_26_FILE_INSURED_IN_ORDER_DHL = "NACIONALIDAD";
    public static final String COLUMN_27_FILE_INSURED_IN_ORDER_DHL = "AGENCIA";
    public static final String COLUMN_28_FILE_INSURED_IN_ORDER_DHL = "MONTO_PRIMA";
    public static final String COLUMN_29_FILE_INSURED_IN_ORDER_DHL = "PAGADO_DESDE";
    public static final String COLUMN_30_FILE_INSURED_IN_ORDER_DHL = "PAGADO_HASTA";

    // INSURED IN ORDER DHN FILE
    public static final String FILENAME_INSURED_IN_ORDER_DHN = "asegurados-en-regla-dhn";
    public static final String FIRST_COLUMN_FILE_INSURED_IN_ORDER_DHN = "A";
    public static final String LAST_COLUMN_FILE_INSURED_IN_ORDER_DHN = "AC";
    public static final List<String> COLUMN_NAMES_INSURED_IN_ORDER_DHN = new ArrayList<>();
    public static final String COLUMN_01_FILE_INSURED_IN_ORDER_DHN = "NRO_OPERACION";
    public static final String COLUMN_02_FILE_INSURED_IN_ORDER_DHN = "NOMBRES";
    public static final String COLUMN_03_FILE_INSURED_IN_ORDER_DHN = "APELLIDO_PATERNO";
    public static final String COLUMN_04_FILE_INSURED_IN_ORDER_DHN = "APELLIDO_MATERNO";
    public static final String COLUMN_05_FILE_INSURED_IN_ORDER_DHN = "APELLIDO_CASADA";
    public static final String COLUMN_06_FILE_INSURED_IN_ORDER_DHN = "TIPO_DOCUMENTO";
    public static final String COLUMN_07_FILE_INSURED_IN_ORDER_DHN = "NRO_DOCUMENTO";
    public static final String COLUMN_08_FILE_INSURED_IN_ORDER_DHN = "COPIA_DUPLICADO";
    public static final String COLUMN_09_FILE_INSURED_IN_ORDER_DHN = "EXTENSION";
    public static final String COLUMN_10_FILE_INSURED_IN_ORDER_DHN = "PLAZA";
    public static final String COLUMN_11_FILE_INSURED_IN_ORDER_DHN = "FECHA_DESEMBOLSO";
    public static final String COLUMN_12_FILE_INSURED_IN_ORDER_DHN = "VALOR_ASEGURADO";
    public static final String COLUMN_13_FILE_INSURED_IN_ORDER_DHN = "TASA_PRIMA";
    public static final String COLUMN_14_FILE_INSURED_IN_ORDER_DHN = "FECHA_NACIMIENTO";
    public static final String COLUMN_15_FILE_INSURED_IN_ORDER_DHN = "MONTO_DESEMBOLSADO";
    public static final String COLUMN_16_FILE_INSURED_IN_ORDER_DHN = "FECHA_VENCIMIENTO";
    public static final String COLUMN_17_FILE_INSURED_IN_ORDER_DHN = "MONEDA";
    public static final String COLUMN_18_FILE_INSURED_IN_ORDER_DHN = "TIPO_CREDITO";
    public static final String COLUMN_19_FILE_INSURED_IN_ORDER_DHN = "ASEGURADO";
    public static final String COLUMN_20_FILE_INSURED_IN_ORDER_DHN = "COBERTURA";
    public static final String COLUMN_21_FILE_INSURED_IN_ORDER_DHN = "TIPO_COBERTURA";
    public static final String COLUMN_22_FILE_INSURED_IN_ORDER_DHN = "SEXO";
    public static final String COLUMN_23_FILE_INSURED_IN_ORDER_DHN = "PERIODO";
    public static final String COLUMN_24_FILE_INSURED_IN_ORDER_DHN = "LINEA_CREDITO";
    public static final String COLUMN_25_FILE_INSURED_IN_ORDER_DHN = "PLAZO_CREDITO";
//    public static final String COLUMN_26_FILE_INSURED_IN_ORDER_DHN = "TASA_EXTRAPRIMA_BANCO";
//    public static final String COLUMN_27_FILE_INSURED_IN_ORDER_DHN = "TASA_EXTRAPRIMA_SCVS";
    public static final String COLUMN_28_FILE_INSURED_IN_ORDER_DHN = "NACIONALIDAD";
    public static final String COLUMN_29_FILE_INSURED_IN_ORDER_DHN = "AGENCIA";
    public static final String COLUMN_30_FILE_INSURED_IN_ORDER_DHN = "PRIMA_BS";
    public static final String COLUMN_31_FILE_INSURED_IN_ORDER_DHN = "TASA_EXTRAPRIMA";
//    public static final String COLUMN_32_FILE_INSURED_IN_ORDER_DHN = "EXTRAPRIMA_BS";

    // AGE VALIDATIONS
    public static final long ENTRY_AGE_LIMIT = 70;
    public static final long STAY_AGE_LIMIT = 75;

    //STATUS VALIDATIONS
    public static final Long REGISTERED = 977L;
    public static final Long ACTIVE = 978L;
    public static final Long INACTIVE = 979L;
    public static final Long PENDING = 980L;
    public static final Long CUMULUS_PENDING_REVIEW = 981L;
    public static final Long REJECTED = 982L;
    public static final Long CANCELED = 983L;
    public static final Long CANCELED_FOR_REPLACEMENT = 984L;
    public static final Long TIMED_OUT = 985L;

    // COVERAGE TYPE VALIDATIONS
    public static final String NORMAL = "NORMAL";
    public static final String LIMITED_COVERAGE = "COB. LIMITADA";
    public static final String ACCIDENTS_16 = "ACCIDENTES 0.16";
    public static final String ACCIDENTS_69 = "ACCIDENTES 0.69";
    public static final String FREE_COVER = "FREE COVER";
    public static final String PRIME_RATE = "TASA PREFERENCIAL";
    public static final String CREDIT_CARD = "TARJETAS DE CREDITO";
    public static final String UNEMPLOYMENT = "DESEMPLEO";

    //DJS LIMIT DATE
    public static final long DJS_LIMIT_DATE = 180;

    //ESTADOS SINIESTROS
    public static final String PAID = "Pagado";

    //GARANTE
    public static final String GUARANTOR = "GARANTE";
    public static final String GUARANTOR_DATE_LIMIT = "2020-01-01";

    public static final List<Long> REPORTS_FOR_EXCLUDE = new ArrayList<>();
    // VALIDATIONS RULES INSURED
    public static final String VR_INSURANCE_NOT_FOUND = "No se encontró el seguro";
    public static final String VR_INSURANCE_PENDING_APPROVAL = "El estado de la solicitud del seguro no es ACTIVO";
    public static final String VR_INSURANCE_REJECTED = "Seguro rechazado";
    public static final String VR_ENTRY_AGE_OUT_RANGE = "El asegurado sobrepasó el límite de edad permitido";
    public static final String VR_STAY_AGE_OUT_RANGE = "El asegurado sobrepasó el límite de edad permitido";
    public static final String VR_DIFFERENT_EXTRAPREMIUM_RATE = "Las tasas de extraprima no coinciden";
    public static final String VR_DIFFERENT_AMOUNTS_DISBURSEMENT_AND_SUBSCRIBED = "Monto desembolsado es mayor al monto suscrito";
    public static final String VR_DIFFERENT_CREDIT_TERM_CREDIT_OPERATION = "Plazo del credito reportado es menor al plazo de credito suscrito";
    public static final String VR_DJS_APPROVAL_MAXIMUM_TIME = "El tiempo de llenado de la DJS excedió los 180 días";
    public static final String VR_LIMIT_COVERAGE_FOR_CREDIT_LINE = "La operacion pertenece a una línea de crédito";
    public static final String VR_DUPLICATE_CREDIT_OPERATIONS = "La operación se encuentra duplicada";
    public static final String VR_PREMIUM_ZERO_AMOUNT = "El monto de la prima es igual a 0";
    public static final String VR_REPORT_PAID_SINISTER = "El siniestro reportado por el asegurado ya fue pagado";
    public static final String VR_CUMULUS_INCREASE = "Aumento inusual en el cumulo del cliente";
    public static final String VR_REPORTED_NEW_DISBURSEMENT_OUT_PERIOD = "Desembolsos reportados fuera de rango";
    public static final String VR_IRREGULARITY_IN_HIERARCHY_BORROWERS = "Inconsistencia en correlacion jerarquica de prestarios";
    public static final String VR_REPORTED_GUARANTORS_OUT_PERIOD_2019 = "la operación reporta garantes, siendo que fue generada después del 2019";

    public static final String CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS = "Operacion no encontrada en reporte de desgravamen mensual";

    public static final Integer IN_ORDER_MONTHLY_DISBURSEMENT = 1;
    public static final Integer OBSERVED_MONTHLY_DISBURSEMENT = -1;

    public static final String MESSAGE_VALIDATIONS_INSUREDS = "Validacion exitosa de los asegurados : " + LocalDate.now().toString();


    // EXCEPCIONES DE OPERACIONES CREDITICIAS
    public static final String CREDIT_OPERATION_NOT_FOUND_ERROR = "Operacion crediticia";

    // TODO: CHANGE SCHEMA DATABASE IN JUST TIME
    public static final String CURRENT_SCHEMA_DB_NAME = "dbo";

    /*
    *  columns.add("NUMERO_OPERACION");
        columns.add("NOMBRE_COMPLETO_ASEGURADO");
        columns.add("CEDULA_ASEGURADO");
        columns.add("MONTO_ACUMULADO");
        columns.add("COMENTARIOS_MES_ACTUAL");
        columns.add("DESEMBOLSO_MES_ACTUAL");
        columns.add("DESEMBOLSO_MES_ANTERIOR");
    *
    * */
    static {
        COLUMN_NAMES_PRELIMINARY_OBSERVED_CASE.addAll(Arrays.asList(
                COLUMN_01_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_02_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_03_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_04_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_05_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_06_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_07_FILE_PRELIMINARY_OBSERVED_CASE,
                COLUMN_08_FILE_PRELIMINARY_OBSERVED_CASE
        ));

        COLUMN_NAMES_INSURED_IN_ORDER_DHL.addAll(Arrays.asList(
                COLUMN_01_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_02_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_03_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_04_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_05_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_06_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_07_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_08_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_09_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_10_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_11_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_12_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_13_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_14_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_15_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_16_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_17_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_18_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_19_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_20_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_21_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_22_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_23_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_24_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_25_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_26_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_27_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_28_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_29_FILE_INSURED_IN_ORDER_DHL,
                COLUMN_30_FILE_INSURED_IN_ORDER_DHL
        ));

        COLUMN_NAMES_INSURED_IN_ORDER_DHN.addAll(Arrays.asList(
                COLUMN_01_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_02_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_03_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_04_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_05_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_06_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_07_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_08_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_09_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_10_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_11_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_12_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_13_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_14_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_15_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_16_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_17_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_18_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_19_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_20_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_21_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_22_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_23_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_24_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_25_FILE_INSURED_IN_ORDER_DHN,
//                COLUMN_26_FILE_INSURED_IN_ORDER_DHN,
//                COLUMN_27_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_28_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_29_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_30_FILE_INSURED_IN_ORDER_DHN,
                COLUMN_31_FILE_INSURED_IN_ORDER_DHN
//                COLUMN_32_FILE_INSURED_IN_ORDER_DHN
        ));


        REPORTS_FOR_EXCLUDE.addAll(
                Arrays.asList(
                        ClassifierEnum.PastMonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.PastMonthlyDisbursementsDHN_ReportType.getReferenceCode()
                )
        );
    }


    // Custom messages
    public static final String MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES = "Casos observados no encontrados para los parametros de busqueda seleccionados";
    // Names Report for sepelio masivo
    public  static final  String NAME_REPORT_CERTIFICATE_SMVS="REPORT_CERTIFICATE_SSPM_V3";
    public  static final  String NAME_REPORT_CERTIFICATE_SMVS4="REPORT_CERTIFICATE_SSPM_V4";





}
