package com.scfg.core.adapter.persistence.mortgageReliefValidations;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.application.port.out.ValidationInsuredMortgageReliefPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ValidationInsuredMortgageReliefAdapter implements ValidationInsuredMortgageReliefPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;

    @Override
    public List<InsuredSummaryDTO> getInsuredsSummary(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId) {
        List<Object[]> insuredsSummaryObjects = monthlyDisbursementRepository.getInsuredsSummary(policyTypeId, monthId, yearId, insurancePolicyHolderId);
        List<InsuredSummaryDTO> insuredSummaryDTOS = insuredsSummaryObjects.stream()
                .map(InsuredSummaryDTO::new)
                .collect(Collectors.toList());
        return insuredSummaryDTOS;

    }

    @Override
    public List<MonthlyDisbursementDhlDTO> getInsuredInOrderDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntityList = monthlyDisbursementRepository
                    .findAllByCaseStatus(
                            1,
                            ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                            ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                            monthId,
                            yearId,
                            insurancePolicyHolderId,
                            ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                            ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType());
            return monthlyDisbursementJpaEntityList.stream()
                    .map(monthlyDisbursementJpaEntity -> mapMonthlyDisbursementJpaEntityToDtoDhl(monthlyDisbursementJpaEntity, false))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getInsuredInOrderDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntityList = monthlyDisbursementRepository
                    .findAllByCaseStatus(
                            1,
                            ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                            ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                            monthId,
                            yearId,
                            insurancePolicyHolderId,
                            ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                            ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType());
            return monthlyDisbursementJpaEntityList.stream()
                    .map(monthlyDisbursementJpaEntity -> mapMonthlyDisbursementJpaEntityToDtoDhn(monthlyDisbursementJpaEntity, false))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }

    public static MonthlyDisbursementDhlDTO mapMonthlyDisbursementJpaEntityToDtoDhl(
            MonthlyDisbursementJpaEntity monthlyDisbursementJpaEntity,
            boolean withRelations) {

        ClientJpaEntity client = monthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = monthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = monthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = monthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = monthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = monthlyDisbursementJpaEntity.getCurrency();

        MonthlyDisbursementDhlDTO monthlyDisbursementDhlDTO = MonthlyDisbursementDhlDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(monthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(monthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASAX(monthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(monthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(monthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .ASEGURADO(monthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(monthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(monthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO_DIAS(monthlyDisbursementJpaEntity.getCreditTermDays())
                .EXTRAPRIMA(monthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .MONTO_PRIMA(monthlyDisbursementJpaEntity.getPremiumAmount())
                .PAGADO_DESDE(monthlyDisbursementJpaEntity.getPaidFrom())
                .PAGADO_HASTA(monthlyDisbursementJpaEntity.getPaidUp())
                .build()
                // For JSON API
                .hiddenRelationship();
        if (withRelations) {
            monthlyDisbursementDhlDTO.setID_CLIENTE(client.getId());
            monthlyDisbursementDhlDTO.setID_OPERACION_CREDITICIA(creditOperation.getId());
            monthlyDisbursementDhlDTO.setID_AGENCIA(agency.getAGENCY_ID()
                    .longValue());
            monthlyDisbursementDhlDTO.setID_TIPO_CREDITO(creditType.getId());
            monthlyDisbursementDhlDTO.setID_TIPO_COBERTURA(coverageType.getId());
            monthlyDisbursementDhlDTO.setID_MONEDA(currency.getId());
        }
        return monthlyDisbursementDhlDTO;
    }

    public static MonthlyDisbursementDhnDTO mapMonthlyDisbursementJpaEntityToDtoDhn(
            MonthlyDisbursementJpaEntity monthlyDisbursementJpaEntity,
            boolean withRelations) {
        ClientJpaEntity client = monthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = monthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = monthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = monthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = monthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = monthlyDisbursementJpaEntity.getCurrency();

        MonthlyDisbursementDhnDTO monthlyDisbursementDhnDTO = MonthlyDisbursementDhnDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(monthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(monthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASA_PRIMA(monthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(monthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(monthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .TIPO_COBERTURA(coverageType.getDescription())
                .ASEGURADO(monthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(monthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(monthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO(monthlyDisbursementJpaEntity.getCreditTermDays())
                .TASA_EXTRAPRIMA(monthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .PRIMA_BS(monthlyDisbursementJpaEntity.getPremiumAmount())
                .build()
                // For JSON API
                .hiddenRelationship();

        if (withRelations) {
            monthlyDisbursementDhnDTO.setID_CLIENTE(client.getId());
            monthlyDisbursementDhnDTO.setID_OPERACION_CREDITICIA(creditOperation.getId());
            monthlyDisbursementDhnDTO.setID_AGENCIA(agency.getAGENCY_ID()
                    .longValue());
            monthlyDisbursementDhnDTO.setID_TIPO_CREDITO(creditType.getId());
            monthlyDisbursementDhnDTO.setID_TIPO_COBERTURA(coverageType.getId());
            monthlyDisbursementDhnDTO.setID_MONEDA(currency.getId());
        }
        return monthlyDisbursementDhnDTO;
    }

}
