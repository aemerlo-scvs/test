package com.scfg.core.adapter.persistence.consolidatedObservedCase;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.observedCase.ObservedCaseJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.application.port.out.ConsolidatedObservedCasePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhnDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class ConsolidatedObservedCaseAdapter implements ConsolidatedObservedCasePort {

    private final ConsolidatedObservedCaseRepository consolidatedObservedCaseRepository;

    @Override
    public PersistenceResponse registerConsolidatedObservedCaseForRegulatedPolicy(
            List<ConsolidatedObservedCaseDhlDTO> consolidatedObservedCaseDhlDTOList, long overwrite) {
        List<ConsolidatedObservedCaseJpaEntity> consolidatedObservedCaseJpaEntityList = consolidatedObservedCaseDhlDTOList
                .stream()
                .map(consolidatedObservedCaseDhlDTO -> mapConsolidatedObservedCaseDhlDtoToJpaEntityForCreate(consolidatedObservedCaseDhlDTO))
                .collect(Collectors.toList());
        consolidatedObservedCaseRepository.saveAll(consolidatedObservedCaseJpaEntityList);
        return new PersistenceResponse(
                ConsolidatedObservedCaseDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerConsolidatedObservedCaseForNotRegulatedPolicy(
            List<ConsolidatedObservedCaseDhnDTO> consolidatedObservedCaseDhnDTOList, long overwrite) {
        List<ConsolidatedObservedCaseJpaEntity> consolidatedObservedCaseJpaEntityList = consolidatedObservedCaseDhnDTOList
                .stream()
                .map(consolidatedObservedCaseDhnDTO -> mapConsolidatedObservedCaseDhnDtoToJpaEntityForCreate(consolidatedObservedCaseDhnDTO))
                .collect(Collectors.toList());
        consolidatedObservedCaseRepository.saveAll(consolidatedObservedCaseJpaEntityList);
        return new PersistenceResponse(
                ConsolidatedObservedCaseDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Transactional
    @Override
    public List<ConsolidatedObservedCaseDhlDTO> getConsolidatedObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<ConsolidatedObservedCaseJpaEntity> consolidatedObservedCaseJpaEntities = consolidatedObservedCaseRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType.getReferenceCode(),
                    ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType.getReferenceCodeType()
            );
            /*String x = "Policy Reference Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCode() + "\n" +
                    "Policy Type Ref Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCodeType() + "\n" +
                    "Month Id: " + monthId + "\n" +
                    "Year Id:" + yearId + "\n" +
                    "Insurance Policy Holder Id: " + insurancePolicyHolderId + "\n" +
                    "Report reference code: " + ClassifierEnum.ReportConsolidatedObservedCaseDHL.getReferenceCode() + "\n" +
                    "Report Type reference code: " + ClassifierEnum.ReportConsolidatedObservedCaseDHL.getReferenceCodeType();


            log.info(x);*/

            // Mapper data
            return consolidatedObservedCaseJpaEntities.stream()
                    .map(consolidatedObservedCase -> mapConsolitadedObservedCaseEntityJpaToDtoDhl(consolidatedObservedCase))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }


    @Transactional
    @Override
    public List<ConsolidatedObservedCaseDhnDTO> getConsolidatedObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<ConsolidatedObservedCaseJpaEntity> consolidatedObservedCaseJpaEntities = consolidatedObservedCaseRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCode(),
                    ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCodeType()
            );
           /* String x = "Policy Reference Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCode() + "\n" +
                    "Policy Type Ref Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCodeType() + "\n" +
                    "Month Id: " + monthId + "\n" +
                    "Year Id:" + yearId + "\n" +
                    "Insurance Policy Holder Id: " + insurancePolicyHolderId + "\n" +
                    "Report reference code: " + ClassifierEnum.ReportConsolidatedObservedCaseDHN.getReferenceCode() + "\n" +
                    "Report Type reference code: " + ClassifierEnum.ReportConsolidatedObservedCaseDHN.getReferenceCodeType();


            log.info(x);*/

            // Mapper data
            return consolidatedObservedCaseJpaEntities.stream()
                    .map(consolidatedObservedCase -> mapConsolitadedObservedCaseEntityJpaToDtoDhn(consolidatedObservedCase))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }


    // Mapper to JSON API
    public static ConsolidatedObservedCaseDhlDTO mapConsolitadedObservedCaseEntityJpaToDtoDhl(ConsolidatedObservedCaseJpaEntity consolidatedObservedCase) {
        ObservedCaseJpaEntity observedCaseJpaEntity = consolidatedObservedCase.getObservedCase();

        CreditOperationJpaEntity creditOperationJpaEntity = observedCaseJpaEntity.getCreditOperation();

        InsuranceRequestJpaEntity insuranceRequestJpaEntity = creditOperationJpaEntity.getInsuranceRequest();
        ClientJpaEntity clientJpaEntity = observedCaseJpaEntity.getClient();

        ClassifierJpaEntity currencyEntity = consolidatedObservedCase.getCurrency();

        return ConsolidatedObservedCaseDhlDTO.builder()
                .NRO_OPERACION(creditOperationJpaEntity.getOperationNumber())
                .FECHA_DESEMBOLSO(creditOperationJpaEntity.getDisbursementDate())
                .CLIENTE(clientJpaEntity.getFullname())
                .CI(clientJpaEntity.getDocumentNumber())
                .DESEMBOLSOS_ANTERIORES(observedCaseJpaEntity.getPreviousMonthDisbursement())
                .DESEMBOLSOS_MES_ACTUAL(observedCaseJpaEntity.getCurrentMonthDisbursement())
                .ACUMULADO(observedCaseJpaEntity.getAccumulated())
                .COMENTARIOS_MES_ACTUAL(observedCaseJpaEntity.getCurrentMonthComments()) // change
                .CAPITAL_USD(consolidatedObservedCase.getInsuredCapital())
                .FECHA(consolidatedObservedCase.getLastAcceptanceDate()) // renombrar el DTO
                .CONDICION(consolidatedObservedCase.getCondition())
                .NRO_SOL_WEB(insuranceRequestJpaEntity.getRequestNumber())
                .ESTADO_SOLICITUD(insuranceRequestJpaEntity.getRequestStatus().getDescription())
                .COMENTARIO(consolidatedObservedCase.getBrokerComments())
                .ESTADO(consolidatedObservedCase.getFinalStatus()
                        .equals(HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE_NUMBER)
                        ? HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE
                        : HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_RECONSIDERER)
                .build()

                // for JSON API
                .hiddenRelationship();
    }

    private static ConsolidatedObservedCaseDhnDTO mapConsolitadedObservedCaseEntityJpaToDtoDhn(ConsolidatedObservedCaseJpaEntity consolidatedObservedCase) {
        ObservedCaseJpaEntity observedCaseJpaEntity = consolidatedObservedCase.getObservedCase();

        CreditOperationJpaEntity creditOperationJpaEntity = observedCaseJpaEntity.getCreditOperation();

        InsuranceRequestJpaEntity insuranceRequestJpaEntity = creditOperationJpaEntity.getInsuranceRequest();
        ClientJpaEntity clientJpaEntity = observedCaseJpaEntity.getClient();

        ClassifierJpaEntity currencyEntity = consolidatedObservedCase.getCurrency();

        return ConsolidatedObservedCaseDhnDTO.builder()
                .NRO_OPERACION(creditOperationJpaEntity.getOperationNumber())
                .FECHA_DESEMBOLSO(creditOperationJpaEntity.getInsuranceRequest().getDisbursementDate())
                .CLIENTE(clientJpaEntity.getFullname())
                .CI(clientJpaEntity.getDocumentNumber())
                .DESEMBOLSOS_ANTERIORES(observedCaseJpaEntity.getPreviousMonthDisbursement())
                .DESEMBOLSOS_MES_ACTUAL(observedCaseJpaEntity.getCurrentMonthDisbursement())
                .MONTO_ACUMULADO(observedCaseJpaEntity.getAccumulated())
                .COMENTARIOS_MES_ACTUAL(observedCaseJpaEntity.getCurrentMonthComments()) // change
                .CAPITAL_BS(consolidatedObservedCase.getInsuredCapital())
                .FECHA(consolidatedObservedCase.getLastAcceptanceDate()) // renombrar el DTO
                .CONDICION(consolidatedObservedCase.getCondition())
                .NRO_SOLICITUD_WEB(insuranceRequestJpaEntity.getRequestNumber())
                .ESTADO_SOLICITUD(insuranceRequestJpaEntity.getRequestStatus().getDescription())
                .COMENTARIOS_BROKER(consolidatedObservedCase.getBrokerComments())
                .ESTADO(consolidatedObservedCase.getFinalStatus()
                        .equals(HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE_NUMBER)
                        ? HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE
                        : HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_RECONSIDERER)
                .build()

                // for JSON API
                .hiddenRelationship();


    }


    public static ConsolidatedObservedCaseJpaEntity mapConsolidatedObservedCaseDhlDtoToJpaEntityForCreate(
            ConsolidatedObservedCaseDhlDTO consolidatedObservedCaseDhlDTO) {
        return ConsolidatedObservedCaseJpaEntity.builder()
                .brokerComments(consolidatedObservedCaseDhlDTO.getCOMENTARIO())
                .condition(consolidatedObservedCaseDhlDTO.getCONDICION())
                .lastAcceptanceDate(consolidatedObservedCaseDhlDTO.getFECHA())
                .insuredCapital(consolidatedObservedCaseDhlDTO.getCAPITAL_BS())
                .finalStatus(consolidatedObservedCaseDhlDTO.getESTADO()
                        .equals(HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE) ? 0 : 1)
                // For relationship
                .currency(ClassifierJpaEntity.builder()
                        .id(consolidatedObservedCaseDhlDTO.getID_TIPO_MONEDA())
                        .build())
                .observedCase(ObservedCaseJpaEntity.builder()
                        .id(consolidatedObservedCaseDhlDTO.getID_CASO_OBSERVADO())
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(consolidatedObservedCaseDhlDTO.getID_SOLICITUD())
                        .build())
                .build();
    }

    public static ConsolidatedObservedCaseJpaEntity mapConsolidatedObservedCaseDhnDtoToJpaEntityForCreate(
            ConsolidatedObservedCaseDhnDTO consolidatedObservedCaseDhnDTO) {
        return ConsolidatedObservedCaseJpaEntity.builder()
                .brokerComments(consolidatedObservedCaseDhnDTO.getCOMENTARIOS_BROKER())
                .condition(consolidatedObservedCaseDhnDTO.getCONDICION())
                .lastAcceptanceDate(consolidatedObservedCaseDhnDTO.getFECHA())
                .insuredCapital(consolidatedObservedCaseDhnDTO.getCAPITAL_BS())
                .finalStatus(consolidatedObservedCaseDhnDTO.getESTADO()
                        .equals(HelpersConstants.CONSOLIDATED_OBSERVED_CASE_STATUS_DEPURATE) ? 0 : 1)
                // For relationship
                .currency(ClassifierJpaEntity.builder()
                        .id(consolidatedObservedCaseDhnDTO.getID_TIPO_MONEDA())
                        .build())
                .observedCase(ObservedCaseJpaEntity.builder()
                        .id(consolidatedObservedCaseDhnDTO.getID_CASO_OBSERVADO())
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(consolidatedObservedCaseDhnDTO.getID_SOLICITUD())
                        .build())
                .build();
    }
}
