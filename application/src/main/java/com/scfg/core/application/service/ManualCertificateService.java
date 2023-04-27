package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ManualCertificateUseCase;
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
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhnDTO;
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
public class ManualCertificateService implements ManualCertificateUseCase {


    private final ManualCertificatePort manualCertificatePort;
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
    public PersistenceResponse registerManualCertificatesForRegulatedPolicy(
            long policyTypeId, long monthId,
            long yearId, long insurancePolicyHolderId,
            long reportTypeId, long usersId,
            List<ManualCertificateDhlDTO> manualCertificatesDhl, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> creditTypeErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
        List<String> creditOperationStatusErrors = new ArrayList<>();

        // Save mortgage relief item
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
            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeId, policyTypeId,
                    usersId, insurancePolicyHolderId);

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

        manualCertificatesDhl.stream()
                .forEach(manualCertificate -> {

                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;
                    long agencyId = 0l, coverageIdc = 0l, creditTypeIdc = 0l,
                            insuranceRequestId = 0l, managenId = 0l, clientId = 0l, creditOperationId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;
                    // get elements fro rows for relationships
                    try {

                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(manualCertificate.getMONEDA(), ClassifierTypeEnum.Currency);

                        // ACTIVO/PENDIENTE/....
                        Classifier insuranceRequestStatusCls = classifierPort.getClassifierByName(manualCertificate.getESTADO_SOLICITUD(),
                                ClassifierTypeEnum.DHInsuranceRequestStatus);

                        // Client
                        Client client = Client.builder()
                                .documentNumber(manualCertificate.getCI())
                                .birthDate(manualCertificate.getFECHA_NACIMIENTO())
                                .gender(manualCertificate.getGENERO())
                                .nationality(manualCertificate.getNACIONALIDAD())
                                .accumulatedAmountDhl(manualCertificate.getMONTO_ACUMULADO_BS())
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
                                .requestNumber(manualCertificate.getNRO_DJS_MANUAL())
                                .djsFillDate(manualCertificate.getFECHA_LLENADO_DJS())
                                .requestedAmount(manualCertificate.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(manualCertificate.getMONTO_ACUMULADO_BS())
                                .acceptanceDate(manualCertificate.getFECHA_ACEPTACION())
                                .disbursementDate(manualCertificate.getFECHA_DESEMBOLSO())
                                // object
                                .currency(currencyCls)
                                .requestStatus(insuranceRequestStatusCls)
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();


                        // Credit Operation
                        /*CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(manualCertificate.getNRO_OPERACION())
                                .disbursementDate(manualCertificate.getFECHA_DESEMBOLSO())
                                .insuredAmount(manualCertificate.getVALOR_ASEGURADO())
                                .premiumRate(manualCertificate.getTASAX())
                                .premiumValue(manualCertificate.getPRIMA_BS())
                                .extraPremiumRate(manualCertificate.getTASA_EXTRAPRIMA())
                                .extraPremiumValue(manualCertificate.getEXTRAPRIMA_BS())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(manualCertificate.getNRO_OPERACION())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                .build();

                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Coverage
                        coverageIdc = classifierPort.getClassifierByName(manualCertificate.getCOBERTURA(), ClassifierTypeEnum.Currency)
                                .getId();

                        // Credit Type
                        creditTypeIdc = classifierPort.getClassifierByName(manualCertificate.getTIPO_CREDITO(), ClassifierTypeEnum.Currency)
                                .getId();

                        // Manager
                        Manager manager = Manager.builder()
                                .NAMES(manualCertificate.getGESTOR())
                                // default value
                                .STATUS(1)
                                .build();
                        managerMap = managerPort.findOrUpsert(manager);
                        manager = ((Manager) managerMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        managenId = manager.getMANAGER_ID()
                                .longValue();


                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(manualCertificate.getAGENCIA())
                                // default values
                                .BRANCH_OFFICE_ID(BigInteger.valueOf(101l))
                                .STATUS(1)
                                .ZONES_ID(393l)
                                .build();
                        agencyMap = agencyPort.findOrUpsert(agency);
                        agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getAGENCY_ID()
                                .longValue();

                        // validate persistence
                        /*rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;*/

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // assign relationship
                        manualCertificate.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        manualCertificate.setID_CLIENTE(clientId);
                        manualCertificate.setID_COBERTURA(coverageIdc);
                        manualCertificate.setID_SOLICITUD_SEGURO(insuranceRequestId);
                        manualCertificate.setID_COBERTURA(coverageIdc);
                        manualCertificate.setID_TIPO_CREDITO(creditTypeIdc);
                        manualCertificate.setID_GESTOR(managenId);
                        manualCertificate.setID_AGENCIA(agencyId);
                        manualCertificate.setID_MONEDA(currencyCls.getId());

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException
                            | CreditOperationNotFoundException e) {

                        creditOperationStatusErrors.add(manualCertificate.getNRO_OPERACION()
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
                            currencyErrors.add(manualCertificate.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                            creditTypeErrors.add(manualCertificate.getTIPO_CREDITO());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add(manualCertificate.getCOBERTURA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.DHInsuranceRequestStatus.getName())) {
                            insuranceRequestStatusErrors.add(manualCertificate.getESTADO_SOLICITUD());
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

        if (currencyErrors.isEmpty() && creditTypeErrors.isEmpty()
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()
                && creditOperationStatusErrors.isEmpty()) {
            return manualCertificatePort.registerManualCertificatesForRegulatedPolicy(manualCertificatesDhl, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), currencyErrors);
            errors.put(ClassifierTypeEnum.CreditTYpe
                    .getName(), creditTypeErrors);
            errors.put(ClassifierTypeEnum.InsuranceCoverage
                    .getName(), coverageErrors);
            errors.put(ClassifierTypeEnum.DHInsuranceRequestStatus
                    .getName(), insuranceRequestStatusErrors);
            errors.put(HelpersConstants.CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS,
                    creditOperationStatusErrors);

            return persistenceResponse.setData(errors);
        }
    }

    // Modificar el metodo
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class,
            CreditOperationNotFoundException.class
    })
    @Override
    public PersistenceResponse registerManualCertificatesForNotRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<ManualCertificateDhnDTO> manualCertificatesDhn, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> creditTypeErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();
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

        manualCertificatesDhn.stream()
                .forEach(manualCertificate -> {
                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;

                    long agencyId = 0l, coverageIdc = 0l, creditTypeIdc = 0l,
                            insuranceRequestId = 0l, managenId = 0l, clientId = 0l, creditOperationId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    // get elements fro rows for relationships
                    try {
                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(manualCertificate.getMONEDA(), ClassifierTypeEnum.Currency);
                        // ACTIVO/PENDIENTE/....
                        Classifier insuranceRequestStatusCls = classifierPort.getClassifierByName(manualCertificate.getESTADO_SOLICITUD(),
                                ClassifierTypeEnum.DHInsuranceRequestStatus);

                        // Client
                        Client client = Client.builder()
                                .documentNumber(manualCertificate.getCI())
                                .birthDate(manualCertificate.getFECHA_NACIMIENTO())
                                .gender(manualCertificate.getGENERO())
                                .nationality(manualCertificate.getNACIONALIDAD())
                                .accumulatedAmountDhl(manualCertificate.getMONTO_ACUMULADO_BS())
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
                                .requestNumber(manualCertificate.getNRO_DJS_MANUAL())
                                .djsFillDate(manualCertificate.getFECHA_LLENADO_DJS())
                                .requestedAmount(manualCertificate.getMONTO_SOLICITADO_BS())
                                .accumulatedAmount(manualCertificate.getMONTO_ACUMULADO_BS())
                                .acceptanceDate(manualCertificate.getFECHA_ACEPTACION())
                                .disbursementDate(manualCertificate.getFECHA_DESEMBOLSO())
                                // object
                                .currency(currencyCls)
                                .requestStatus(insuranceRequestStatusCls)
                                /*.currency(Classifier.builder()
                                        .id(currencyCls)
                                        .build())
                                .requestStatus(Classifier.builder()
                                        .id(insuranceRequestStatusIdc)
                                        .build())*/
                                .build();
                        insuranceRequestMap = insuranceRequestPort.findOrUpsert(insuranceRequest);
                        insuranceRequest = ((InsuranceRequest) insuranceRequestMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        insuranceRequestId = insuranceRequest.getId();


                        /*// Credit Operation
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(manualCertificate.getNRO_OPERACION())
                                .disbursementDate(manualCertificate.getFECHA_DESEMBOLSO())
                                .insuredAmount(manualCertificate.getVALOR_ASEGURADO())
                                .premiumRate(manualCertificate.getTASAX())
                                .premiumValue(manualCertificate.getPRIMA_BS())
                                .extraPremiumRate(manualCertificate.getTASA_EXTRAPRIMA())
                                .extraPremiumValue(manualCertificate.getEXTRAPRIMA_BS())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                //.insuranceRequestId(insuranceRequestId)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();*/
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(manualCertificate.getNRO_OPERACION())
                                .currency(currencyCls)
                                .insuranceRequest(insuranceRequest)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation);
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();


                        // Coverage
                        coverageIdc = classifierPort.getClassifierByName(manualCertificate.getCOBERTURA(), ClassifierTypeEnum.InsuranceCoverage)
                                .getId();

                        // Credit Type
                        creditTypeIdc = classifierPort.getClassifierByName(manualCertificate.getTIPO_CREDITO(), ClassifierTypeEnum.CreditTYpe)
                                .getId();

                        // Manager
                        Manager manager = Manager.builder()
                                .NAMES(manualCertificate.getGESTOR())
                                // default value
                                .STATUS(1)
                                .build();
                        managerMap = managerPort.findOrUpsert(manager);
                        manager = ((Manager) managerMap.getOrDefault(KEY_CONTENT_ENTITY, null));
                        managenId = manager.getMANAGER_ID()
                                .longValue();

                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(manualCertificate.getAGENCIA())
                                // default values
                                .BRANCH_OFFICE_ID(BigInteger.valueOf(101l))
                                .STATUS(1)
                                .ZONES_ID(393l)
                                .build();
                        agencyMap = agencyPort.findOrUpsert(agency);
                        agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getAGENCY_ID()
                                .longValue();

                        /*// validate persistence
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;*/

                        rollbackInsuranceRequest = insuranceRequestMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;


                        // assign relationship
                        manualCertificate.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        manualCertificate.setID_CLIENTE(clientId);
                        manualCertificate.setID_COBERTURA(coverageIdc);
                        manualCertificate.setID_SOLICITUD_SEGURO(insuranceRequestId);
                        manualCertificate.setID_COBERTURA(coverageIdc);
                        manualCertificate.setID_TIPO_CREDITO(creditTypeIdc);
                        manualCertificate.setID_GESTOR(managenId);
                        manualCertificate.setID_AGENCIA(agencyId);
                        manualCertificate.setID_MONEDA(currencyCls.getId());

                        //manualCertificate.setITEM_DESGRAVAMEN(mortgageReliefItem);
                        // Currency
                        /*long currencyId = classifierPort.getClassifierByNameAndClassifier(
                                manualCertificate.getMONEDA(),
                                ClassifierEnum.CurrencyBs.getReferenceCode(),
                                ClassifierEnum.CurrencyBs.getReferenceCodeType())
                                .getId();*/
                        //manualCertificate.setID_CLIENTE(currencyId);

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException
                            | CreditOperationNotFoundException e) {

                        creditOperationStatusErrors.add(manualCertificate.getNRO_OPERACION()
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
                            currencyErrors.add(manualCertificate.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                            creditTypeErrors.add(manualCertificate.getTIPO_CREDITO());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add(manualCertificate.getCOBERTURA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.DHInsuranceRequestStatus.getName())) {
                            insuranceRequestStatusErrors.add(manualCertificate.getESTADO_SOLICITUD());
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

        if (currencyErrors.isEmpty() && creditTypeErrors.isEmpty()
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()
                && creditOperationStatusErrors.isEmpty()) {
            return manualCertificatePort.registerManualCertificatesForNotRegulatedPolicy(manualCertificatesDhn, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), currencyErrors);
            errors.put(ClassifierTypeEnum.CreditTYpe
                    .getName(), creditTypeErrors);
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
    public List<ManualCertificateDhlDTO> getManualCertificateDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return manualCertificatePort.getManualCertificateDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<ManualCertificateDhnDTO> getManualCertificateDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return manualCertificatePort.getManualCertificateDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }

}
