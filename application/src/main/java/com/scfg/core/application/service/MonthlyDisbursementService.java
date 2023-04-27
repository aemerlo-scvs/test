package com.scfg.core.application.service;

import com.scfg.core.application.port.in.MonthlyDisbursementUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Client;
import com.scfg.core.domain.common.CreditOperation;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import com.scfg.core.domain.managers.Agency;
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
public class MonthlyDisbursementService implements MonthlyDisbursementUseCase {

    private final PastMonthlyDisbursementPort pastMonthlyDisbursementPort;
    private final MonthlyDisbursementPort monthlyDisbursementPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final CreditOperationPort creditOperationPort;
    private final ClientPort clientPort;
    private final AgencyPort agencyPort;
    private final ClassifierPort classifierPort;

    private long rollbackMortgageReliefItem = 1;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveMonthlyDisbursementsForRegulatedPolicy(
            long monthId, long yearId,
            long insurancePolicyHolderId, long reportTypeId,
            long policyTypeId, long usersId,
            List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> creditTypeErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();

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
            // disable or delete last registers from monthly disbursement
            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);

            // delete observed cases related
            Classifier reportTypeObservedCaseDHL = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHL.getId(), policyTypeId, usersId, insurancePolicyHolderId);
        } else {
            // clean table Monthly Disbursement, too filter by params
            mortgageReliefItemPort.deleteAllByPolicyTypeAndReportTypeAndInsuranceHolder(policyTypeId, reportTypeId, insurancePolicyHolderId);
        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        monthlyDisbursementDhlDTOS.stream()
                .forEach(monthlyDisbursement -> {
                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;

                    long agencyId = 0l, coverageIdc = 0l, creditTypeIdc = 0l,
                            insuranceRequestId = 0l, creditOperationId = 0l, clientId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;
                    try {

                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(monthlyDisbursement.getMONEDA(), ClassifierTypeEnum.Currency);

                        // Coverage (default Normal Coverage)
                        coverageIdc = classifierPort.getClassifierByReferences(ClassifierEnum.Normal_Coverage)
                                .getId();

                        // Credit Type
                        creditTypeIdc = classifierPort.getClassifierByName(monthlyDisbursement.getTIPO_CREDITO(), ClassifierTypeEnum.CreditTYpe)
                                .getId();


                        // Client
                        Client client = Client.builder()
                                .documentNumber(monthlyDisbursement.getNRO_DOCUMENTO())
                                .names(monthlyDisbursement.getNOMBRES())
                                .lastName(monthlyDisbursement.getAPELLIDO_PATERNO())
                                .mothersLastName(monthlyDisbursement.getAPELLIDO_MATERNO())
                                .marriedLastName(monthlyDisbursement.getAPELLIDO_CASADA())
                                .duplicateCopy(monthlyDisbursement.getCOPIA_DUPLICADO())
                                .extension(monthlyDisbursement.getEXTENSION())
                                .birthDate(monthlyDisbursement.getFECHA_NACIMIENTO())
                                .gender(monthlyDisbursement.getSEXO())
                                .nationality(monthlyDisbursement.getNACIONALIDAD())
                                // object
                                .currency(currencyCls)
                                .documentType(documentTypeCiCls)
                                /* .documentTypeId(documentTypeCiIdc)
                                 .currencyId(currencyIdc)*/
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Credit Operation
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(monthlyDisbursement.getNRO_OPERACION())
                                .disbursementDate(monthlyDisbursement.getFECHA_DESEMBOLSO())
                                .insuredAmount(monthlyDisbursement.getVALOR_ASEGURADO())
                                .disbursedAmount(monthlyDisbursement.getMONTO_DESEMBOLSADO())
                                .creditLine(monthlyDisbursement.getLINEA_CREDITO()
                                        .equals("SI")
                                        ? 1 : 0)
                                .premiumRate(monthlyDisbursement.getTASAX())
                                .premiumValue(monthlyDisbursement.getMONTO_PRIMA())
                                .extraPremiumValue(monthlyDisbursement.getEXTRAPRIMA())
                                .currency(currencyCls)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(monthlyDisbursement.getAGENCIA())
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
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // assign relationship
                        monthlyDisbursement.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        monthlyDisbursement.setID_CLIENTE(clientId);
                        monthlyDisbursement.setID_OPERACION_CREDITICIA(creditOperationId);
                        monthlyDisbursement.setID_AGENCIA(agencyId);
                        monthlyDisbursement.setID_TIPO_CREDITO(creditTypeIdc);
                        monthlyDisbursement.setID_TIPO_COBERTURA(coverageIdc);
                        monthlyDisbursement.setID_MONEDA(currencyCls.getId());

                        //monthlyDisbursement.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException e) {
                        // Registrar nuevo clasificador
                        //classifierPort.save(null);
                    } catch (ClassifierNotFoundException classifierNotFoundException) {
                        String classifierNameException = classifierNotFoundException.getClassifierName();
                        if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                            currencyErrors.add(monthlyDisbursement.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                            creditTypeErrors.add(monthlyDisbursement.getTIPO_CREDITO());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add("Cobertura Normal");
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
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
            return monthlyDisbursementPort.registerMonthlyDisbursementsForRegulatedPolicy(monthlyDisbursementDhlDTOS, overwrite);
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
            return persistenceResponse.setData(errors);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveMonthlyDisbursementsForNotRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnDTOS, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> creditTypeErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();


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
            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);

            // delete observed cases related
            Classifier reportTypeObservedCaseDHN = classifierPort.getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType);

            mortgageReliefItemPort.disableLastInformation(
                    monthId, yearId, reportTypeObservedCaseDHN.getId(), policyTypeId, usersId, insurancePolicyHolderId);
        } else {
            // clean table, too filter by params
            mortgageReliefItemPort.deleteAllByPolicyTypeAndReportTypeAndInsuranceHolder(policyTypeId, reportTypeId, insurancePolicyHolderId);
        }

        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();


        monthlyDisbursementDhnDTOS.stream()
                .forEach(monthlyDisbursement -> {

                    Map<String, Object> clientMap, insuranceRequestMap,
                            creditOperationMap, agencyMap, managerMap;

                    long agencyId = 0l, coverageIdc = 0l, creditTypeIdc = 0l,
                            insuranceRequestId = 0l, creditOperationId = 0l, clientId = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {
                        // Document Type
                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                        // Currency
                        // Bolivianos/Dolares
                        Classifier currencyCls = classifierPort.getClassifierByName(monthlyDisbursement.getMONEDA(), ClassifierTypeEnum.Currency);

                        // Coverage (default Normal Coverage)
                        coverageIdc = classifierPort.getClassifierByName(monthlyDisbursement.getTIPO_COBERTURA(), ClassifierTypeEnum.InsuranceCoverage)
                                .getId();

                        // Credit Type
                        creditTypeIdc = classifierPort.getClassifierByName(monthlyDisbursement.getTIPO_CREDITO(), ClassifierTypeEnum.CreditTYpe)
                                .getId();

                        // Client
                        Client client = Client.builder()
                                .documentNumber(monthlyDisbursement.getNRO_DOCUMENTO())
                                .names(monthlyDisbursement.getNOMBRES())
                                .lastName(monthlyDisbursement.getAPELLIDO_PATERNO())
                                .mothersLastName(monthlyDisbursement.getAPELLIDO_MATERNO())
                                .marriedLastName(monthlyDisbursement.getAPELLIDO_CASADA())
                                .duplicateCopy(monthlyDisbursement.getCOPIA_DUPLICADO())
                                .extension(monthlyDisbursement.getEXTENSION())
                                .birthDate(monthlyDisbursement.getFECHA_NACIMIENTO())
                                .gender(monthlyDisbursement.getSEXO())
                                .nationality(monthlyDisbursement.getNACIONALIDAD())
                                // object
                                .currency(currencyCls)
                                .documentType(documentTypeCiCls)
                                /* .documentTypeId(documentTypeCiIdc)
                                 .currencyId(currencyIdc)*/
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Credit Operation
                        CreditOperation creditOperation = CreditOperation.builder()
                                .operationNumber(monthlyDisbursement.getNRO_OPERACION())
                                .disbursementDate(monthlyDisbursement.getFECHA_DESEMBOLSO())
                                .insuredAmount(monthlyDisbursement.getVALOR_ASEGURADO())
                                .disbursedAmount(monthlyDisbursement.getMONTO_DESEMBOLSADO())
                                .creditLine(monthlyDisbursement.getLINEA_CREDITO()
                                        .equals("SI")
                                        ? 1 : 0)
                                .premiumRate(monthlyDisbursement.getTASA_EXTRAPRIMA())
                                .premiumValue(monthlyDisbursement.getPRIMA_BS())
                                .extraPremiumValue(monthlyDisbursement.getEXTRAPRIMA_BS())
                                .currency(currencyCls)
                                .build();
                        creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                        creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // Agency
                        Agency agency = Agency.builder()
                                .DESCRIPTION(monthlyDisbursement.getAGENCIA())
                                // default values
                                .BRANCH_OFFICE_ID(BigInteger.valueOf(101))
                                .STATUS(1)
                                .ZONES_ID(393l)
                                .build();
                        agencyMap = agencyPort.findOrUpsert(agency);
                        agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getAGENCY_ID()
                                .longValue();

                        // validate persistence
                        rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // assign relationship
                        monthlyDisbursement.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        monthlyDisbursement.setID_CLIENTE(clientId);
                        monthlyDisbursement.setID_OPERACION_CREDITICIA(creditOperationId);
                        monthlyDisbursement.setID_AGENCIA(agencyId);
                        monthlyDisbursement.setID_TIPO_CREDITO(creditTypeIdc);
                        monthlyDisbursement.setID_TIPO_COBERTURA(coverageIdc);
                        monthlyDisbursement.setID_MONEDA(currencyCls.getId());

                        //monthlyDisbursement.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException e) {
                        // Registrar nuevo clasificador
                        //classifierPort.save(null);
                    } catch (ClassifierNotFoundException classifierNotFoundException) {
                        String classifierNameException = classifierNotFoundException.getClassifierName();
                        if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                            currencyErrors.add(monthlyDisbursement.getMONEDA());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                            creditTypeErrors.add(monthlyDisbursement.getTIPO_CREDITO());
                        }
                        if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                            coverageErrors.add(monthlyDisbursement.getTIPO_COBERTURA());
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
                && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
            return monthlyDisbursementPort.registerMonthlyDisbursementsForNotRegulatedPolicy(monthlyDisbursementDhnDTOS, overwrite);
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
            return persistenceResponse.setData(errors);
        }

    }

    @Override
    public List<MonthlyDisbursementDhlDTO> getMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS = monthlyDisbursementPort
                .getMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);
        if (monthlyDisbursementDhlDTOS.isEmpty()) {
            monthlyDisbursementDhlDTOS = pastMonthlyDisbursementPort.getPastMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);
        }
        return monthlyDisbursementDhlDTOS;
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnDTOS = monthlyDisbursementPort
                .getMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);
        if (monthlyDisbursementDhnDTOS.isEmpty()) {
            monthlyDisbursementDhnDTOS = pastMonthlyDisbursementPort.getPastMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);
        }
        return monthlyDisbursementDhnDTOS;
    }


}
