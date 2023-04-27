package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PastMonthlyDisbursementUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
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

import static com.scfg.core.common.util.HelpersMethods.*;

@UseCase
@RequiredArgsConstructor
public class PastMonthlyDisbursementService implements PastMonthlyDisbursementUseCase {

    private final PastMonthlyDisbursementPort pastMonthlyDisbursementPort;
    private final MonthlyDisbursementPort monthlyDisbursementPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final CreditOperationPort creditOperationPort;
    private final ClientPort clientPort;
    private final AgencyPort agencyPort;
    private final ClassifierPort classifierPort;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse migrateInformationToPastMonthlyDisbursementsForRegulatedPolicy(
            long monthId, long yearId,
            long insurancePolicyHolderId, long reportTypeId,
            long policyTypeId, long usersId,
            long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        //monthlyDisbursementPort.deleteForPeriod(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId);

       /* Map<String, List<String>> errors = new HashMap<>();
        List<String> coverageErrors = new ArrayList<>();
        List<String> creditTypeErrors = new ArrayList<>();
        List<String> currencyErrors = new ArrayList<>();
        List<String> insuranceRequestStatusErrors = new ArrayList<>();*/

        Classifier currentMonthCls = classifierPort.getClassifierById(monthId);

        Classifier currentYearCls = classifierPort.getClassifierById(yearId);

        Map<String, Integer> prevPeriod = getPeriod(currentMonthCls.getOrder(), currentYearCls.getOrder(), false);


        // Get Past Monthly disbursement
        List<MonthlyDisbursementDhlDTO> pastMonthlyDisbursementDhlDTOS = monthlyDisbursementPort
                .getPastMonthlyDisbursementDHLFilteredForPeriod(
                        prevPeriod.get(KEY_MONTH).longValue(),
                        prevPeriod.get(KEY_YEAR).longValue(),
                        insurancePolicyHolderId);

        if (!pastMonthlyDisbursementDhlDTOS.isEmpty()) {

            Classifier prevMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Month, prevPeriod.get(KEY_MONTH).intValue());

            Classifier prevYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Year, prevPeriod.get(KEY_YEAR).intValue());

            Classifier reportTypePastMonthlyDisbursementsDHL = classifierPort
                    .getClassifierByReferences(ClassifierEnum.PastMonthlyDisbursementsDHL_ReportType);

            long pastMonthlyId = prevMonthCls.getId(), pastYearId = prevYearCls.getId(),
                    reportTypeIdPastMonthlyDisbursement = reportTypePastMonthlyDisbursementsDHL.getId();

            Map<String, Integer> prevTwoPeriod = getPeriod(prevMonthCls.getOrder(), prevYearCls.getOrder(), false);

            Classifier prevTwoMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Month, prevTwoPeriod.get(KEY_MONTH).intValue());

            Classifier prevTwoYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Year, prevTwoPeriod.get(KEY_YEAR).intValue());

            // delete information for table Past Monthly Disbursement
            mortgageReliefItemPort.deleteForPeriod(
                    prevTwoMonthCls.getId(),
                    prevTwoYearCls.getId(),
                    insurancePolicyHolderId, reportTypeIdPastMonthlyDisbursement,
                    policyTypeId);

            // Save mortgage refief item for past monthly disbursement
            MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                    .policyTypeIdc(policyTypeId)
                    .monthIdc(pastMonthlyId)
                    .yearIdc(pastYearId)
                    .insurancePolicyHolderIdc(insurancePolicyHolderId)
                    .reportTypeIdc(reportTypePastMonthlyDisbursementsDHL.getId())
                    .usersId(usersId)
                    .build();

            long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                    .getId();


            pastMonthlyDisbursementDhlDTOS.stream()
                    .forEach(monthlyDisbursementDhlDTO ->
                            monthlyDisbursementDhlDTO.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId));

            persistenceResponse = pastMonthlyDisbursementPort.savePastMonthlyDisbursementsForRegulatedPolicy(pastMonthlyDisbursementDhlDTOS, overwrite);

            /*pastMonthlyDisbursementDhlDTOS.stream()
                    .forEach(pastMonthlyDisbursement -> {
                        Map<String, Object> clientMap, insuranceRequestMap,
                                creditOperationMap, agencyMap, managerMap;

                        long agencyId = 0l, coverageIdc = 0l, creditTypeIdc = 0l,
                                insuranceRequestId = 0l, creditOperationId = 0l, clientId = 0l;

                        long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;
                        try {

                            // Document Type
                            // Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                            //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);

                            // Currency
                            // Bolivianos/Dolares
                            //Classifier currencyCls = classifierPort.getClassifierByName(pastMonthlyDisbursement.getMONEDA(), ClassifierTypeEnum.Currency);

                            // Coverage (default Normal Coverage)
                            *//*coverageIdc = classifierPort.getClassifierByReferences(ClassifierEnum.Normal_Coverage)
                                    .getId();*//*

                            // Credit Type
                            *//*creditTypeIdc = classifierPort.getClassifierByName(pastMonthlyDisbursement.getTIPO_CREDITO(), ClassifierTypeEnum.CreditTYpe)
                                    .getId();*//*


                            // Client
                            //clientId  = pastMonthlyDisbursement.getID_CLIENTE();


                            *//*Client client = Client.builder()
                                    .documentNumber(pastMonthlyDisbursement.getNRO_DOCUMENTO())
                                    .names(pastMonthlyDisbursement.getNOMBRES())
                                    .lastName(pastMonthlyDisbursement.getAPELLIDO_PATERNO())
                                    .mothersLastName(pastMonthlyDisbursement.getAPELLIDO_MATERNO())
                                    .marriedLastName(pastMonthlyDisbursement.getAPELLIDO_CASADA())
                                    .duplicateCopy(pastMonthlyDisbursement.getCOPIA_DUPLICADO())
                                    .extension(pastMonthlyDisbursement.getEXTENSION())
                                    .birthDate(pastMonthlyDisbursement.getFECHA_NACIMIENTO())
                                    .gender(pastMonthlyDisbursement.getSEXO())
                                    .nationality(pastMonthlyDisbursement.getNACIONALIDAD())
                                    // object
                                    .currency(currencyCls)
                                    .documentType(documentTypeCiCls)
                                    *//**//* .documentTypeId(documentTypeCiIdc)
                                     .currencyId(currencyIdc)*//**//*
                                    .build();
                            clientMap = clientPort.findOrUpsert(client);
                            clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                    .getId();*//*

                            // Credit Operation
                            //creditOperationId = pastMonthlyDisbursement.getID_OPERACION_CREDITICIA();

                            *//*CreditOperation creditOperation = CreditOperation.builder()
                                    .operationNumber(pastMonthlyDisbursement.getNRO_OPERACION())
                                    .disbursementDate(pastMonthlyDisbursement.getFECHA_DESEMBOLSO())
                                    .insuredAmount(pastMonthlyDisbursement.getVALOR_ASEGURADO())
                                    .disbursedAmount(pastMonthlyDisbursement.getMONTO_DESEMBOLSADO())
                                    .creditLine(pastMonthlyDisbursement.getLINEA_CREDITO()
                                            .equals("SI")
                                            ? 1 : 0)
                                    .premiumRate(pastMonthlyDisbursement.getTASAX())
                                    .premiumValue(pastMonthlyDisbursement.getMONTO_PRIMA())
                                    .extraPremiumValue(pastMonthlyDisbursement.getEXTRAPRIMA())
                                    .currency(currencyCls)
                                    .build();
                            creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                            creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                    .getId();*//*

                            // Agency
                            //agencyId = pastMonthlyDisbursement.getID_AGENCIA();

                            *//*Agency agency = Agency.builder()
                                    .DESCRIPTION(pastMonthlyDisbursement.getAGENCIA())
                                    // default values
                                    .BRANCH_OFFICE_ID(BigInteger.valueOf(1l))
                                    .STATUS(1)
                                    .ZONES_ID(1l)
                                    .build();
                            agencyMap = agencyPort.findOrUpsert(agency);
                            agencyId = ((Agency) agencyMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                    .getAGENCY_ID()
                                    .longValue();*//*

                            // validate persistence
                            *//*rollbackCreditOperation = creditOperationMap.get(KEY_ACTION_ENTITY)
                                    .equals(CREATE_ACTION) ? 1 : 0;

                            rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                    .equals(CREATE_ACTION) ? 1 : 0;*//*

                            // assign relationship
                            pastMonthlyDisbursement.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                            *//*pastMonthlyDisbursement.setID_CLIENTE(clientId);
                            pastMonthlyDisbursement.setID_OPERACION_CREDITICIA(creditOperationId);
                            pastMonthlyDisbursement.setID_AGENCIA(agencyId);
                            pastMonthlyDisbursement.setID_TIPO_CREDITO(creditTypeIdc);
                            pastMonthlyDisbursement.setID_TIPO_COBERTURA(coverageIdc);
                            pastMonthlyDisbursement.setID_MONEDA(currencyCls.getId());*//*

                            //monthlyDisbursement.setITEM_DESGRAVAMEN(mortgageReliefItem);

                        } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException e) {
                            // Registrar nuevo clasificador
                            //classifierPort.save(null);
                        } catch (ClassifierNotFoundException classifierNotFoundException) {
                            String classifierNameException = classifierNotFoundException.getClassifierName();
                            if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                                currencyErrors.add(pastMonthlyDisbursement.getMONEDA());
                            }
                            if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                                creditTypeErrors.add(pastMonthlyDisbursement.getTIPO_CREDITO());
                            }
                            if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                                coverageErrors.add("Cobertura Normal");
                            }
                            // rollback related entities
                            mortgageReliefItemPort.callSpRollbackRelatedEntities(rollbackCreditOperation, rollbackInsuranceRequest, rollbackClient);
                        }
                    });*/
            /*if (currencyErrors.isEmpty() && creditTypeErrors.isEmpty()
                    && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
                return pastMonthlyDisbursementPort.savePastMonthlyDisbursementsForRegulatedPolicy(pastMonthlyDisbursementDhlDTOS, overwrite);
            } else { // send errors to API Controller
                // Observed case
                errors.put(ClassifierTypeEnum.Currency
                        .getName(), currencyErrors);
                errors.put(ClassifierTypeEnum.CreditTYpe
                        .getName(), creditTypeErrors);
                errors.put(ClassifierTypeEnum.InsuranceCoverage
                        .getName(), coverageErrors);
                errors.put(ClassifierTypeEnum.InsuranceRequestStatus
                        .getName(), insuranceRequestStatusErrors);
                return persistenceResponse.setData(errors);
            }*/
        }
        return persistenceResponse;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse migrateInformationToPastMonthlyDisbursementsForNotRegulatedPolicy(
            long monthId, long yearId,
            long insurancePolicyHolderId, long reportTypeId,
            long policyTypeId, long usersId,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        //mortgageReliefItemPort.deleteForPeriod(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId);

        Classifier currentMonthCls = classifierPort.getClassifierById(monthId);

        Classifier currentYearCls = classifierPort.getClassifierById(yearId);

        Map<String, Integer> prevPeriod = getPeriod(currentMonthCls.getOrder(), currentYearCls.getOrder(), false);

        // Get Past Monthly disbursement
        List<MonthlyDisbursementDhnDTO> pastMonthlyDisbursementDhnDTOS = monthlyDisbursementPort
                .getPastMonthlyDisbursementDHNFilteredForPeriod(
                        prevPeriod.get(KEY_MONTH).longValue(),
                        prevPeriod.get(KEY_YEAR).longValue(),
                        insurancePolicyHolderId);

        if (!pastMonthlyDisbursementDhnDTOS.isEmpty()) {

            Classifier prevMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Month, prevPeriod.get(KEY_MONTH).intValue());

            Classifier prevYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Year, prevPeriod.get(KEY_YEAR).intValue());

            Classifier reportTypePastMonthlyDisbursementsDHN = classifierPort.getClassifierByReferences(ClassifierEnum.PastMonthlyDisbursementsDHN_ReportType);

            long pastMonthlyId = prevMonthCls.getId(), pastYearId = prevYearCls.getId(),
                    reportTypeIdPastMonthlyDisbursement = reportTypePastMonthlyDisbursementsDHN.getId();

            // Get Last
            Map<String, Integer> prevTwoPeriod = getPeriod(prevMonthCls.getOrder(), prevYearCls.getOrder(), false);

            Classifier prevTwoMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Month, prevTwoPeriod.get(KEY_MONTH).intValue());

            Classifier prevTwoYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                    ClassifierTypeEnum.Year, prevTwoPeriod.get(KEY_YEAR).intValue());

            // delete information for table Past Monthly Disbursement
            mortgageReliefItemPort.deleteForPeriod(
                    prevTwoMonthCls.getId(),
                    prevTwoYearCls.getId(),
                    insurancePolicyHolderId, reportTypeIdPastMonthlyDisbursement,
                    policyTypeId);

            // Save mortgage relief item for past monthly disbursement
            MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                    .policyTypeIdc(policyTypeId)
                    .monthIdc(pastMonthlyId)
                    .yearIdc(pastYearId)
                    .insurancePolicyHolderIdc(insurancePolicyHolderId)
                    .reportTypeIdc(reportTypePastMonthlyDisbursementsDHN.getId())
                    .usersId(usersId)
                    .build();

            long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                    .getId();

            pastMonthlyDisbursementDhnDTOS.stream()
                    .forEach(monthlyDisbursementDhnDTO ->
                            monthlyDisbursementDhnDTO.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId));

            persistenceResponse = pastMonthlyDisbursementPort.savaPastMonthlyDisbursementsForNotRegulatedPolicy(
                    pastMonthlyDisbursementDhnDTOS, overwrite);

            /* pastMonthlyDisbursementDhnDTOS.stream()
                    .forEach(pastMonthlyDisbursement -> {

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
                            Classifier currencyCls = classifierPort.getClassifierByName(pastMonthlyDisbursement.getMONEDA(), ClassifierTypeEnum.Currency);

                            // Coverage (default Normal Coverage)
                            coverageIdc = classifierPort.getClassifierByName(pastMonthlyDisbursement.getTIPO_COBERTURA(), ClassifierTypeEnum.InsuranceCoverage)
                                    .getId();

                            // Credit Type
                            creditTypeIdc = classifierPort.getClassifierByName(pastMonthlyDisbursement.getTIPO_CREDITO(), ClassifierTypeEnum.CreditTYpe)
                                    .getId();

                            // Client
                            Client client = Client.builder()
                                    .documentNumber(pastMonthlyDisbursement.getNRO_DOCUMENTO())
                                    .names(pastMonthlyDisbursement.getNOMBRES())
                                    .lastName(pastMonthlyDisbursement.getAPELLIDO_PATERNO())
                                    .mothersLastName(pastMonthlyDisbursement.getAPELLIDO_MATERNO())
                                    .marriedLastName(pastMonthlyDisbursement.getAPELLIDO_CASADA())
                                    .duplicateCopy(pastMonthlyDisbursement.getCOPIA_DUPLICADO())
                                    .extension(pastMonthlyDisbursement.getEXTENSION())
                                    .birthDate(pastMonthlyDisbursement.getFECHA_NACIMIENTO())
                                    .gender(pastMonthlyDisbursement.getSEXO())
                                    .nationality(pastMonthlyDisbursement.getNACIONALIDAD())
                                    // object
                                    .currency(currencyCls)
                                    .documentType(documentTypeCiCls)
                                    *//* .documentTypeId(documentTypeCiIdc)
                                     .currencyId(currencyIdc)*//*
                                    .build();
                            clientMap = clientPort.findOrUpsert(client);
                            clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                    .getId();


                            // Credit Operation
                            CreditOperation creditOperation = CreditOperation.builder()
                                    .operationNumber(pastMonthlyDisbursement.getNRO_OPERACION())
                                    .disbursementDate(pastMonthlyDisbursement.getFECHA_DESEMBOLSO())
                                    .insuredAmount(pastMonthlyDisbursement.getVALOR_ASEGURADO())
                                    .disbursedAmount(pastMonthlyDisbursement.getMONTO_DESEMBOLSADO())
                                    .creditLine(pastMonthlyDisbursement.getLINEA_CREDITO()
                                            .equals("SI")
                                            ? 1 : 0)
                                    .premiumRate(pastMonthlyDisbursement.getTASA_EXTRAPRIMA())
                                    .premiumValue(pastMonthlyDisbursement.getPRIMA_BS())
                                    .extraPremiumValue(pastMonthlyDisbursement.getEXTRAPRIMA_BS())
                                    .currency(currencyCls)
                                    .build();
                            creditOperationMap = creditOperationPort.findOrUpsert(creditOperation); // not assign identifier value
                            creditOperationId = ((CreditOperation) creditOperationMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                    .getId();

                            // Agency
                            Agency agency = Agency.builder()
                                    .DESCRIPTION(pastMonthlyDisbursement.getAGENCIA())
                                    // default values
                                    .BRANCH_OFFICE_ID(BigInteger.valueOf(1l))
                                    .STATUS(1)
                                    .ZONES_ID(1l)
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
                            pastMonthlyDisbursement.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                            pastMonthlyDisbursement.setID_CLIENTE(clientId);
                            pastMonthlyDisbursement.setID_OPERACION_CREDITICIA(creditOperationId);
                            pastMonthlyDisbursement.setID_AGENCIA(agencyId);
                            pastMonthlyDisbursement.setID_TIPO_CREDITO(creditTypeIdc);
                            pastMonthlyDisbursement.setID_TIPO_COBERTURA(coverageIdc);
                            pastMonthlyDisbursement.setID_MONEDA(currencyCls.getId());

                            //monthlyDisbursement.setITEM_DESGRAVAMEN(mortgageReliefItem);

                        } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException e) {
                            // Registrar nuevo clasificador
                            //classifierPort.save(null);
                        } catch (ClassifierNotFoundException classifierNotFoundException) {
                            String classifierNameException = classifierNotFoundException.getClassifierName();
                            if (classifierNameException.equals(ClassifierTypeEnum.Currency.getName())) {
                                currencyErrors.add(pastMonthlyDisbursement.getMONEDA());
                            }
                            if (classifierNameException.equals(ClassifierTypeEnum.CreditTYpe.getName())) {
                                creditTypeErrors.add(pastMonthlyDisbursement.getTIPO_CREDITO());
                            }
                            if (classifierNameException.equals(ClassifierTypeEnum.InsuranceCoverage.getName())) {
                                coverageErrors.add(pastMonthlyDisbursement.getTIPO_COBERTURA());
                            }
                            // rollback related entities
                            mortgageReliefItemPort.callSpRollbackRelatedEntities(rollbackCreditOperation, rollbackInsuranceRequest, rollbackClient);
                        }
                    });
            if (currencyErrors.isEmpty() && creditTypeErrors.isEmpty()
                    && coverageErrors.isEmpty() && insuranceRequestStatusErrors.isEmpty()) {
                return pastMonthlyDisbursementPort.savaPastMonthlyDisbursementsForNotRegulatedPolicy(pastMonthlyDisbursementDhnDTOS, overwrite);
            } else { // send errors to API Controller
                // Observed case
                errors.put(ClassifierTypeEnum.Currency
                        .getName(), currencyErrors);
                errors.put(ClassifierTypeEnum.CreditTYpe
                        .getName(), creditTypeErrors);
                errors.put(ClassifierTypeEnum.InsuranceCoverage
                        .getName(), coverageErrors);
                errors.put(ClassifierTypeEnum.InsuranceRequestStatus
                        .getName(), insuranceRequestStatusErrors);
                return persistenceResponse.setData(errors);
            }*/
        }
        return persistenceResponse;
    }

    @Override
    public List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return monthlyDisbursementPort.getMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return monthlyDisbursementPort.getMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
