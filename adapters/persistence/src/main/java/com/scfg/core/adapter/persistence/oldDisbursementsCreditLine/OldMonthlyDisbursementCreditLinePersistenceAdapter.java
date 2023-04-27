package com.scfg.core.adapter.persistence.oldDisbursementsCreditLine;


import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.application.port.out.OldMonthlyDisbursementCreditLinePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class OldMonthlyDisbursementCreditLinePersistenceAdapter implements OldMonthlyDisbursementCreditLinePort {

    private final OldMonthlyDisbursementCreditLineRepository oldMonthlyDisbursementCreditLineRepository;


    @Override
    public PersistenceResponse registerOldMonthlyDisbursementsForRegulatedPolicy(List<OldMonthlyDisbursementCreditLineDhlDTO> oldMonthlyDisbursementCreditLineDhlDTOS, long overwrite) {
        List<OldMonthlyDisbursementCreditLineJpaEntity> lastObservedCaseJpaEntities = oldMonthlyDisbursementCreditLineDhlDTOS.stream()
                .map(oldMonthlyDisbursement -> mapOldMonthlyDisbursementDhlDtoToJpaEntityForCreate(oldMonthlyDisbursement))
                .collect(Collectors.toList());
        oldMonthlyDisbursementCreditLineRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                OldMonthlyDisbursementCreditLineDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerOldMonthlyDisbursementsForNotRegulatedPolicy(List<OldMonthlyDisbursementCreditLineDhnDTO> oldMonthlyDisbursementCreditLineDhnDTOS, long overwrite) {
        List<OldMonthlyDisbursementCreditLineJpaEntity> lastObservedCaseJpaEntities = oldMonthlyDisbursementCreditLineDhnDTOS.stream()
                .map(oldMonthlyDisbursement -> mapOldMonthlyDisbursementDhnDtoToJpaEntityForCreate(oldMonthlyDisbursement))
                .collect(Collectors.toList());
        oldMonthlyDisbursementCreditLineRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                OldMonthlyDisbursementCreditLineDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public List<OldMonthlyDisbursementCreditLineDhlDTO> getOldMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return null;
    }

    @Override
    public List<OldMonthlyDisbursementCreditLineDhnDTO> getOldMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<OldMonthlyDisbursementCreditLineJpaEntity> oldMonthlyDisbursementCreditLineJpaEntities = oldMonthlyDisbursementCreditLineRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCode(),
                ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return oldMonthlyDisbursementCreditLineJpaEntities.stream()
                .map(oldMonthlyDisbursement -> mapOldMonthlyDisbursementJpaEntityToDtoDhn(oldMonthlyDisbursement))
                .collect(Collectors.toList());
    }


    // not working
    public static OldMonthlyDisbursementCreditLineJpaEntity mapOldMonthlyDisbursementDhlDtoToJpaEntityForCreate(OldMonthlyDisbursementCreditLineDhlDTO oldMonthlyDisbursementCreditLineDhlDTO) {
        return null;

    }

    public static OldMonthlyDisbursementCreditLineJpaEntity mapOldMonthlyDisbursementDhnDtoToJpaEntityForCreate(OldMonthlyDisbursementCreditLineDhnDTO oldMonthlyDisbursementCreditLineDhnDTO) {
        return OldMonthlyDisbursementCreditLineJpaEntity.builder()
                .userManager(oldMonthlyDisbursementCreditLineDhnDTO.getUSUARIO())
                .policyNumber(oldMonthlyDisbursementCreditLineDhnDTO.getNRO_POLIZA())
                .contractor(oldMonthlyDisbursementCreditLineDhnDTO.getCONTRATANTE())
                .portfolio(oldMonthlyDisbursementCreditLineDhnDTO.getCARTERA())

                /*.clientFullName(oldMonthlyDisbursementCreditLineDhnDTO.getNOMBRE_COMPLETO())
                .clienCi(oldMonthlyDisbursementCreditLineDhnDTO.getCI())
                .dateBirth(oldMonthlyDisbursementCreditLineDhnDTO.getFECHA_NACIMIENTO())*/

                /*.requestNumber(oldMonthlyDisbursementCreditLineDhnDTO.getNRO_SOLICITUD())
                .requestDate(oldMonthlyDisbursementCreditLineDhnDTO.getFECHA_SOLICITUD())*/

                .state(oldMonthlyDisbursementCreditLineDhnDTO.getESTADO())
                .acceptance(oldMonthlyDisbursementCreditLineDhnDTO.getACEPTACION())
                .extraPremiumRate(oldMonthlyDisbursementCreditLineDhnDTO.getPORCENTAJE_EXTRAPRIMA())

                //.currencyType(oldMonthlyDisbursementCreditLineDhnDTO.getMONEDA())

                .requestAmount(oldMonthlyDisbursementCreditLineDhnDTO.getMONTO_SOLICITADO())
                .accumulatedAmount(oldMonthlyDisbursementCreditLineDhnDTO.getMONTO_ACUMULADO())
                .companyPositionDate(oldMonthlyDisbursementCreditLineDhnDTO.getFECHA_POSICION_COMPANIA())


                .requestAttached(oldMonthlyDisbursementCreditLineDhnDTO.getADJUNTO_SOLICITUD())
                //.djsDate(oldMonthlyDisbursementCreditLineDhnDTO.getFECHA_DJS())
                .certificateAttached(oldMonthlyDisbursementCreditLineDhnDTO.getADJUNTO_CERTIFICADO())
                .certificatedDate(oldMonthlyDisbursementCreditLineDhnDTO.getFECHA_CERTIFICADO())

                // For relationship
                .regional(ClassifierJpaEntity.builder()
                        .id(oldMonthlyDisbursementCreditLineDhnDTO.getID_REGIONAL())
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(oldMonthlyDisbursementCreditLineDhnDTO.getID_AGENCIA()))
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(oldMonthlyDisbursementCreditLineDhnDTO.hiddenRelationship().getID_SOLICITUD_SEGURO())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(oldMonthlyDisbursementCreditLineDhnDTO.getID_MONEDA())
                        .build())

                // For create
                .mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        oldMonthlyDisbursementCreditLineDhnDTO.getITEM_DESGRAVAMEN()
                ))

                .build();
    }

    public static OldMonthlyDisbursementCreditLineDhlDTO mapOldMonthlyDisbursementJpaEntityToDtoDhl(OldMonthlyDisbursementCreditLineJpaEntity oldMonthlyDisbursementCreditLineJpaEntity) {
        return null;
    }

    public static OldMonthlyDisbursementCreditLineDhnDTO mapOldMonthlyDisbursementJpaEntityToDtoDhn(OldMonthlyDisbursementCreditLineJpaEntity oldMonthlyDisbursementCreditLineJpaEntity) {
        ClassifierJpaEntity regional = oldMonthlyDisbursementCreditLineJpaEntity.getRegional();
        ClientJpaEntity client = oldMonthlyDisbursementCreditLineJpaEntity.getClient();
        AgencyJpaEntity agency = oldMonthlyDisbursementCreditLineJpaEntity.getAgency();
        InsuranceRequestJpaEntity insuranceRequest = oldMonthlyDisbursementCreditLineJpaEntity.getInsuranceRequest();
        ClassifierJpaEntity currency = oldMonthlyDisbursementCreditLineJpaEntity.getCurrency();

        return OldMonthlyDisbursementCreditLineDhnDTO.builder()
                .USUARIO(oldMonthlyDisbursementCreditLineJpaEntity.getUserManager())
                .NRO_POLIZA(oldMonthlyDisbursementCreditLineJpaEntity.getPolicyNumber())
                .CONTRATANTE(oldMonthlyDisbursementCreditLineJpaEntity.getContractor())
                .CARTERA(oldMonthlyDisbursementCreditLineJpaEntity.getPortfolio())
                .NOMBRE_COMPLETO(client.getFullname())
                .CI(client.getDocumentNumber())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .NRO_SOLICITUD(insuranceRequest.getRequestNumber())
                .FECHA_SOLICITUD(insuranceRequest.getInsuranceRequestDate())
                .ESTADO(oldMonthlyDisbursementCreditLineJpaEntity.getState())
                .ACEPTACION(oldMonthlyDisbursementCreditLineJpaEntity.getAcceptance())
                .PORCENTAJE_EXTRAPRIMA(oldMonthlyDisbursementCreditLineJpaEntity.getExtraPremiumRate())
                .MONEDA(currency.getDescription())
                .MONTO_SOLICITADO(oldMonthlyDisbursementCreditLineJpaEntity.getRequestAmount())
                .MONTO_ACUMULADO(oldMonthlyDisbursementCreditLineJpaEntity.getAccumulatedAmount())
                .FECHA_POSICION_COMPANIA(oldMonthlyDisbursementCreditLineJpaEntity.getCompanyPositionDate())
                .REGIONAL(regional.getDescription())
                .AGENCIA(agency.getDESCRIPTION())
                .ADJUNTO_SOLICITUD(oldMonthlyDisbursementCreditLineJpaEntity.getRequestAttached())
                .FECHA_DJS(insuranceRequest.getDjsFillDate())
                .ADJUNTO_CERTIFICADO(oldMonthlyDisbursementCreditLineJpaEntity.getCertificateAttached())
                .FECHA_CERTIFICADO(oldMonthlyDisbursementCreditLineJpaEntity.getCertificatedDate())
                .build()
                // For JSON API
                .hiddenRelationship();
    }
}
