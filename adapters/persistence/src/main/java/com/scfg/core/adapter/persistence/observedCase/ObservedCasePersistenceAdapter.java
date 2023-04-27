package com.scfg.core.adapter.persistence.observedCase;

import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestRepository;
import com.scfg.core.application.port.out.ObservedCasePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ObservedCaseNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ObservedCase;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ObservedCasePersistenceAdapter implements ObservedCasePort {

    private final InsuranceRequestRepository insuranceRequestRepository;
    private final ObservedCaseRepository observedCaseRepository;

    @Override
    public List<ObservedCase> getAllObservedCases() {
        Object observedCaseList = observedCaseRepository.findAll();
        return (List<ObservedCase>) observedCaseList;
    }

    @Override
    public ObservedCase getObservedCaseById(long observedCaseId) {
        ObservedCaseJpaEntity observedCase = observedCaseRepository.findById(observedCaseId)
                .orElseThrow(() ->
                        new NotDataFoundException("Observed Case: " + observedCaseId + " Not found")
                );
        return mapToDomain(observedCase);
    }

    @Override
    public ObservedCase getObservedCaseByClientIdAndCreditOperationIdAndMortgageReliefItemIds(Long clientId, Long creditOperationId,
                                                                                              List<Long> mortgageReliefItemIds) {
        ObservedCaseJpaEntity observedCaseEntity = observedCaseRepository
                .findByClient_IdAndCreditOperation_IdAndMortgageReliefItem_IdIn(clientId, creditOperationId, mortgageReliefItemIds)
                .stream().findFirst()
                .orElseThrow(() ->
                        new ObservedCaseNotFoundException("Observed Case:  Not found")
                );

        return mapToDomain(observedCaseEntity);
        /*return observedCaseOptional.isPresent()
                ? mapToDomain(observedCaseOptional.get())
                : null;*/
    }

    @Override
    public List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<ObservedCaseJpaEntity> observedCaseJpaEntityList = observedCaseRepository
                    .findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                            ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                            ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                            monthId,
                            yearId,
                            insurancePolicyHolderId,
                            ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType.getReferenceCode(),
                            ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType.getReferenceCodeType()
                    );
            return observedCaseJpaEntityList.stream()
                    .map(observedCaseJpaEntity -> mapPreliminaryObservedCaseEntityJpaToDto(observedCaseJpaEntity))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }

    @Override
    public List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        try {
            List<ObservedCaseJpaEntity> observedCaseJpaEntityList = observedCaseRepository
                    .findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                            ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                            ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                            monthId,
                            yearId,
                            insurancePolicyHolderId,
                            ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCode(),
                            ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType.getReferenceCodeType()
                    );
            return observedCaseJpaEntityList.stream()
                    .map(observedCaseJpaEntity -> mapPreliminaryObservedCaseEntityJpaToDto(observedCaseJpaEntity))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String es = e.getMessage();
        }
        return null;
    }

    @Override
    public PersistenceResponse save(ObservedCase observedCase) {
        return null;
    }

    @Override
    public PersistenceResponse update(ObservedCase observedCase) {
        return null;
    }

    @Override
    public PersistenceResponse delete(ObservedCase observedCase) {
        return null;
    }

    @Override
    public PersistenceResponse saveAll(List<ObservedCase> observedCases) {

        List<ObservedCaseJpaEntity> observedCasesJpaEntities = observedCases.stream()
                .map(observedCase -> mapToJpaEntity(observedCase))
                .collect(Collectors.toList());
        observedCaseRepository.saveAll(observedCasesJpaEntities);
        return new PersistenceResponse(
                ObservedCase.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                null
        );
    }

    public static PreliminaryObservedCaseDTO mapPreliminaryObservedCaseEntityJpaToDto(ObservedCaseJpaEntity observedCase) {
        return PreliminaryObservedCaseDTO.builder()
                .NUMERO_OPERACION(observedCase.getCreditOperation().getOperationNumber())
                .NOMBRE_COMPLETO_ASEGURADO(observedCase.getClient().getFullname())
                .CEDULA_ASEGURADO(observedCase.getClient().getDocumentNumber())
                .COMENTARIOS_MES_ACTUAL(observedCase.getCurrentMonthComments())
                .DESEMBOLSO_MES_ACTUAL(observedCase.getCurrentMonthDisbursement())
                .DESEMBOLSO_MES_ANTERIOR(observedCase.getPreviousMonthDisbursement())
                .MONTO_ACUMULADO(observedCase.getAccumulated())
                .FECHA_DESEMBOLSO(observedCase.getCreditOperation().getDisbursementDate())
                .build();
    }

//    public Request getRequestByRequestNumber(String requestNumber) {
//        RequestJpaEntity request = requestRepository.findByRequestNumber(requestNumber)
//                .orElseThrow(() ->
//                        new NotDataFoundException("Request Number: " + requestNumber + " Not found")
//                );
//
//        return mapToDomain(request);
//    }
//
//    public PersistenceResponse save(Request request) {
//        RequestJpaEntity requestJpaEntity = mapToJpaEntity(request);
//        try {
//            requestJpaEntity = requestRepository.save(requestJpaEntity);
//        } catch (Exception ex) {
//            throw ex;
//        }
//        return new PersistenceResponse(
//                Classifier.class.getSimpleName(),
//                ActionRequest.CREATE,
//                requestJpaEntity
//        );
//    }
//
//    public PersistenceResponse update(Request request) {
//        RequestJpaEntity requestJpaEntity = mapToJpaEntity(request);
//        requestJpaEntity = requestRepository.save(requestJpaEntity);
//        return new PersistenceResponse(
//                Classifier.class.getSimpleName(),
//                ActionRequest.UPDATE,
//                requestJpaEntity
//        );
//    }
//
//    public PersistenceResponse delete(Request request) {
//        RequestJpaEntity  requestJpaEntity = mapToJpaEntity(request);
//        // status for deleted
//        requestJpaEntity.setStatus(PersistenceStatus
//                .DELETED
//                .getValue());
//        requestRepository.save(requestJpaEntity);
//
//        return new PersistenceResponse(
//                Classifier.class.getSimpleName(),
//                ActionRequest.DELETE,
//                null
//        );
//    }

    /***
     * Persist model in database
     * @param observedCase
     * @return
     */
    public static ObservedCaseJpaEntity mapToJpaEntity(ObservedCase observedCase) {
        ObservedCaseJpaEntity resultJPA = new ObservedCaseJpaEntity();
        resultJPA.setId(observedCase.getId());
        resultJPA.setAccumulated(observedCase.getAccumulated());
        resultJPA.setCurrentMonthComments(observedCase.getCurrentMonthComments());
        resultJPA.setCurrentMonthDisbursement(observedCase.getCurrentMonthDisbursement());
        resultJPA.setPreviousMonthDisbursement(observedCase.getPreviousMonthDisbursement());
        // relationship
        resultJPA.setClient(ClientJpaEntity.builder()
                .id(observedCase.getClientId())
                .build());
        resultJPA.setCreditOperation(CreditOperationJpaEntity.builder()
                .id(observedCase.getCreditOperationId())
                .build());
        resultJPA.setMortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                .id(observedCase.getMortgageReliefItemId())
                .build());

        /*resultJPA.setCreatedAt(observedCase.getCreatedAt());
        resultJPA.setLastModifiedAt(observedCase.getLastModifiedAt());*/
        return resultJPA;
    }

    /***
     * Export domain for consume APIs
     * @param observedCaseJpaEntity
     * @return
     */
    public static ObservedCase mapToDomain(ObservedCaseJpaEntity observedCaseJpaEntity) {
        ObservedCase result = new ObservedCase();
        result.setId(observedCaseJpaEntity.getId());
        result.setCurrentMonthDisbursement(observedCaseJpaEntity.getCurrentMonthDisbursement());
        result.setCurrentMonthComments(observedCaseJpaEntity.getCurrentMonthComments());
        result.setPreviousMonthDisbursement(observedCaseJpaEntity.getPreviousMonthDisbursement());
        result.setCreatedAt(observedCaseJpaEntity.getCreatedAt());
        result.setLastModifiedAt(observedCaseJpaEntity.getLastModifiedAt());
        result.setClientId(observedCaseJpaEntity.getClient().getId());
        result.setCreditOperationId(observedCaseJpaEntity.getCreditOperation().getId());
        result.setMortgageReliefItemId(observedCaseJpaEntity.getMortgageReliefItem().getId());
        return result;
    }

    // Mapper fot DTO


}
