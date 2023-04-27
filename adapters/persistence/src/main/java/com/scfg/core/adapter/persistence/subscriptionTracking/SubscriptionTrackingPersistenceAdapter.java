package com.scfg.core.adapter.persistence.subscriptionTracking;


import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.ManagerJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.application.port.out.SubscriptionTrackingPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhnDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class SubscriptionTrackingPersistenceAdapter implements SubscriptionTrackingPort {

    private final SubscriptionTrackingRepository subscriptionTrackingRepository;

    @Override
    public PersistenceResponse registerSubscriptionsTrackingForRegulatedPolicy(List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS, long overwrite) {
        List<SubscriptionTrackingJpaEntity> lastObservedCaseJpaEntities = subscriptionTrackingDhlDTOS.stream()
                .map(subscriptionTracking -> mapSubscriptionTrackingDhlDtoToJpaEntityForCreate(subscriptionTracking))
                .collect(Collectors.toList());
        subscriptionTrackingRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                SubscriptionTrackingDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerSubscriptionsTrackingForNotRegulatedPolicy(List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS, long overwrite) {
        List<SubscriptionTrackingJpaEntity> lastObservedCaseJpaEntities = subscriptionTrackingDhnDTOS.stream()
                .map(subscriptionTracking -> mapSubscriptionTrackingDhnDtoToJpaEntityForCreate(subscriptionTracking))
                .collect(Collectors.toList());
        subscriptionTrackingRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                SubscriptionTrackingDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public List<SubscriptionTrackingDhlDTO> getSubscriptionTrackingDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntities = subscriptionTrackingRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCode(),
                ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return subscriptionTrackingJpaEntities.stream()
                .map(subscriptionTracking -> mapSubscriptionTrackingJpaEntityToDtoDhl(subscriptionTracking))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionTrackingDhnDTO> getSubscriptionTrackingDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntities = subscriptionTrackingRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCode(),
                ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return subscriptionTrackingJpaEntities.stream()
                .map(subscriptionTracking -> mapSubscriptionTrackingJpaEntityToDtoDhn(subscriptionTracking))
                .collect(Collectors.toList());
    }

    public static SubscriptionTrackingJpaEntity mapSubscriptionTrackingDhlDtoToJpaEntityForCreate(SubscriptionTrackingDhlDTO subscriptionTrackingDhlDTO) {
        return SubscriptionTrackingJpaEntity.builder()
                .correlativeControl(subscriptionTrackingDhlDTO.getCORRELATIVO_CONTROL())
                .item(subscriptionTrackingDhlDTO.getITEM())
                .operationNumber(subscriptionTrackingDhlDTO.getNRO_OPERACION())
                //.dateReceptionRequirementsDjs(subscriptionTrackingDhlDTO.getFECHA_RECEPCION_DJS_REQUERIMIENTO())
                .registrationDateDjs(subscriptionTrackingDhlDTO.getFECHA_LLENADO_DJS())
                /*.statementNumber(subscriptionTrackingDhlDTO.getNRO_DECLARACION())
                .insured(subscriptionTrackingDhlDTO.getASEGURADO())
                .gender(subscriptionTrackingDhlDTO.getGENERO())
                .documentNumber(subscriptionTrackingDhlDTO.getCEDULA_IDENTIDAD())
                .extension(subscriptionTrackingDhlDTO.getEX())
                .birthDate(subscriptionTrackingDhlDTO.getFECHA_NACIMIENTO())
                .currency(subscriptionTrackingDhlDTO.getMONEDA())
                .requestedAmountBs(subscriptionTrackingDhlDTO.getMONTO_SOLICITADO_BS())
                .accumulatedAmountUsd(subscriptionTrackingDhlDTO.getMONTO_ACUMULADO_BS())*/
                .requirements(subscriptionTrackingDhlDTO.getREQUISITOS())
                //.complianceDateRequeriments(subscriptionTrackingDhlDTO.getFECHA_CUMPLIMIENTO_REQUISITOS())
                .state(subscriptionTrackingDhlDTO.getESTADO())
                .reason(subscriptionTrackingDhlDTO.getCAUSA())
                .additionalComments(subscriptionTrackingDhlDTO.getCOMENTARIOS_ADICIONALES())
                /*.bankPronouncementDate(subscriptionTrackingDhlDTO.getFECHA_PRONUNCIAMIENTO_AL_BANCO())
                .disbursementDate(subscriptionTrackingDhlDTO.getFECHA_DESEMBOLSO())*/
                .validateTimeDjs(subscriptionTrackingDhlDTO.getTIEMPO_VALIDEZ_DJS())
                .exclusionLetter(subscriptionTrackingDhlDTO.getCARTA_RECHAZO_EXCLUSION())
                .risk(subscriptionTrackingDhlDTO.getRIESGO())
                .rateExtrapremium(subscriptionTrackingDhlDTO.getPORCENTAJE_EXTRAPRIMA())
                .extraPremiumReason(subscriptionTrackingDhlDTO.getMOTIVO_EXTRAPRIMA())
                .coverageGranted(subscriptionTrackingDhlDTO.getCOBERTURA_OTORGADA())
                .coverageDetail(subscriptionTrackingDhlDTO.getDETALLE_MOTIVO())
                .examPerformed(subscriptionTrackingDhlDTO.getEXAMENES_REALIZADOS())
                /*.office(subscriptionTrackingDhlDTO.getOFICINA())
                .branchOffice(subscriptionTrackingDhlDTO.getSUCURSAL_OFICINA())
                .manager(subscriptionTrackingDhlDTO.getGESTOR())*/
                .requirementType(subscriptionTrackingDhlDTO.getTIPO_REQUERIMIENTO())
                .reinsuranceShipmentDate(subscriptionTrackingDhlDTO.getFECHA_ENVIO_REASEGURO())
                .operationType(subscriptionTrackingDhlDTO.getTIPO_OPERACION())
                .reinsuranceResponseDate(subscriptionTrackingDhlDTO.getFECHA_RESPUESTA())
                .responseTimeReinsurance(subscriptionTrackingDhlDTO.getTIEMPO_RESPUESTA_REASEGURO())
                .insuredCapital(subscriptionTrackingDhlDTO.getCAPITAL_ASEGURADO_BS())
                .level(subscriptionTrackingDhlDTO.getNIVEL())
                .creditTermMonths(subscriptionTrackingDhlDTO.getPLAZO_CREDITO_MESES())
                .broadcastDate(subscriptionTrackingDhlDTO.getFECHA_EMISION())
                .comments(subscriptionTrackingDhlDTO.getCOMENTARIOS())
                .process(subscriptionTrackingDhlDTO.getPROCESO())
                /*.schedulingDate(subscriptionTrackingDhlDTO.getFECHA_AGENDAMIENTO())
                .portfolio(subscriptionTrackingDhlDTO.getCARTERA())*/

                // For relationship
                .regional(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_REGIONAL())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_CLIENTE())
                        .build())
                .manager(ManagerJpaEntity.builder()
                        .MANAGER_ID(BigInteger.valueOf(subscriptionTrackingDhlDTO.getID_GESTOR()))
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(subscriptionTrackingDhlDTO.getID_AGENCIA()))
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_SOLICITUD_SEGURO())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_MONEDA())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_TIPO_COBERTURA())
                        .build())


                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(subscriptionTrackingDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For create

                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        subscriptionTrackingDhlDTO.getITEM_DESGRAVAMEN()
                ))*/

                .build();
    }

    public static SubscriptionTrackingJpaEntity mapSubscriptionTrackingDhnDtoToJpaEntityForCreate(SubscriptionTrackingDhnDTO subscriptionTrackingDhnDTO) {
        return SubscriptionTrackingJpaEntity.builder()
                .correlativeControl(subscriptionTrackingDhnDTO.getCORRELATIVO_CONTROL())
                .item(subscriptionTrackingDhnDTO.getITEM())
                .operationNumber(subscriptionTrackingDhnDTO.getNRO_OPERACION())
                //.dateReceptionRequirementsDjs(subscriptionTrackingDhlDTO.getFECHA_RECEPCION_DJS_REQUERIMIENTO())
                .registrationDateDjs(subscriptionTrackingDhnDTO.getFECHA_LLENADO_DJS())
                /*.statementNumber(subscriptionTrackingDhlDTO.getNRO_DECLARACION())
                .insured(subscriptionTrackingDhlDTO.getASEGURADO())
                .gender(subscriptionTrackingDhlDTO.getGENERO())
                .documentNumber(subscriptionTrackingDhlDTO.getCEDULA_IDENTIDAD())
                .extension(subscriptionTrackingDhlDTO.getEX())
                .birthDate(subscriptionTrackingDhlDTO.getFECHA_NACIMIENTO())
                .currency(subscriptionTrackingDhlDTO.getMONEDA())
                .requestedAmountBs(subscriptionTrackingDhlDTO.getMONTO_SOLICITADO_BS())
                .accumulatedAmountUsd(subscriptionTrackingDhlDTO.getMONTO_ACUMULADO_BS())*/
                .requirements(subscriptionTrackingDhnDTO.getREQUISITOS())
                //.complianceDateRequeriments(subscriptionTrackingDhlDTO.getFECHA_CUMPLIMIENTO_REQUISITOS())
                .state(subscriptionTrackingDhnDTO.getESTADO())
                .reason(subscriptionTrackingDhnDTO.getCAUSA())
                .additionalComments(subscriptionTrackingDhnDTO.getCOMENTARIOS_ADICIONALES())
                /*.bankPronouncementDate(subscriptionTrackingDhlDTO.getFECHA_PRONUNCIAMIENTO_AL_BANCO())
                .disbursementDate(subscriptionTrackingDhlDTO.getFECHA_DESEMBOLSO())*/
                .validateTimeDjs(subscriptionTrackingDhnDTO.getTIEMPO_VALIDEZ_DJS())
                .exclusionLetter(subscriptionTrackingDhnDTO.getCARTA_EXCLUSION())
                .risk(subscriptionTrackingDhnDTO.getRIESGO())
                .rateExtrapremium(subscriptionTrackingDhnDTO.getTASA_EXTRAPRIMA())
                .extraPremiumReason(subscriptionTrackingDhnDTO.getMOTIVO_EXTRAPRIMA())
                .coverageGranted(subscriptionTrackingDhnDTO.getCOBERTURA_OTORGADA())
                .coverageDetail(subscriptionTrackingDhnDTO.getDETALLE_MOTIVO())
                .examPerformed(subscriptionTrackingDhnDTO.getEXAMENES_REALIZADOS())
                /*.office(subscriptionTrackingDhlDTO.getOFICINA())
                .branchOffice(subscriptionTrackingDhlDTO.getSUCURSAL_OFICINA())
                .manager(subscriptionTrackingDhlDTO.getGESTOR())*/
                .requirementType(subscriptionTrackingDhnDTO.getTIPO_REQUERIMIENTO())
                .reinsuranceShipmentDate(subscriptionTrackingDhnDTO.getFECHA_ENVIO_REASEGURO())
                .operationType(subscriptionTrackingDhnDTO.getTIPO_OPERACION())
                .reinsuranceResponseDate(subscriptionTrackingDhnDTO.getFECHA_RESPUESTA())
                .responseTimeReinsurance(subscriptionTrackingDhnDTO.getTIEMPO_RESPUESTA_REASEGURO())
                .insuredCapital(subscriptionTrackingDhnDTO.getCAPITAL_ASEGURADO_BS())
                .level(subscriptionTrackingDhnDTO.getNIVEL())
                .creditTermMonths(subscriptionTrackingDhnDTO.getPLAZO_CREDITO_MESES())
                .broadcastDate(subscriptionTrackingDhnDTO.getFECHA_EMISION())
                .comments(subscriptionTrackingDhnDTO.getCOMENTARIOS())
                .process(subscriptionTrackingDhnDTO.getPROCESO())
                /*.schedulingDate(subscriptionTrackingDhlDTO.getFECHA_AGENDAMIENTO())
                .portfolio(subscriptionTrackingDhlDTO.getCARTERA())*/

                // For relationship
                .regional(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_REGIONAL())
                        .build())
                .client(ClientJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_CLIENTE())
                        .build())
                .manager(ManagerJpaEntity.builder()
                        .MANAGER_ID(BigInteger.valueOf(subscriptionTrackingDhnDTO.getID_GESTOR()))
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(subscriptionTrackingDhnDTO.getID_AGENCIA()))
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_SOLICITUD_SEGURO())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_MONEDA())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_TIPO_COBERTURA())
                        .build())

                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(subscriptionTrackingDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For create
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        subscriptionTrackingDhnDTO.getITEM_DESGRAVAMEN()
                ))*/

                .build();
    }

    public static SubscriptionTrackingDhlDTO mapSubscriptionTrackingJpaEntityToDtoDhl(SubscriptionTrackingJpaEntity subscriptionTrackingJpaEntity) {
        ClassifierJpaEntity regional = subscriptionTrackingJpaEntity.getRegional();
        ClientJpaEntity client = subscriptionTrackingJpaEntity.getClient();
        ManagerJpaEntity manager = subscriptionTrackingJpaEntity.getManager();
        AgencyJpaEntity agency = subscriptionTrackingJpaEntity.getAgency();
        InsuranceRequestJpaEntity insuredRequest = subscriptionTrackingJpaEntity.getInsuranceRequest();
        ClassifierJpaEntity currency = subscriptionTrackingJpaEntity.getCurrency();
        ClassifierJpaEntity coverage = subscriptionTrackingJpaEntity.getCoverage();

        return SubscriptionTrackingDhlDTO.builder()
                .CORRELATIVO_CONTROL(subscriptionTrackingJpaEntity.getCorrelativeControl())
                .ITEM(subscriptionTrackingJpaEntity.getItem())
                .FECHA_RECEPCION_DJS_REQUERIMIENTO(insuredRequest.getDjsReceptionDate())
                .FECHA_LLENADO_DJS(insuredRequest.getDjsFillDate())
                .NRO_DECLARACION(insuredRequest.getRequestNumber())
                .NRO_OPERACION(subscriptionTrackingJpaEntity.getOperationNumber())
                .ASEGURADO(client.getFullname())
                .GENERO(client.getGender())
                .CEDULA_IDENTIDAD(client.getDocumentNumber())
                .EX(client.getExtension())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONEDA(currency.getDescription())
                .MONTO_SOLICITADO_BS(insuredRequest.getRequestedAmount())
                .MONTO_ACUMULADO_BS(insuredRequest.getAccumulatedAmount())
                .REQUISITOS(subscriptionTrackingJpaEntity.getRequirements())
                .FECHA_CUMPLIMIENTO_REQUISITOS(insuredRequest.getFulfillmentRequirementsDate())
                .ESTADO(subscriptionTrackingJpaEntity.getState())
                .CAUSA(subscriptionTrackingJpaEntity.getReason())
                .COMENTARIOS_ADICIONALES(subscriptionTrackingJpaEntity.getAdditionalComments())
                .FECHA_PRONUNCIAMIENTO_AL_BANCO(insuredRequest.getBankPronouncementDate())
                .FECHA_DESEMBOLSO(insuredRequest.getDisbursementDate())
                .TIEMPO_VALIDEZ_DJS(subscriptionTrackingJpaEntity.getValidateTimeDjs())
                .TIEMPO_RESPUESTA(subscriptionTrackingJpaEntity.getResponseTimeReinsurance())
                .CARTA_RECHAZO_EXCLUSION(subscriptionTrackingJpaEntity.getExclusionLetter())
                .RIESGO(subscriptionTrackingJpaEntity.getRisk())
                .PORCENTAJE_EXTRAPRIMA(subscriptionTrackingJpaEntity.getRateExtrapremium())
                .MOTIVO_EXTRAPRIMA(subscriptionTrackingJpaEntity.getExtraPremiumReason())
                //.COBERTURA_OTORGADA(subscriptionTrackingJpaEntity.getCoverageGranted())
                .COBERTURA_OTORGADA(coverage.getDescription())
                .DETALLE_MOTIVO(subscriptionTrackingJpaEntity.getCoverageDetail())
                .EXAMENES_REALIZADOS(subscriptionTrackingJpaEntity.getExamPerformed())
                .OFICINA(regional.getDescription())
                .SUCURSAL_OFICINA(agency.getDESCRIPTION())
                .GESTOR(manager.getCARGO())
                .TIPO_REQUERIMIENTO(subscriptionTrackingJpaEntity.getRequirementType())
                .FECHA_ENVIO_REASEGURO(subscriptionTrackingJpaEntity.getReinsuranceShipmentDate())
                .TIPO_OPERACION(subscriptionTrackingJpaEntity.getOperationType())
                .FECHA_RESPUESTA(subscriptionTrackingJpaEntity.getReinsuranceResponseDate())
                .TIEMPO_RESPUESTA_REASEGURO(subscriptionTrackingJpaEntity.getResponseTimeReinsurance())
                .CAPITAL_ASEGURADO_BS(subscriptionTrackingJpaEntity.getInsuredCapital())
                .NIVEL(subscriptionTrackingJpaEntity.getLevel())
                .PLAZO_CREDITO_MESES(subscriptionTrackingJpaEntity.getCreditTermMonths())
                .FECHA_EMISION(insuredRequest.getInsuranceRequestDate())
                .COMENTARIOS(subscriptionTrackingJpaEntity.getComments())
                .PROCESO(subscriptionTrackingJpaEntity.getProcess())
                .FECHA_AGENDAMIENTO(insuredRequest.getSchedulingDate())

                .build()
                // For JSON API
                .hiddenRelationship();
    }

    public static SubscriptionTrackingDhnDTO mapSubscriptionTrackingJpaEntityToDtoDhn(SubscriptionTrackingJpaEntity subscriptionTrackingJpaEntity) {
        ClassifierJpaEntity regional = subscriptionTrackingJpaEntity.getRegional();
        ClientJpaEntity client = subscriptionTrackingJpaEntity.getClient();
        ManagerJpaEntity manager = subscriptionTrackingJpaEntity.getManager();
        AgencyJpaEntity agency = subscriptionTrackingJpaEntity.getAgency();
        InsuranceRequestJpaEntity insuredRequest = subscriptionTrackingJpaEntity.getInsuranceRequest();
        ClassifierJpaEntity currency = subscriptionTrackingJpaEntity.getCurrency();
        ClassifierJpaEntity coverage = subscriptionTrackingJpaEntity.getCoverage();

        return SubscriptionTrackingDhnDTO.builder()
                .CORRELATIVO_CONTROL(subscriptionTrackingJpaEntity.getCorrelativeControl())
                .ITEM(subscriptionTrackingJpaEntity.getItem())
                .FECHA_RECEPCION_DJS(insuredRequest.getDjsReceptionDate())
                .FECHA_LLENADO_DJS(insuredRequest.getDjsFillDate())
                .NRO_DECLARACION(insuredRequest.getRequestNumber())
                .NRO_OPERACION(subscriptionTrackingJpaEntity.getOperationNumber())
                .ASEGURADO(client.getFullname())
                .GENERO(client.getGender())
                .CI(client.getDocumentNumber())
                .EXTENSION(client.getExtension())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONEDA(currency.getDescription())
                .MONTO_SOLICITADO_BS(insuredRequest.getRequestedAmount())
                .MONTO_ACUMULADO_BS(insuredRequest.getAccumulatedAmount())
                .REQUISITOS(subscriptionTrackingJpaEntity.getRequirements())
                .FECHA_CUMPLIMIENTO_REQUISITOS(insuredRequest.getFulfillmentRequirementsDate())
                .ESTADO(subscriptionTrackingJpaEntity.getState())
                .CAUSA(subscriptionTrackingJpaEntity.getReason())
                .COMENTARIOS_ADICIONALES(subscriptionTrackingJpaEntity.getAdditionalComments())
                .FECHA_PRONUNCIAMIENTO_AL_BANCO(insuredRequest.getBankPronouncementDate())
                .FECHA_DESEMBOLSO(insuredRequest.getDisbursementDate())
                .TIEMPO_VALIDEZ_DJS(subscriptionTrackingJpaEntity.getValidateTimeDjs())
                .TIEMPO_RESPUESTA(subscriptionTrackingJpaEntity.getResponseTimeReinsurance())
                .CARTA_EXCLUSION(subscriptionTrackingJpaEntity.getExclusionLetter())
                .RIESGO(subscriptionTrackingJpaEntity.getRisk())
                .TASA_EXTRAPRIMA(subscriptionTrackingJpaEntity.getRateExtrapremium())
                .MOTIVO_EXTRAPRIMA(subscriptionTrackingJpaEntity.getExtraPremiumReason())
                //.COBERTURA_OTORGADA(subscriptionTrackingJpaEntity.getCoverageGranted())
                .COBERTURA_OTORGADA(coverage.getDescription()).DETALLE_MOTIVO(subscriptionTrackingJpaEntity.getCoverageDetail())
                .EXAMENES_REALIZADOS(subscriptionTrackingJpaEntity.getExamPerformed())
                .OFICINA(regional.getDescription())
                .SUCURSAL_OFICINA(agency.getDESCRIPTION())
                .GESTOR(manager.getCARGO())
                .TIPO_REQUERIMIENTO(subscriptionTrackingJpaEntity.getRequirementType())
                .FECHA_ENVIO_REASEGURO(subscriptionTrackingJpaEntity.getReinsuranceShipmentDate())
                .TIPO_OPERACION(subscriptionTrackingJpaEntity.getOperationType())
                .FECHA_RESPUESTA(subscriptionTrackingJpaEntity.getReinsuranceResponseDate())
                .TIEMPO_RESPUESTA_REASEGURO(subscriptionTrackingJpaEntity.getResponseTimeReinsurance())
                .CAPITAL_ASEGURADO_BS(subscriptionTrackingJpaEntity.getInsuredCapital())
                .NIVEL(subscriptionTrackingJpaEntity.getLevel())
                .PLAZO_CREDITO_MESES(subscriptionTrackingJpaEntity.getCreditTermMonths())
                .FECHA_EMISION(insuredRequest.getInsuranceRequestDate())
                .COMENTARIOS(subscriptionTrackingJpaEntity.getComments())
                .PROCESO(subscriptionTrackingJpaEntity.getProcess())
                .FECHA_AGENDAMIENTO(insuredRequest.getSchedulingDate())

                .build()
                // For JSON API
                .hiddenRelationship();
    }


}
