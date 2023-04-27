package com.scfg.core.adapter.persistence.brokerSettlementCalculations;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.application.port.out.BrokerSettlementCalculationsPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class BrokerSettlementCalculationsAdapter implements BrokerSettlementCalculationsPort {

    private final BrokerSettlementCalculationsRepository brokerSettlementCalculationsRepository;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse registerCalculationsForRegulatedPolicy(
            List<BrokerSettlementCalculationsDhlDTO> brokerSettlementCalculationsDhlDTO,
            long overwrite) {
        List<BrokerSettlementCalculationsJpaEntity> brokerSettlementCalculationsJpaEntities = brokerSettlementCalculationsDhlDTO.stream()
                .map(brokerCalculations -> mapBrokerCalculationsDhlDtoToJpaEntityForCreate(brokerCalculations))
                .collect(Collectors.toList());
        brokerSettlementCalculationsRepository.saveAll(brokerSettlementCalculationsJpaEntities);

        /*return HelpersMethods.mapper()
                .convertValue(brokerSettlementCalculationsJpaEntities,
                        new TypeReference<List<BrokerSettlementCalculationsDhlDTO>>() {
                        });*/

        //List<BrokerSettlementCalculationsDhlDTO> calculationsForDHL = objectMapper.convertValue(brokerSettlementCalculations, new TypeReference<List<BrokerSettlementCalculationsDhlDTO>>() {});

        return new PersistenceResponse(
                BrokerSettlementCalculationsDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse registerCalculationsForNotRegulatedPolicy(
            List<BrokerSettlementCalculationsDhnDTO> brokerSettlementCalculationsDhnDTO,
            long overwrite) {
        List<BrokerSettlementCalculationsJpaEntity> brokerSettlementCalculationsJpaEntities = brokerSettlementCalculationsDhnDTO.stream()
                .map(brokerCalculations -> mapBrokerCalculationsDhnDtoToJpaEntityForCreate(brokerCalculations))
                .collect(Collectors.toList());
        brokerSettlementCalculationsRepository.saveAll(brokerSettlementCalculationsJpaEntities);

        /*return HelpersMethods.mapper()
                .convertValue(brokerSettlementCalculationsJpaEntities,
                        new TypeReference<List<BrokerSettlementCalculationsDhnDTO>>() {
                        });*/
        return new PersistenceResponse(
                BrokerSettlementCalculationsDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Transactional
    @Override
    public List<BrokerSettlementCalculationsDhlDTO> getBrokerSettlementCalculationsDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<BrokerSettlementCalculationsJpaEntity> brokerSettlementCalculationsJpaEntities = brokerSettlementCalculationsRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.BrokerSettlementCalculationsDHL_ReportType.getReferenceCode(),
                    ClassifierEnum.BrokerSettlementCalculationsDHL_ReportType.getReferenceCodeType()
            );
            // Mapper data
            return brokerSettlementCalculationsJpaEntities.stream()
                    .map(brokerSettlementCalculations -> mapBrokerSettlementCalculationsEntityJpaToDtoDhl(brokerSettlementCalculations))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;


    }

    @Transactional
    @Override
    public List<BrokerSettlementCalculationsDhnDTO> getBrokerSettlementCalculationsDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<BrokerSettlementCalculationsJpaEntity> brokerSettlementCalculationsJpaEntities = brokerSettlementCalculationsRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.BrokerSettlementCalculationsDHN_ReportType.getReferenceCode(),
                    ClassifierEnum.BrokerSettlementCalculationsDHN_ReportType.getReferenceCodeType()
            );
            // Mapper data
            return brokerSettlementCalculationsJpaEntities.stream()
                    .map(brokerSettlementCalculations -> mapBrokerSettlementCalculationsEntityJpaToDtoDhn(brokerSettlementCalculations))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;

    }

    public static BrokerSettlementCalculationsJpaEntity mapBrokerCalculationsDhlDtoToJpaEntityForCreate(BrokerSettlementCalculationsDhlDTO brokerSettlementCalculationsDhlDTO) {
        return BrokerSettlementCalculationsJpaEntity.builder()
                .totalInsuredCapital(brokerSettlementCalculationsDhlDTO.getVALOR_ASEGURADO())
                .premiumAmount(brokerSettlementCalculationsDhlDTO.getPRIMA())
                .commisionBankAmount(brokerSettlementCalculationsDhlDTO.getCOM_FASSIL())
                .totalInsureds(brokerSettlementCalculationsDhlDTO.getCANT_ASEGURADOS())
                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        brokerSettlementCalculationsDhlDTO.getITEM_DESGRAVAMEN()
                ))*/
                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(brokerSettlementCalculationsDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                .insuredCoverage(ClassifierJpaEntity.builder()
                        .id(brokerSettlementCalculationsDhlDTO.getID_COBERTURA_ASEGURADOS())
                        .build())
                .build();
    }

    public static BrokerSettlementCalculationsJpaEntity mapBrokerCalculationsDhnDtoToJpaEntityForCreate(BrokerSettlementCalculationsDhnDTO brokerSettlementCalculationsDhnDTO) {
        return BrokerSettlementCalculationsJpaEntity.builder()
                .totalInsuredCapital(brokerSettlementCalculationsDhnDTO.getCAPITAL_ASEGURADO())
                .premiumAmount(brokerSettlementCalculationsDhnDTO.getPRIMA())
                .extraPremiumAmount(brokerSettlementCalculationsDhnDTO.getEXTRAPRIMA())
                .totalPremiumAmount(brokerSettlementCalculationsDhnDTO.getTOTAL_PRIMA())
                .premiumCompanyAmount(brokerSettlementCalculationsDhnDTO.getPRIMA_COMPANIA())
                .commisionBankAmount(brokerSettlementCalculationsDhnDTO.getSERVICIO_COBRANZA())
                .totalInsureds(brokerSettlementCalculationsDhnDTO.getTOTAL_ASEGURADOS())
                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        brokerSettlementCalculationsDhnDTO.getITEM_DESGRAVAMEN()
                ))*/
                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(brokerSettlementCalculationsDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                .insuredCoverage(ClassifierJpaEntity.builder()
                        .id(brokerSettlementCalculationsDhnDTO.getID_COBERTURA_ASEGURADOS())
                        .build())
                .build();
    }


    public static BrokerSettlementCalculationsDhlDTO mapBrokerSettlementCalculationsEntityJpaToDtoDhl(BrokerSettlementCalculationsJpaEntity brokerSettlementCalculationsJpaEntity) {
        try {
            return BrokerSettlementCalculationsDhlDTO.builder()
                    .ITEMS(brokerSettlementCalculationsJpaEntity.getInsuredCoverage()
                            .getDescription())
                    .VALOR_ASEGURADO(brokerSettlementCalculationsJpaEntity.getTotalInsuredCapital())
                    .CANT_ASEGURADOS(brokerSettlementCalculationsJpaEntity.getTotalInsureds())
                    .COM_FASSIL(brokerSettlementCalculationsJpaEntity.getCommisionBankAmount())
                    .PRIMA(brokerSettlementCalculationsJpaEntity.getPremiumAmount())
                    .build()
                    // For JSON API
                    .hiddenRelationship();
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;

    }

    public static BrokerSettlementCalculationsDhnDTO mapBrokerSettlementCalculationsEntityJpaToDtoDhn(BrokerSettlementCalculationsJpaEntity brokerSettlementCalculationsJpaEntity) {
        try {
            return BrokerSettlementCalculationsDhnDTO.builder()
                    .LINEA(brokerSettlementCalculationsJpaEntity.getInsuredCoverage()
                            .getDescription())
                    .CAPITAL_ASEGURADO(brokerSettlementCalculationsJpaEntity.getTotalInsuredCapital())
                    .PRIMA(brokerSettlementCalculationsJpaEntity.getPremiumAmount())
                    .TOTAL_PRIMA(brokerSettlementCalculationsJpaEntity.getTotalPremiumAmount())
                    .PRIMA_COMPANIA(brokerSettlementCalculationsJpaEntity.getPremiumCompanyAmount())
                    .SERVICIO_COBRANZA(brokerSettlementCalculationsJpaEntity.getCommisionBankAmount())
                    .TOTAL_ASEGURADOS(brokerSettlementCalculationsJpaEntity.getTotalInsureds())
                    .EXTRAPRIMA(brokerSettlementCalculationsJpaEntity.getExtraPremiumAmount())

                    //.HIDDEN_RELATIONSHIP(true)
                    .build()
                    // For JSON API
                    .hiddenRelationship();
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;


    }

}
