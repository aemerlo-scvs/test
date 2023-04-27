package com.scfg.core.common.enums;

public enum ClassifierTypeEnum {

    PolicyType(1, "Tipo Poliza"),
    ReportType(2, "Tipo Reporte"),
    InsuranceTaker(3, "Tomador Seguro"),
    Month(4, "Mes"),
    Year(5, "Gestion"),
    InsuranceCoverage(6, "Cobertura Asegurado"),
    Gender(7, "Genero"),
    ExclusionType(8, "Tipos de Exclusion"),
    Regional(9, "Regionales"),
    Office(10, "Oficinas"),
    Currency(11, "Monedas"),
    DocumentType(12, "Tipos de documento de identidad"),

    Relationship(13, "Parentescos"),
    MaritalStatus(14, "Estado Civil"),
    Country(15, "Paises"),
    Activity(16, "Actividades"),
    SMVSInsuranceRequestStatus(17, "Estados de Solicitud (Sepelio)"),
    ExtensionsDocumentType(18, "Extensiones CI"),
    CreditTYpe(19, "Tipos de credito"),
    BorrowerType(20, "Tipos de prestatarios"),
    DHInsuranceRequestStatus(21, "Estados de Solicitud (DH)"),
    DirectionType(22, "Tipos de Direcciones"),
    FormatType(23, "Tipos de Formatos"),
    PersonDocumentType(24, "Tipos de Documentos de Persona"),
    PolicyStatus(25, "Estado de la Póliza"),
    annexesType(26, "Tipos de Anexos"),
    paymentType(27, "Tipos de Pago"),
    periodicity(28, "Periodicidad"),
    paymentChannel(29, "Canal de Pago"),
    transactionType(30, "Tipo de Transacción"),
    receiptStatus(31, "Estado del Recibo"),
    requirementType(40, "Lista de Requisitos"),
    businessGroups(41, "Lista de grupos empresariales"),
    citeType(49, "Cite"),
    rejectType(51, "Motivo Rechazo"),
    acceptType(52, "Motivo Aceptacion");


    private long referenceId;
    private String name;

    public long getReferenceId() {
        return referenceId;
    }

    public String getName() {
        return name;
    }

    ClassifierTypeEnum(long referenceId, String name){
        this.referenceId = referenceId;
        this.name = name;

    }
}

