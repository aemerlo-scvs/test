package com.scfg.core.adapter.persistence.lastCasesObserved;


import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.application.port.out.LastObservedCasePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class LastObservedCasePersistenceAdapter implements LastObservedCasePort {

    private final LastObservedCaseRepository lastObservedCaseRepository;


    @Override
    public PersistenceResponse registerLastObservedCasesRegulatedPolicy(List<LastObservedCaseDhlDTO> lastObservedCaseDhlDTOS, long overwrite) {
        List<LastObservedCaseJpaEntity> lastObservedCaseJpaEntities = lastObservedCaseDhlDTOS.stream()
                .map(lastObservedCase -> mapLastObservedCaseDhlDtoToJpaEntityForCreate(lastObservedCase))
                .collect(Collectors.toList());
        lastObservedCaseRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                LastObservedCaseDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerLastObservedCasesnotRegulatedPolicy(List<LastObservedCaseDhnDTO> lastObservedCaseDhnDTOS, long overwrite) {
        List<LastObservedCaseJpaEntity> lastObservedCaseJpaEntities = lastObservedCaseDhnDTOS.stream()
                .map(lastObservedCase -> mapLastObservedCaseDhnDtoToJpaEntityForCreate(lastObservedCase))
                .collect(Collectors.toList());
        lastObservedCaseRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                LastObservedCaseDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
        /*try {
        } catch (Exception e) {
            String err = e.getMessage();
        }
        return null;*/
    }

    @Transactional
    @Override
    public List<LastObservedCaseDhlDTO> getLastObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<LastObservedCaseJpaEntity> lastObservedCaseJpaEntities = lastObservedCaseRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.LastObservedCaseDHL_ReportType.getReferenceCode(),
                    ClassifierEnum.LastObservedCaseDHL_ReportType.getReferenceCodeType()
            );
            /*String x = "Policy Reference Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCode() + "\n" +
                    "Policy Type Ref Code: " + ClassifierEnum.RegulatedPolicyDH.getReferenceCodeType() + "\n" +
                    "Month Id: " + monthId + "\n" +
                    "Year Id:" + yearId + "\n" +
                    "Insurance Policy Holder Id: " + insurancePolicyHolderId + "\n" +
                    "Report reference code: " + ClassifierEnum.ReportLastObservedCaseDHL.getReferenceCode() + "\n" +
                    "Report Type reference code: " + ClassifierEnum.ReportLastObservedCaseDHL.getReferenceCodeType();
            log.info(x);*/

            // Mapper data
            return lastObservedCaseJpaEntities.stream()
                    .map(lastObservedCase -> mapLastObservedCaseEntityJpaToDtoDhl(lastObservedCase))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }


    @Transactional
    @Override
    public List<LastObservedCaseDhnDTO> getLastObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {

            List<LastObservedCaseJpaEntity> lastObservedCaseJpaEntities = lastObservedCaseRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.LastObservedCaseDHN_ReportType.getReferenceCode(),
                    ClassifierEnum.LastObservedCaseDHN_ReportType.getReferenceCodeType()
            );

            /*String x = "Policy Reference Code: " + ClassifierEnum.UnregulatedPolicyDH.getReferenceCode() + "\n" +
                    "Policy Type Ref Code: " + ClassifierEnum.UnregulatedPolicyDH.getReferenceCodeType() + "\n" +
                    "Month Id: " + monthId + "\n" +
                    "Year Id:" + yearId + "\n" +
                    "Insurance Policy Holder Id: " + insurancePolicyHolderId + "\n" +
                    "Report reference code: " + ClassifierEnum.ReportLastObservedCaseDHN.getReferenceCode() + "\n" +
                    "Report Type reference code: " + ClassifierEnum.ReportLastObservedCaseDHN.getReferenceCodeType();*/

            // Mapper data
            return lastObservedCaseJpaEntities.stream()
                    .map(lastObservedCase -> mapLastObservedCaseEntityJpaToDtoDhn(lastObservedCase))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }


    public static LastObservedCaseDhlDTO mapLastObservedCaseEntityJpaToDtoDhl(LastObservedCaseJpaEntity lastObservedCaseDhl) {
        ClientJpaEntity client = lastObservedCaseDhl.getClient();

        return LastObservedCaseDhlDTO.builder()
                .NRO(lastObservedCaseDhl.getId()) // modificar
                .NRO_OPERACION(lastObservedCaseDhl.getCreditOperation()
                        .getOperationNumber())
                .NRO_SOLICITUD(lastObservedCaseDhl.getCreditOperation()
                        .getInsuranceRequest()
                        .getRequestNumber())
                .NOMBRE(client.getFullname())
                .NRO_CI(client.getDocumentNumber())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .SALDO(lastObservedCaseDhl.getBalance())
                .FECHA_DESEMBOLSO(lastObservedCaseDhl.getDisbursementDate())
                .CAPITAL_EXCLUIDO(lastObservedCaseDhl.getExludedCapital())
                .TIPO_OBSERVACION(lastObservedCaseDhl.getObservationType()
                        .getDescription())
                .build()
                // for API JSON
                .hiddenRelationship();
    }

    public static LastObservedCaseDhnDTO mapLastObservedCaseEntityJpaToDtoDhn(LastObservedCaseJpaEntity lastObservedCaseDhn) {
        ClientJpaEntity client = lastObservedCaseDhn.getClient();

        return LastObservedCaseDhnDTO.builder()
                .NRO(lastObservedCaseDhn.getId()) // modificar
                .NRO_OPERACION(lastObservedCaseDhn.getCreditOperation()
                        .getOperationNumber())
                .NOMBRE(client.getFullname())
                .NRO_SOLICITUD(lastObservedCaseDhn.getCreditOperation()
                        .getInsuranceRequest()
                        .getRequestNumber())
                .NRO_CI(client.getDocumentNumber())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .SALDO(lastObservedCaseDhn.getBalance())
                .FECHA_DESEMBOLSO(lastObservedCaseDhn.getDisbursementDate())
                .CAPITAL_EXCLUIDO(lastObservedCaseDhn.getExludedCapital())
                .TIPO_OBSERVACION(lastObservedCaseDhn.getObservationType()
                        .getDescription())
                .build()
                // for API JSON
                .hiddenRelationship();
    }

    public static LastObservedCaseJpaEntity mapLastObservedCaseDhlDtoToJpaEntityForCreate(LastObservedCaseDhlDTO lastObservedCaseDhlDTO) {
        return LastObservedCaseJpaEntity.builder()
                .balance(lastObservedCaseDhlDTO.getSALDO())
                .disbursementDate(lastObservedCaseDhlDTO.getFECHA_DESEMBOLSO())
                .exludedCapital(lastObservedCaseDhlDTO.getCAPITAL_EXCLUIDO())

                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(lastObservedCaseDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(lastObservedCaseDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())*/
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        lastObservedCaseDhlDTO.getITEM_DESGRAVAMEN()
                ))*/
                .observationType(ClassifierJpaEntity.builder()
                        .id(lastObservedCaseDhlDTO.getID_TIPO_OBSERVACION())
                        .build())
                .creditOperation(CreditOperationJpaEntity.builder()
                        .id(lastObservedCaseDhlDTO.getID_OPERACION_CREDITICIA())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(lastObservedCaseDhlDTO.getID_CLIENTE())
                        .build())

                .build();
    }

    public static LastObservedCaseJpaEntity mapLastObservedCaseDhnDtoToJpaEntityForCreate(LastObservedCaseDhnDTO lastObservedCaseDhnDTO) {
        return LastObservedCaseJpaEntity.builder()
                .balance(lastObservedCaseDhnDTO.getSALDO())
                .disbursementDate(lastObservedCaseDhnDTO.getFECHA_DESEMBOLSO())
                .exludedCapital(lastObservedCaseDhnDTO.getCAPITAL_EXCLUIDO())

                // For relationship
                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(lastObservedCaseDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                /*.mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(lastObservedCaseDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())*/
                .observationType(ClassifierJpaEntity.builder()
                        .id(lastObservedCaseDhnDTO.getID_TIPO_OBSERVACION())
                        .build())
                /*.observationType(ClassifierPersistenceAdapter.mapToJpaEntity(lastObservedCaseDhnDTO.getOBSERVACION()))
                .creditOperation(CreditOperationAdapter.mapToJpaEntity(lastObservedCaseDhnDTO.getOPERACION_CREDITICIA()))
                .client(ClientPersistenceAdapter.mapToJpaEntity(lastObservedCaseDhnDTO.getCLIENTE()))*/
                .creditOperation(CreditOperationJpaEntity.builder()
                        .id(lastObservedCaseDhnDTO.getID_OPERACION_CREDITICIA())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(lastObservedCaseDhnDTO.getID_CLIENTE())
                        .build())
                .build();
    }
}
