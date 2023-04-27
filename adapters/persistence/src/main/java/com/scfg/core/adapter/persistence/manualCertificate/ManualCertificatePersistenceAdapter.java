package com.scfg.core.adapter.persistence.manualCertificate;


import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.ManagerJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.application.port.out.ManualCertificatePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhnDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ManualCertificatePersistenceAdapter implements ManualCertificatePort {

    private final ManualCertificateRepository manualCertificateRepository;


    @Override
    public PersistenceResponse registerManualCertificatesForRegulatedPolicy(List<ManualCertificateDhlDTO> manualCertificatesDhn, long overwrite) {
        List<ManualCertificateJpaEntity> lastObservedCaseJpaEntities = manualCertificatesDhn.stream()
                .map(manualCertificate -> mapManualCertificateDhlDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        manualCertificateRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                ManualCertificateDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }


    @Override
    public PersistenceResponse registerManualCertificatesForNotRegulatedPolicy(List<ManualCertificateDhnDTO> manualCertificatesDhn, long overwrite) {
        List<ManualCertificateJpaEntity> lastObservedCaseJpaEntities = manualCertificatesDhn.stream()
                .map(manualCertificate -> mapManualCertificateDhnDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        manualCertificateRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                ManualCertificateDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }


    @Override
    public List<ManualCertificateDhlDTO> getManualCertificateDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<ManualCertificateJpaEntity> manualCertificateJpaEntities = manualCertificateRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCode(),
                ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return manualCertificateJpaEntities.stream()
                .map(manualCertificate -> mapManualCertificateJpaEntityToDtoDhl(manualCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManualCertificateDhnDTO> getManualCertificateDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<ManualCertificateJpaEntity> manualCertificateJpaEntities = manualCertificateRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCode(),
                ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return manualCertificateJpaEntities.stream()
                .map(manualCertificate -> mapManualCertificateJpaEntityToDtoDhn(manualCertificate))
                .collect(Collectors.toList());
    }


    public static ManualCertificateJpaEntity mapManualCertificateDhlDtoToJpaEntityForCreate(ManualCertificateDhlDTO manualCertificateDhlDTO) {
        return ManualCertificateJpaEntity.builder()

                .policyType(manualCertificateDhlDTO.getTIPO_POLIZA())
                .creditOperationNumber(manualCertificateDhlDTO.getNRO_OPERACION())
                .weight(manualCertificateDhlDTO.getPESO())
                .heightCm(manualCertificateDhlDTO.getESTATURA())
                .requestedAmount(manualCertificateDhlDTO.getMONTO_SOLICITADO_BS())
                .accumulatedAmount(manualCertificateDhlDTO.getMONTO_ACUMULADO_BS())
                .crediTermDays(manualCertificateDhlDTO.getPLAZO_CREDITO())
                .daysMonthsYears(manualCertificateDhlDTO.getDIAS_MESES_ANHOS())
                .insuredValue(manualCertificateDhlDTO.getVALOR_ASEGURADO())
                .ratePremium(manualCertificateDhlDTO.getTASAX())
                .rateExtrapremium(manualCertificateDhlDTO.getTASA_EXTRAPRIMA())
                .premium(manualCertificateDhlDTO.getPRIMA_BS())
                .rateExtrapremiumBank(manualCertificateDhlDTO.getTASA_EXTRAPRIMA_BANCO())
                .extraPremium(manualCertificateDhlDTO.getEXTRAPRIMA_BS())
                /*.djsManualNumber(manualCertificateDhlDTO.getNRO_DJS_MANUAL())

                .registrationDateDjs(manualCertificateDhlDTO.getFECHA_LLENADO_DJS())

                .clientFullName(manualCertificateDhlDTO.getNOMBRE())
                .clientCi(manualCertificateDhlDTO.getCI())
                .dateBirth(manualCertificateDhlDTO.getFECHA_NACIMIENTO())
                .gender(manualCertificateDhlDTO.getGENERO())
                .nationality(manualCertificateDhlDTO.getNACIONALIDAD())
                .manager(manualCertificateDhlDTO.getGESTOR())
                .agency(manualCertificateDhlDTO.getAGENCIA())
                .currencyType(manualCertificateDhlDTO.getMONEDA())

                .creditType(manualCertificateDhlDTO.getTIPO_CREDITO())
                .acceptandeDate(manualCertificateDhlDTO.getFECHA_ACEPTACION())
                .certicateNumber(manualCertificateDhlDTO.getNRO_CERTIFICADO())
                .disbusementDate(manualCertificateDhlDTO.getFECHA_DESEMBOLSO())
                .coverage(manualCertificateDhlDTO.getCOBERTURA())*/


                // For relationship
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_SOLICITUD_SEGURO())
                        .build()
                )
                .client(ClientJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_CLIENTE())
                        .build())
                .manager(ManagerJpaEntity.builder()
                        .MANAGER_ID(BigInteger.valueOf(manualCertificateDhlDTO.getID_GESTOR()))
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(manualCertificateDhlDTO.getID_AGENCIA()))
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_MONEDA())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_COBERTURA())
                        .build())
                .creditType(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_TIPO_CREDITO())
                        .build())

                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(manualCertificateDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For create
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        manualCertificateDhlDTO.getITEM_DESGRAVAMEN()
                ))*/
                .build();
    }

    public static ManualCertificateJpaEntity mapManualCertificateDhnDtoToJpaEntityForCreate(ManualCertificateDhnDTO manualCertificateDhnDTO) {
        return ManualCertificateJpaEntity.builder()

                .policyType(manualCertificateDhnDTO.getTIPO_POLIZA())
                .creditOperationNumber(manualCertificateDhnDTO.getNRO_OPERACION())
                .weight(manualCertificateDhnDTO.getPESO())
                .heightCm(manualCertificateDhnDTO.getESTATURA_CM())
                .requestedAmount(manualCertificateDhnDTO.getMONTO_SOLICITADO_BS())
                .accumulatedAmount(manualCertificateDhnDTO.getMONTO_ACUMULADO_BS())
                .crediTermDays(manualCertificateDhnDTO.getPLAZO_CREDITO())
                .daysMonthsYears(manualCertificateDhnDTO.getDIAS_MESES_ANHOS())
                .insuredValue(manualCertificateDhnDTO.getVALOR_ASEGURADO())
                .ratePremium(manualCertificateDhnDTO.getTASAX())
                .rateExtrapremium(manualCertificateDhnDTO.getTASA_EXTRAPRIMA())
                .premium(manualCertificateDhnDTO.getPRIMA_BS())
                .rateExtrapremiumBank(manualCertificateDhnDTO.getTASA_EXTRAPRIMA_BANCO())
                .extraPremium(manualCertificateDhnDTO.getEXTRAPRIMA_BS())
                /*.djsManualNumber(manualCertificateDhlDTO.getNRO_DJS_MANUAL())

                .registrationDateDjs(manualCertificateDhlDTO.getFECHA_LLENADO_DJS())

                .clientFullName(manualCertificateDhlDTO.getNOMBRE())
                .clientCi(manualCertificateDhlDTO.getCI())
                .dateBirth(manualCertificateDhlDTO.getFECHA_NACIMIENTO())
                .gender(manualCertificateDhlDTO.getGENERO())
                .nationality(manualCertificateDhlDTO.getNACIONALIDAD())
                .manager(manualCertificateDhlDTO.getGESTOR())
                .agency(manualCertificateDhlDTO.getAGENCIA())
                .currencyType(manualCertificateDhlDTO.getMONEDA())

                .creditType(manualCertificateDhlDTO.getTIPO_CREDITO())
                .acceptandeDate(manualCertificateDhlDTO.getFECHA_ACEPTACION())
                .certicateNumber(manualCertificateDhlDTO.getNRO_CERTIFICADO())
                .disbusementDate(manualCertificateDhlDTO.getFECHA_DESEMBOLSO())
                .coverage(manualCertificateDhlDTO.getCOBERTURA())*/


                // For relationship
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        manualCertificateDhnDTO.getITEM_DESGRAVAMEN()
                ))*/

                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_SOLICITUD_SEGURO())
                        .build()
                )
                .client(ClientJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_CLIENTE())
                        .build())
                .manager(ManagerJpaEntity.builder()
                        .MANAGER_ID(BigInteger.valueOf(manualCertificateDhnDTO.getID_GESTOR()))
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(manualCertificateDhnDTO.getID_AGENCIA()))
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_MONEDA())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_COBERTURA())
                        .build())
                .creditType(ClassifierJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_TIPO_CREDITO())
                        .build())

                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(manualCertificateDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())

                .build();
    }


    public static ManualCertificateDhlDTO mapManualCertificateJpaEntityToDtoDhl(ManualCertificateJpaEntity manualCertificateJpaEntity) {
        InsuranceRequestJpaEntity insuranceRequest = manualCertificateJpaEntity.getInsuranceRequest();
        ClientJpaEntity client = manualCertificateJpaEntity.getClient();
        ManagerJpaEntity manager = manualCertificateJpaEntity.getManager();
        AgencyJpaEntity agency = manualCertificateJpaEntity.getAgency();
        ClassifierJpaEntity currency = manualCertificateJpaEntity.getCurrency();
        ClassifierJpaEntity coverage = manualCertificateJpaEntity.getCoverage();
        ClassifierJpaEntity creditType = manualCertificateJpaEntity.getCreditType();
        ClassifierJpaEntity insuranceRequestStatus = insuranceRequest.getRequestStatus();

        return ManualCertificateDhlDTO.builder()
                .NRO_DJS_MANUAL(insuranceRequest.getRequestNumber())
                .TIPO_POLIZA(manualCertificateJpaEntity.getPolicyType())
                .FECHA_LLENADO_DJS(insuranceRequest.getDjsFillDate())
                .NRO_OPERACION(manualCertificateJpaEntity.getCreditOperationNumber())
                .NOMBRE(client.getFullname())
                .CI(client.getDocumentNumber())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .GENERO(client.getGender())
                .NACIONALIDAD(client.getNationality())
                .PESO(manualCertificateJpaEntity.getWeight())
                .ESTATURA(manualCertificateJpaEntity.getHeightCm())
                .GESTOR(manager.getCARGO())
                .AGENCIA(agency.getDESCRIPTION())
                .MONEDA(currency.getDescription())
                .MONTO_SOLICITADO_BS(manualCertificateJpaEntity.getRequestedAmount())
                .MONTO_ACUMULADO_BS(manualCertificateJpaEntity.getAccumulatedAmount())
                .PLAZO_CREDITO(manualCertificateJpaEntity.getCrediTermDays())
                .DIAS_MESES_ANHOS(manualCertificateJpaEntity.getDaysMonthsYears())
                .TIPO_CREDITO(creditType.getDescription())
                .FECHA_ACEPTACION(insuranceRequest.getInclusionDate())
                .NRO_CERTIFICADO(insuranceRequest.getRequestNumber())
                .ESTADO_SOLICITUD(insuranceRequestStatus.getDescription())
                .FECHA_DESEMBOLSO(manualCertificateJpaEntity.getInsuranceRequest().getDisbursementDate())
                .VALOR_ASEGURADO(manualCertificateJpaEntity.getInsuredValue())
                .TASAX(manualCertificateJpaEntity.getRatePremium())
                .COBERTURA(coverage.getDescription())
                .TASA_EXTRAPRIMA(manualCertificateJpaEntity.getRateExtrapremium())
                .PRIMA_BS(manualCertificateJpaEntity.getPremium())
                .TASA_EXTRAPRIMA_BANCO(manualCertificateJpaEntity.getRateExtrapremiumBank())
                .EXTRAPRIMA_BS(manualCertificateJpaEntity.getExtraPremium())
                .build()
                // For JSON API
                .hiddenRelationship();
    }

    public static ManualCertificateDhnDTO mapManualCertificateJpaEntityToDtoDhn(ManualCertificateJpaEntity manualCertificateJpaEntity) {
        InsuranceRequestJpaEntity insuranceRequest = manualCertificateJpaEntity.getInsuranceRequest();
        ClientJpaEntity client = manualCertificateJpaEntity.getClient();
        ManagerJpaEntity manager = manualCertificateJpaEntity.getManager();
        AgencyJpaEntity agency = manualCertificateJpaEntity.getAgency();
        ClassifierJpaEntity currency = manualCertificateJpaEntity.getCurrency();
        ClassifierJpaEntity coverage = manualCertificateJpaEntity.getCoverage();
        ClassifierJpaEntity creditType = manualCertificateJpaEntity.getCreditType();
        ClassifierJpaEntity insuranceRequestStatus = insuranceRequest.getRequestStatus();


        return ManualCertificateDhnDTO.builder()
                .NRO_DJS_MANUAL(insuranceRequest.getRequestNumber())
                .TIPO_POLIZA(manualCertificateJpaEntity.getPolicyType())
                .FECHA_LLENADO_DJS(insuranceRequest.getDjsFillDate())
                .NRO_OPERACION(manualCertificateJpaEntity.getCreditOperationNumber())
                .NOMBRE_COMPLETO(client.getFullname())
                .CI(client.getDocumentNumber())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .GENERO(client.getGender())
                .NACIONALIDAD(client.getNationality())
                .PESO(manualCertificateJpaEntity.getWeight())
                .ESTATURA_CM(manualCertificateJpaEntity.getHeightCm())
                .GESTOR(manager.getCARGO())
                .AGENCIA(agency.getDESCRIPTION())
                .MONEDA(currency.getDescription())
                .MONTO_SOLICITADO_BS(manualCertificateJpaEntity.getRequestedAmount())
                .MONTO_ACUMULADO_BS(manualCertificateJpaEntity.getAccumulatedAmount())
                .PLAZO_CREDITO(manualCertificateJpaEntity.getCrediTermDays())
                .DIAS_MESES_ANHOS(manualCertificateJpaEntity.getDaysMonthsYears())
                .TIPO_CREDITO(creditType.getDescription())
                .FECHA_ACEPTACION(insuranceRequest.getInclusionDate())
                .NRO_CERTIFICADO(insuranceRequest.getRequestNumber())
                .ESTADO_SOLICITUD(insuranceRequestStatus.getDescription())
                .FECHA_DESEMBOLSO(manualCertificateJpaEntity.getInsuranceRequest().getDisbursementDate())
                .VALOR_ASEGURADO(manualCertificateJpaEntity.getInsuredValue())
                .TASAX(manualCertificateJpaEntity.getRatePremium())
                .COBERTURA(coverage.getDescription())
                .TASA_EXTRAPRIMA(manualCertificateJpaEntity.getRateExtrapremium())
                .PRIMA_BS(manualCertificateJpaEntity.getPremium())
                .TASA_EXTRAPRIMA_BANCO(manualCertificateJpaEntity.getRateExtrapremiumBank())
                .EXTRAPRIMA_BS(manualCertificateJpaEntity.getExtraPremium())
                .build()
                // For JSON API
                .hiddenRelationship();
    }
}
