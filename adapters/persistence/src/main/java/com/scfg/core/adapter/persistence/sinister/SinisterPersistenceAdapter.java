package com.scfg.core.adapter.persistence.sinister;

import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.application.port.out.SinisterPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhnDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class SinisterPersistenceAdapter implements SinisterPort {

    private final SinisterRepository sinisterRepository;

    @Override
    public PersistenceResponse registerSinistersForRegulatedPolicy(List<SinisterDhlDTO> sinisterDhlDTOS, long overwrite) {
        List<SinisterJpaEntity> sinisterJpaEntities = sinisterDhlDTOS.stream()
                .map(sinister -> mapSinisterDhlDtoToJpaEntityForCreate(sinister))
                .collect(Collectors.toList());
        sinisterRepository.saveAll(sinisterJpaEntities);

        return new PersistenceResponse(
                SinisterDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerSinistersForNotRegulatedPolicy(List<SinisterDhnDTO> sinisterDhnDTOS, long overwrite) {
        List<SinisterJpaEntity> sinisterJpaEntities = sinisterDhnDTOS.stream()
                .map(sinister -> mapSinisterDhnDtoToJpaEntityForCreate(sinister))
                .collect(Collectors.toList());
        sinisterRepository.saveAll(sinisterJpaEntities);

        return new PersistenceResponse(
                SinisterDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Transactional
    @Override
    public List<SinisterDhlDTO> getSinistersDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<SinisterJpaEntity> sinisterJpaEntities = sinisterRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.SinisterDHL_ReportType.getReferenceCode(),
                    ClassifierEnum.SinisterDHL_ReportType.getReferenceCodeType()
            );

            String x = "Policy Ref = " + ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode() + "\n " +
                    "Policy Type Ref = "+  ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType()+"\n " +
                    "Month Id = "+monthId+"\n " +
                    "Year Id = "+yearId+"\n " +
                    "Insured Policy Holder Id = "+insurancePolicyHolderId+"\n " +
                    "Report Ref = "+ClassifierEnum.SinisterDHL_ReportType.getReferenceCode()+"\n " +
                    "Report Type Ref = "+ClassifierEnum.SinisterDHL_ReportType.getReferenceCodeType()+"\n " ;
            log.info(x);



            // Mapper data
            return sinisterJpaEntities.stream()
                    .map(sinister -> mapSinisterEntityJpaToDtoDhl(sinister))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }

    @Transactional
    @Override
    public List<SinisterDhnDTO> getSinistersDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<SinisterJpaEntity> sinisterJpaEntities = sinisterRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                    ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    ClassifierEnum.SinisterDHN_ReportType.getReferenceCode(),
                    ClassifierEnum.SinisterDHN_ReportType.getReferenceCodeType()
            );
            // Mapper data
            return sinisterJpaEntities.stream()
                    .map(sinister -> mapSinisterEntityJpaToDtoDhn(sinister))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;
    }

    public static SinisterJpaEntity mapSinisterDhlDtoToJpaEntityForCreate(SinisterDhlDTO sinisterDhlDTO) {
        return SinisterJpaEntity.builder()
                .sinisterCode(sinisterDhlDTO.getNRO_SINIESTRO())
                .paymentDate(sinisterDhlDTO.getFECHA_PAGO())
                .currentState(sinisterDhlDTO.getESTADO())

                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        sinisterDhlDTO.getITEM_DESGRAVAMEN()
                ))*/
                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(sinisterDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(sinisterDhlDTO.getID_CLIENTE())
                        .build())
                .build();
    }

    public static SinisterJpaEntity mapSinisterDhnDtoToJpaEntityForCreate(SinisterDhnDTO sinisterDhnDTO) {
        return SinisterJpaEntity.builder()
                .sinisterCode(sinisterDhnDTO.getNRO_SINIESTRO())
                .paymentDate(sinisterDhnDTO.getFECHA_PAGO())
                .currentState(sinisterDhnDTO.getESTADO())

                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        sinisterDhnDTO.getITEM_DESGRAVAMEN()
                ))*/
                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(sinisterDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(sinisterDhnDTO.getID_CLIENTE())
                        .build())
                .build();
    }


    public static SinisterDhlDTO mapSinisterEntityJpaToDtoDhl(SinisterJpaEntity sinisterJpaEntity) {
        try {
            ClientJpaEntity client = sinisterJpaEntity.getClient();

            return SinisterDhlDTO.builder()
                    .NRO_SINIESTRO(sinisterJpaEntity.getSinisterCode())
                    .FECHA_PAGO(sinisterJpaEntity.getPaymentDate())
                    .ESTADO(sinisterJpaEntity.getCurrentState())
                    .CI_ASEGURADO(client.getDocumentNumber())
                    .ASEGURADO(client.getFullname())
                    .build()
                    // For JSON API
                    .hiddenRelationship();
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;

    }

    public static SinisterDhnDTO mapSinisterEntityJpaToDtoDhn(SinisterJpaEntity sinisterJpaEntity) {
        try {
            ClientJpaEntity client = sinisterJpaEntity.getClient();

            return SinisterDhnDTO.builder()
                    .NRO_SINIESTRO(sinisterJpaEntity.getSinisterCode())
                    .FECHA_PAGO(sinisterJpaEntity.getPaymentDate())
                    .ESTADO(sinisterJpaEntity.getCurrentState())
                    .CI_ASEGURADO(client.getDocumentNumber())
                    .ASEGURADO(client.getFullname())
                    .build()
                    // For JSON API
                    .hiddenRelationship();
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;


    }


}
