package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ObservedCaseUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.CreditOperationNotFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ObservedCaseNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.*;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.scfg.core.common.util.HelpersConstants.*;

@UseCase
@RequiredArgsConstructor
public class ObservedCaseService implements ObservedCaseUseCase {

    private final LastObservedCasePort lastObservedCasePort;
    private final ClassifierPort classifierPort;
    private final ClassifierTypePort classifierTypePort;
    private final CreditOperationPort creditOperationPort;
    private final ClientPort clientPort;
    private final InsuranceRequestPort insuranceRequestPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private long rollbackMortgageReliefItem =1 ;


    // Funcional
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ObservedCaseNotFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class,
            CreditOperationNotFoundException.class
    })
    @Override
    public PersistenceResponse registerLastObservedCasesRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<LastObservedCaseDhlDTO> lastObservedCaseDhlDTOS,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> observationTypeErrors = new ArrayList<>();
        List<String> creditOperationStatusErrors = new ArrayList<>();

        // Save mortgage refief item
        MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                .policyTypeIdc(policyTypeId)
                .monthIdc(monthId)
                .yearIdc(yearId)
                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                .reportTypeIdc(reportTypeId)
                .usersId(usersId)
                .build();

        if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION) {
            // disable last registers
            mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);

            Classifier reportTypeObservedCaseDHL = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHL.getId(), policyTypeId, usersId, insurancePolicyHolderId);
        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        lastObservedCaseDhlDTOS.stream()
                .forEach(observedCase -> {

                    Map<String, Object> clientMap, insuranceRequestMap, creditOperationMap;

                    long observationTypeIdc = 0, creditOperationId = 0,
                            clientId = 0, insuranceRequestId = 0;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {

                        // for classifier (observed case)
                        long documentTypeId = classifierTypePort.getClassifierTypeByReferenceId(ClassifierTypeEnum.DocumentType.getReferenceId())
                                .getId();

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs.", ClassifierTypeEnum.Currency);

                        // status = REGISTERED (just to initialize record)
                        Classifier insuranceRequestStatusActiveCls = classifierPort.getClassifierByReferences(ClassifierEnum.Registered_InsuranceRequestStatus);


                        // Obsevation Type
                        observationTypeIdc = classifierPort.getClassifierByName(observedCase.getTIPO_OBSERVACION(), ClassifierTypeEnum.ExclusionType)
                                .getId();

                        // Client
                        Client client = Client.builder()
                                .documentNumber(observedCase.getNRO_CI())
                                .documentType(documentTypeCiCls)
                                .currency(currencyBsCls)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Insurance Request
                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(observedCase.getNRO_SOLICITUD())
                                .currency(currencyBsCls)
                                .requestStatus(insuranceRequestStatusActiveCls)
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();

                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(observedCase.getNRO_OPERACION())
                                .disbursementDate(observedCase.getFECHA_DESEMBOLSO())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                //.insuranceRequestId(insuranceRequestId) // prev save
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(observedCase.getNRO_OPERACION())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();



                        // validate persistence
                        /*rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;*/

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;


                        //rollbackInsuranceRequest, rollbackInsuranceRequest;

                        // assign relationship
                        observedCase.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        observedCase.setID_OPERACION_CREDITICIA(creditOperationId);
                        observedCase.setID_CLIENTE(clientId);
                        observedCase.setID_TIPO_OBSERVACION(observationTypeIdc);

                        //observedCase.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (CreditOperationNotFoundException opeExc) {
                        creditOperationStatusErrors.add(observedCase.getNRO_OPERACION()
                                .toString());

                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        observationTypeErrors.add(observedCase.getTIPO_OBSERVACION());
                        // rollback related entities
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });
        if (observationTypeErrors.isEmpty() && creditOperationStatusErrors.isEmpty()) {
            return lastObservedCasePort.registerLastObservedCasesRegulatedPolicy(lastObservedCaseDhlDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.ExclusionType
                    .getName(), observationTypeErrors);
            errors.put(CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS,
                    observationTypeErrors);
            return persistenceResponse.setData(errors);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ObservedCaseNotFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class,
            CreditOperationNotFoundException.class
    })
    @Override
    public PersistenceResponse registerLastObservedCasesNotRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<LastObservedCaseDhnDTO> lastObservedCaseDhnDTOS,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);


        Map<String, List<String>> errors = new HashMap<>();
        List<String> observationTypeErrors = new ArrayList<>();
        List<String> creditOperationStatusErrors = new ArrayList<>();

        // Save mortgage refief item
        MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                .policyTypeIdc(policyTypeId)
                .monthIdc(monthId)
                .yearIdc(yearId)
                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                .reportTypeIdc(reportTypeId)
                .usersId(usersId)
                .build();

        if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION) {
            // disable last registers
            mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);

            Classifier reportTypeObservedCaseDHL = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHL.getId(), policyTypeId, usersId, insurancePolicyHolderId);
        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        lastObservedCaseDhnDTOS.stream()
                .forEach(observedCase -> {

                    Map<String, Object> clientMap, insuranceRequestMap, creditOperationMap;

                    long observationTypeIdc = 0, creditOperationId = 0,
                            clientId = 0, insuranceRequestId = 0;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {

                        // for classifier (observed case)
                        long documentTypeId = classifierTypePort.getClassifierTypeByReferenceId(ClassifierTypeEnum.DocumentType.getReferenceId())
                                .getId();

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs.", ClassifierTypeEnum.Currency);

                        // status = REGISTERED (just to initialize record)
                        Classifier insuranceRequestStatusActiveCls = classifierPort.getClassifierByReferences(ClassifierEnum.Registered_InsuranceRequestStatus);

                        // Obsevation Type
                        observationTypeIdc = classifierPort.getClassifierByName(observedCase.getTIPO_OBSERVACION(), ClassifierTypeEnum.ExclusionType)
                                .getId();


                        // Client
                        Client client = Client.builder()
                                .documentNumber(observedCase.getNRO_CI())
                                .documentType(documentTypeCiCls)
                                .currency(currencyBsCls)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();


                        // Insurance Request
                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(observedCase.getNRO_SOLICITUD())
                                .currency(currencyBsCls)
                                .requestStatus(insuranceRequestStatusActiveCls)
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();

                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(observedCase.getNRO_OPERACION())
                                .disbursementDate(observedCase.getFECHA_DESEMBOLSO())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                //.insuranceRequestId(insuranceRequestId) // prev save
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/

                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(observedCase.getNRO_OPERACION())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // validate persistence
                        /*rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;*/

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // assign relationship
                        observedCase.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        observedCase.setID_OPERACION_CREDITICIA(creditOperationId);
                        observedCase.setID_CLIENTE(clientId);
                        observedCase.setID_TIPO_OBSERVACION(observationTypeIdc);

                        //observedCase.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (CreditOperationNotFoundException opeExc) {
                        creditOperationStatusErrors.add(observedCase.getNRO_OPERACION()
                                .toString());

                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        // Classifier Not found
                        observationTypeErrors.add(observedCase.getTIPO_OBSERVACION());
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });

        if (observationTypeErrors.isEmpty() && creditOperationStatusErrors.isEmpty()) {
            return lastObservedCasePort.registerLastObservedCasesnotRegulatedPolicy(lastObservedCaseDhnDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.ExclusionType
                    .getName(), observationTypeErrors);
            errors.put(CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS,
                    observationTypeErrors);
            return persistenceResponse.setData(errors);
        }
    }

    @Override
    public List<LastObservedCaseDhlDTO> getLastObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return lastObservedCasePort.getLastObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<LastObservedCaseDhnDTO> getLastObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return lastObservedCasePort.getLastObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
