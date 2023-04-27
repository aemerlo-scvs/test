package com.scfg.core.application.service;

import com.scfg.core.application.port.in.SubscriptionTrackingUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.MonthlyDisbursementCaseStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.CreditOperationNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Client;
import com.scfg.core.domain.common.CreditOperation;
import com.scfg.core.domain.common.InsuranceRequest;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import com.scfg.core.domain.managers.Agency;
import com.scfg.core.domain.managers.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scfg.core.common.util.HelpersConstants.*;


@UseCase
@RequiredArgsConstructor
public class SubscriptionTrackingService implements SubscriptionTrackingUseCase {

    private final SubscriptionTrackingPort subscriptionTrackingPort;
    private final InsuranceRequestPort insuranceRequestPort;
    private final CreditOperationPort creditOperationPort;
    private final ClientPort clientPort;
    private final ManagerPort managerPort;
    private final AgencyPort agencyPort;
    private final ClassifierPort classifierPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final MonthlyDisbursementPort monthlyDisbursementPort;

    private long rollbackMortgageReliefItem = 1;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class,
            CreditOperationNotFoundException.class
    })
    @Override
    public PersistenceResponse saveSubscriptionsTrackingForRegulatedPolicy(
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long policyTypeId,
            long usersId,
            List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
        List<String> regionalErrors = new ArrayList<>();
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

            // delete observed cases related
            Classifier reportTypeObservedCaseDHL = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHL.getId(), policyTypeId, usersId, insurancePolicyHolderId);

            // update status for each monthly disbursement
            monthlyDisbursementPort.updateAllCaseStatus(
                    monthId, yearId,
                    insurancePolicyHolderId, policyTypeId,
                    MonthlyDisbursementCaseStatusEnum.PendingValidation
                            .getIdentifier());

        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        // Cast object for persistence db
        subscriptionTrackingDhlDTOS.stream()
                .forEach(subscriptionTracking -> {

                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;

                    long agencyId = 0l, coverageIdc = 0l, regionalIdc = 0l,
                            insuranceRequestId = 0l, managerId = 0l, clientId = 0l, creditOperationId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {

                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(subscriptionTracking.getMONEDA(), ClassifierTypeEnum.Currency);

                        // ACTIVO/PENDIENTE/....
                        Classifier insuranceRequestStatusCls = classifierPort.getClassifierByName(subscriptionTracking.getESTADO(),
                                ClassifierTypeEnum.DHInsuranceRequestStatus);

                        // Coverage
                        coverageIdc = classifierPort.getClassifierByReferences(ClassifierEnum.Normal_Coverage)
                                .getId();

                        // Regional
                        regionalIdc = classifierPort.getClassifierByName(subscriptionTracking.getOFICINA(), ClassifierTypeEnum.Regional)
                                .getId();


                        // Client
                        Client client = Client.builder()
                                .documentNumber(subscriptionTracking.getCEDULA_IDENTIDAD())
                                .birthDate(subscriptionTracking.getFECHA_NACIMIENTO())
                                .gender(subscriptionTracking.getGENERO())
                                .extension(subscriptionTracking.getEX())
                                .accumulatedAmountDhl(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                // object
                                .currency(currencyCls)
                                .documentType(documentTypeCiCls)
                                /* .documentTypeId(documentTypeCiIdc)
                                 .currencyId(currencyIdc)*/
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Insurance Request
                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(subscriptionTracking.getNRO_DECLARACION())
                                .djsReceptionDate(subscriptionTracking.getFECHA_RECEPCION_DJS_REQUERIMIENTO())
                                .requestedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                .fulfillmentRequirementsDate(subscriptionTracking.getFECHA_CUMPLIMIENTO_REQUISITOS())
                                .djsFillDate(subscriptionTracking.getFECHA_LLENADO_DJS())
                                .requestedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                .bankPronouncementDate(subscriptionTracking.getFECHA_PRONUNCIAMIENTO_AL_BANCO())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                .schedulingDate(subscriptionTracking.getFECHA_AGENDAMIENTO())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                // object
                                .currency(currencyCls)
                                .requestStatus(insuranceRequestStatusCls)
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();


                        /*// Credit Operation
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(subscriptionTracking.getNRO_OPERACION())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                .disbursedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .insuredAmount(subscriptionTracking.getCAPITAL_ASEGURADO_BS())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                //.insuranceRequestId(insuranceRequestId)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/

                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(subscriptionTracking.getNRO_OPERACION())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();


                        /*// validate persistence
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;*/

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;


                        // Manager
                        Manager manager = Manager.builder()
                                .NAMES(subscriptionTracking.getGESTOR())
                                // default value
                                .STATUS(1)
                                .build();
                        managerMap = managerPort.findOrUpsert(manager);
                        manager = ((Manager) managerMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        managerId = manager.getMANAGER_ID()
                                .longValue();

                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(subscriptionTracking.getSUCURSAL_OFICINA())
                                // default values
                                .BRANCH_OFFICE_ID(BigInteger.valueOf(101l))
                                .STATUS(1)
                                .ZONES_ID(393l)
                                .build();
                        agencyMap = agencyPort.findOrUpsert(agency);
                        agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getAGENCY_ID()
                                .longValue();

                        // assign relationship
                        subscriptionTracking.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        subscriptionTracking.setID_CLIENTE(clientId);
                        subscriptionTracking.setID_REGIONAL(regionalIdc);
                        subscriptionTracking.setID_GESTOR(managerId);
                        subscriptionTracking.setID_TIPO_COBERTURA(coverageIdc);
                        subscriptionTracking.setID_AGENCIA(agencyId);
                        subscriptionTracking.setID_SOLICITUD_SEGURO(insuranceRequestId);
                        subscriptionTracking.setID_MONEDA(currencyCls.getId());

                        //subscriptionTracking.setITEM_DESGRAVAMEN(mortgageReliefItem);


                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | CreditOperationNotFoundException e) {
                        creditOperationStatusErrors.add(subscriptionTracking.getNRO_OPERACION()
                                .toString());

                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    } catch (ClassifierNotFoundException classifierNotFoundException) {
                        String classifierNameException = classifierNotFoundException.getClassifierName();
                        if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                            currencyErrors.add(subscriptionTracking.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.Regional.getName())) {
                            regionalErrors.add(subscriptionTracking.getOFICINA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add(subscriptionTracking.getCOBERTURA_OTORGADA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.DHInsuranceRequestStatus.getName())) {
                            insuranceRequestStatusErrors.add(subscriptionTracking.getESTADO());
                        }
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });

        if (currencyErrors.isEmpty() && regionalErrors.isEmpty()
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()
                && creditOperationStatusErrors.isEmpty()) {
            return subscriptionTrackingPort.registerSubscriptionsTrackingForRegulatedPolicy(subscriptionTrackingDhlDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), currencyErrors);
            errors.put(ClassifierTypeEnum.Regional
                    .getName(), regionalErrors);
            errors.put(ClassifierTypeEnum.InsuranceCoverage
                    .getName(), coverageErrors);
            errors.put(ClassifierTypeEnum.DHInsuranceRequestStatus
                    .getName(), insuranceRequestStatusErrors);
            errors.put(HelpersConstants.CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS,
                    creditOperationStatusErrors);
            return persistenceResponse.setData(errors);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class,
            CreditOperationNotFoundException.class
    })
    @Override
    public PersistenceResponse saveSubscriptionsTrackingForNotRegulatedPolicy(
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long policyTypeId,
            long usersId,
            List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
        List<String> regionalErrors = new ArrayList<>();
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

            // delete observed cases related
            Classifier reportTypeObservedCaseDHL = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHL.getId(), policyTypeId, usersId, insurancePolicyHolderId);

            // update status for each monthly disbursement
            monthlyDisbursementPort.updateAllCaseStatus(
                    monthId, yearId,
                    insurancePolicyHolderId, policyTypeId,
                    MonthlyDisbursementCaseStatusEnum.PendingValidation
                            .getIdentifier());

        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();


        subscriptionTrackingDhnDTOS.stream()
                .forEach(subscriptionTracking -> {

                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;

                    long agencyId = 0l, coverageIdc = 0l, regionalIdc = 0l,
                            insuranceRequestId = 0l, managerId = 0l, clientId = 0l, creditOperationId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {
                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(subscriptionTracking.getMONEDA(), ClassifierTypeEnum.Currency);

                        // ACTIVO/PENDIENTE/....
                        Classifier insuranceRequestStatusCls = classifierPort.getClassifierByName(subscriptionTracking.getESTADO(),
                                ClassifierTypeEnum.DHInsuranceRequestStatus);

                        // Coverage
                        coverageIdc = classifierPort.getClassifierByName(subscriptionTracking.getCOBERTURA_OTORGADA(), ClassifierTypeEnum.InsuranceCoverage)
                                .getId();

                        // Regional
                        regionalIdc = classifierPort.getClassifierByName(subscriptionTracking.getOFICINA(), ClassifierTypeEnum.Regional)
                                .getId();

                        // Client
                        Client client = Client.builder()
                                .documentNumber(subscriptionTracking.getCI())
                                .birthDate(subscriptionTracking.getFECHA_NACIMIENTO())
                                .gender(subscriptionTracking.getGENERO())
                                .extension(subscriptionTracking.getEXTENSION())
                                .accumulatedAmountDhl(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                // object
                                .currency(currencyCls)
                                .documentType(documentTypeCiCls)
                                /* .documentTypeId(documentTypeCiIdc)
                                 .currencyId(currencyIdc)*/
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Insurance Request
                        InsuranceRequest insuranceRequest = InsuranceRequest.builder()
                                .requestNumber(subscriptionTracking.getNRO_DECLARACION())
                                .djsReceptionDate(subscriptionTracking.getFECHA_RECEPCION_DJS())
                                .requestedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                .fulfillmentRequirementsDate(subscriptionTracking.getFECHA_CUMPLIMIENTO_REQUISITOS())
                                .djsFillDate(subscriptionTracking.getFECHA_LLENADO_DJS())
                                .requestedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(subscriptionTracking.getMONTO_ACUMULADO_BS())
                                .bankPronouncementDate(subscriptionTracking.getFECHA_PRONUNCIAMIENTO_AL_BANCO())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                .schedulingDate(subscriptionTracking.getFECHA_AGENDAMIENTO())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                // object
                                .currency(currencyCls)
                                .requestStatus(insuranceRequestStatusCls)
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();


                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(subscriptionTracking.getNRO_OPERACION())
                                .disbursementDate(subscriptionTracking.getFECHA_DESEMBOLSO())
                                .disbursedAmount(subscriptionTracking.getMONTO_SOLICITADO_BS())
                                .insuredAmount(subscriptionTracking.getCAPITAL_ASEGURADO_BS())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                //.insuranceRequestId(insuranceRequestId)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(subscriptionTracking.getNRO_OPERACION())
                                .currency(currencyCls)
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


                        // Manager
                        Manager manager = Manager.builder()
                                .NAMES(subscriptionTracking.getGESTOR())
                                // default value
                                .STATUS(1)
                                .build();
                        managerMap = managerPort.findOrUpsert(manager);
                        manager = ((Manager) managerMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        managerId = manager.getMANAGER_ID()
                                .longValue();

                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(subscriptionTracking.getSUCURSAL_OFICINA())
                                // default values
                                .BRANCH_OFFICE_ID(BigInteger.valueOf(101l))
                                .STATUS(1)
                                .ZONES_ID(393l)
                                .build();
                        agencyMap = agencyPort.findOrUpsert(agency);
                        agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getAGENCY_ID()
                                .longValue();

                        // assign relationship
                        subscriptionTracking.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        subscriptionTracking.setID_CLIENTE(clientId);
                        subscriptionTracking.setID_REGIONAL(regionalIdc);
                        subscriptionTracking.setID_GESTOR(managerId);
                        subscriptionTracking.setID_TIPO_COBERTURA(coverageIdc);
                        subscriptionTracking.setID_AGENCIA(agencyId);
                        subscriptionTracking.setID_SOLICITUD_SEGURO(insuranceRequestId);
                        subscriptionTracking.setID_MONEDA(currencyCls.getId());

                        //subscriptionTracking.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | CreditOperationNotFoundException e) {
                        creditOperationStatusErrors.add(subscriptionTracking.getNRO_OPERACION()
                                .toString());

                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    } catch (ClassifierNotFoundException classifierNotFoundException) {
                        String classifierNameException = classifierNotFoundException.getClassifierName();
                        if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                            currencyErrors.add(subscriptionTracking.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.Regional.getName())) {
                            regionalErrors.add(subscriptionTracking.getOFICINA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add(subscriptionTracking.getCOBERTURA_OTORGADA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.DHInsuranceRequestStatus.getName())) {
                            insuranceRequestStatusErrors.add(subscriptionTracking.getESTADO());
                        }
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });

        if (currencyErrors.isEmpty() && regionalErrors.isEmpty()
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()
                && creditOperationStatusErrors.isEmpty()) {
            return subscriptionTrackingPort.registerSubscriptionsTrackingForNotRegulatedPolicy(subscriptionTrackingDhnDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), currencyErrors);
            errors.put(ClassifierTypeEnum.Regional
                    .getName(), regionalErrors);
            errors.put(ClassifierTypeEnum.InsuranceCoverage
                    .getName(), coverageErrors);
            errors.put(ClassifierTypeEnum.DHInsuranceRequestStatus
                    .getName(), insuranceRequestStatusErrors);
            errors.put(HelpersConstants.CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS,
                    creditOperationStatusErrors);
            return persistenceResponse.setData(errors);
        }

    }

    @Override
    public List<SubscriptionTrackingDhlDTO> getSubscriptionTrackingDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return subscriptionTrackingPort.getSubscriptionTrackingDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<SubscriptionTrackingDhnDTO> getSubscriptionTrackingDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return subscriptionTrackingPort.getSubscriptionTrackingDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
