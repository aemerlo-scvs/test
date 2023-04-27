package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ConsolidatedObservedCaseUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ObservedCaseNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Client;
import com.scfg.core.domain.common.CreditOperation;
import com.scfg.core.domain.common.InsuranceRequest;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.scfg.core.common.util.HelpersConstants.*;


@UseCase
@RequiredArgsConstructor
public class ConsolidatedObservedCaseService implements ConsolidatedObservedCaseUseCase {

    private final ConsolidatedObservedCasePort consolidatedObservedCasePort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final ClientPort clientPort;
    private final InsuranceRequestPort insuranceRequestPort;
    private final CreditOperationPort creditOperationPort;
    private final ObservedCasePort observedCasePort;
    private final ClassifierPort classifierPort;
    private final ClassifierTypePort classifierTypePort;
    private long rollbackMortgageReliefItem =1 ;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ObservedCaseNotFoundException.class,
            Exception.class,
            ClassifierNotFoundException.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveConsolidatedObservedCaseForRegulatedPolicy(
            long monthId, long yearId, long insurancePolicyHolderId, long usersId, long policyTypeId, long reportTypeId,
            List<ConsolidatedObservedCaseDhlDTO> consolidatedObservedCaseDhlDTOList, long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
        List<String> prevObservedCaseErrors = new ArrayList<>();

        consolidatedObservedCaseDhlDTOList.stream()
                .forEach(consolidatedObservedCase -> {

                    long creditOperationId = 0, clientId = 0, insuranceRequestId = 0;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    Map<String, Object> clientMap, insuranceRequestMap, creditOperationMap;
                    try {

                        long documentTypeId = classifierTypePort.getClassifierTypeByReferenceId(ClassifierTypeEnum.DocumentType.getReferenceId())
                                .getId();

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs.", ClassifierTypeEnum.Currency);

                        Classifier insuranceRequestStatusActiveCls = classifierPort.getClassifierByName(consolidatedObservedCase.getESTADO_SOLICITUD(), ClassifierTypeEnum.DHInsuranceRequestStatus);



                        // Client
                        Client client = Client.builder()
                                .documentNumber(consolidatedObservedCase.getCI())
                                .documentType(documentTypeCiCls)
                                .currency(currencyBsCls)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Insurance request

                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(consolidatedObservedCase.getNRO_SOL_WEB())
                                .disbursementDate(consolidatedObservedCase.getFECHA_DESEMBOLSO())
                                .accumulatedAmount(consolidatedObservedCase.getACUMULADO())
                                .currency(currencyBsCls)
                                .requestStatus(insuranceRequestStatusActiveCls) // provisional, obtener estado
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();

                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(consolidatedObservedCase.getNRO_OPERACION())
                                .disbursementDate(consolidatedObservedCase.getFECHA_DESEMBOLSO())
                                .disbursedAmount(consolidatedObservedCase.getDESEMBOLSOS_MES_ACTUAL())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest) // prev save
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/

                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(consolidatedObservedCase.getNRO_OPERACION())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();


                        // validate persistence
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // Observed Case
                        List<Long> mortgageReliefItemsIds = mortgageReliefItemPort.getMortgageReliefItemsByIDs(monthId, yearId, reportTypeId,
                                policyTypeId, insurancePolicyHolderId).stream()
                                .map(MortgageReliefItem::getId)
                                .collect(Collectors.toList());

                        long observedCaseId = observedCasePort.getObservedCaseByClientIdAndCreditOperationIdAndMortgageReliefItemIds(
                                clientId, creditOperationId, mortgageReliefItemsIds).getId();

                        consolidatedObservedCase.setID_SOLICITUD(insuranceRequestId);
                        consolidatedObservedCase.setID_SOLICITUD(insuranceRequestId);
                        consolidatedObservedCase.setID_CASO_OBSERVADO(observedCaseId);
                        consolidatedObservedCase.setID_TIPO_MONEDA(currencyBsCls.getId());


                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        insuranceRequestStatusErrors.add(consolidatedObservedCase.getESTADO_SOLICITUD());
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    } catch (ObservedCaseNotFoundException observedCaseNotFoundException) {
                        String tuple = "Nro. Operacion: " + consolidatedObservedCase.getNRO_OPERACION() + ", Nro. Solicitud: " + consolidatedObservedCase.getNRO_SOL_WEB();
                        prevObservedCaseErrors.add(tuple);
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });

        if (prevObservedCaseErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
            if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION) {
                // disable last registers
                mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);
            }
            return consolidatedObservedCasePort.registerConsolidatedObservedCaseForRegulatedPolicy(consolidatedObservedCaseDhlDTOList, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.DHInsuranceRequestStatus
                    .getName(), insuranceRequestStatusErrors);
            errors.put("Casos observados previos", prevObservedCaseErrors);
            return persistenceResponse.setData(errors);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ObservedCaseNotFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveConsolidatedObservedCaseForNotRegulatedPolicy(
            long monthId, long yearId, long insurancePolicyHolderId, long usersId, long policyTypeId, long reportTypeId,
            List<ConsolidatedObservedCaseDhnDTO> consolidatedObservedCaseDhnDTOList, long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
        List<String> prevObservedCaseErrors = new ArrayList<>();

        consolidatedObservedCaseDhnDTOList.stream()
                .forEach(consolidatedObservedCase -> {

                    Map<String, Object> clientMap, insuranceRequestMap, creditOperationMap;

                    long creditOperationId = 0, clientId = 0, insuranceRequestId = 0;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;
                    try {

                        long documentTypeId = classifierTypePort.getClassifierTypeByReferenceId(ClassifierTypeEnum.DocumentType.getReferenceId())
                                .getId();

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs.", ClassifierTypeEnum.Currency);

                        Classifier insuranceRequestStatusActiveIdc = classifierPort.getClassifierByName(consolidatedObservedCase.getESTADO_SOLICITUD(), ClassifierTypeEnum.DHInsuranceRequestStatus);

                        // Client
                        Client client = Client.builder()
                                .documentNumber(consolidatedObservedCase.getCI())
                                .documentType(documentTypeCiCls)
                                .currency(currencyBsCls)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Insurance request
                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(consolidatedObservedCase.getNRO_SOLICITUD_WEB())
                                .disbursementDate(consolidatedObservedCase.getFECHA_DESEMBOLSO())
                                .accumulatedAmount(consolidatedObservedCase.getMONTO_ACUMULADO())
                                .currency(currencyBsCls)
                                .requestStatus(insuranceRequestStatusActiveIdc) // provisional, obtener estado
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();

                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(consolidatedObservedCase.getNRO_OPERACION())
                                .disbursementDate(consolidatedObservedCase.getFECHA_DESEMBOLSO())
                                .disbursedAmount(consolidatedObservedCase.getDESEMBOLSOS_MES_ACTUAL())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest) // prev save
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(consolidatedObservedCase.getNRO_OPERACION())
                                .currency(currencyBsCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // validate persistence
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // Observed Case
                        List<Long> mortgageReliefItemsIds = mortgageReliefItemPort.getMortgageReliefItemsByIDs(monthId, yearId, reportTypeId,
                                policyTypeId, insurancePolicyHolderId).stream()
                                .map(MortgageReliefItem::getId)
                                .collect(Collectors.toList());

                        long observedCaseId = observedCasePort.getObservedCaseByClientIdAndCreditOperationIdAndMortgageReliefItemIds(
                                clientId, creditOperationId, mortgageReliefItemsIds).getId();

                        consolidatedObservedCase.setID_SOLICITUD(insuranceRequestId);
                        consolidatedObservedCase.setID_CASO_OBSERVADO(observedCaseId);
                        consolidatedObservedCase.setID_TIPO_MONEDA(currencyBsCls.getId());

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        insuranceRequestStatusErrors.add(consolidatedObservedCase.getESTADO_SOLICITUD());
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                        // remove entities relationship
                    } catch (ObservedCaseNotFoundException observedCaseNotFoundException) {
                        String tuple = "Nro. Operacion: " + consolidatedObservedCase.getNRO_OPERACION() + ", Nro. Solicitud: " + consolidatedObservedCase.getNRO_SOLICITUD_WEB();
                        prevObservedCaseErrors.add(tuple);
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }

                    }
                });
        if (prevObservedCaseErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
            if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION) {
                // disable last registers
                mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);
            }
            return consolidatedObservedCasePort.registerConsolidatedObservedCaseForNotRegulatedPolicy(consolidatedObservedCaseDhnDTOList, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.DHInsuranceRequestStatus
                    .getName(), insuranceRequestStatusErrors);
            errors.put("Casos observados previos", prevObservedCaseErrors);

            return persistenceResponse.setData(errors);
        }
    }

    @Override
    public List<ConsolidatedObservedCaseDhlDTO> getConsolidatedObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return consolidatedObservedCasePort.getConsolidatedObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<ConsolidatedObservedCaseDhnDTO> getConsolidatedObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return consolidatedObservedCasePort.getConsolidatedObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
