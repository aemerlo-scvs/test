package com.scfg.core.application.service;

import com.scfg.core.application.port.in.BrokerSettlementCalculationsUseCase;
import com.scfg.core.application.port.in.SinisterUseCase;
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
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.extract.internal.SequenceInformationExtractorTimesTenDatabaseImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scfg.core.common.util.HelpersConstants.*;


@UseCase
@RequiredArgsConstructor
public class SinisterService implements SinisterUseCase {

    private final SinisterPort sinisterPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final ClientPort clientPort;
    private final ClassifierPort classifierPort;
    private final ClassifierTypePort classifierTypePort;
    private long rollbackMortgageReliefItem = 1;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ObservedCaseNotFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveSinistersForRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<SinisterDhlDTO> sinisterDhlDTOS, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> classifierErrors = new ArrayList<>();

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


        sinisterDhlDTOS.stream()
                .forEach(sinister -> {
                    Map<String, Object> clientMap;

                    long clientId = 0l, documentTypeCiIdc = 0l, currencyBsIdc = 0l;
                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;

                    try {

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        documentTypeCiIdc = documentTypeCiCls.getId();
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs.", ClassifierTypeEnum.Currency);
                        currencyBsIdc = currencyBsCls.getId();

                        // client

                        // Client
                        Client client = Client.builder()
                                .documentNumber(sinister.getCI_ASEGURADO())
                                .documentTypeId(documentTypeCiIdc)
                                .currencyId(currencyBsIdc)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // validate persistence
                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;

                        // assign relationship
                        sinister.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        sinister.setID_CLIENTE(clientId);

                        //sinister.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        classifierErrors.add("Clasificador Carnet de Identidad o Moneda");
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });
        if (classifierErrors.isEmpty()) {
            return sinisterPort.registerSinistersForRegulatedPolicy(sinisterDhlDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.DocumentType
                    .getName(), classifierErrors);
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), classifierErrors);

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
    public PersistenceResponse saveSinistersForNotRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<SinisterDhnDTO> sinisterDhnDTOS, long overwrite) {
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> classifierErrors = new ArrayList<>();

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

        sinisterDhnDTOS.stream()
                .forEach(sinister -> {
                    Map<String, Object> clientMap;
                    long clientId = 0l, documentTypeCiIdc = 0l, currencyBsIdc = 0l;

                    long rollbackCreditOperation = 0, rollbackInsuranceRequest = 0, rollbackClient = 0;
                    try {

                        Classifier documentTypeCiCls = classifierPort.getClassifierByReferences(ClassifierEnum.IdentityCard_IdentificationType);
                        documentTypeCiIdc = documentTypeCiCls.getId();
                        //Classifier documentTypeCiCls = classifierPort.getClassifierByAbbreviation("CI", ClassifierTypeEnum.DocumentType);


                        Classifier currencyBsCls = classifierPort.getClassifierByReferences(ClassifierEnum.Bs_Currency);
                        //Classifier currencyBsCls = classifierPort.getClassifierByAbbreviation("Bs2.", ClassifierTypeEnum.Currency);
                        currencyBsIdc = currencyBsCls.getId();

                        // Client
                        Client client = Client.builder()
                                .documentNumber(sinister.getCI_ASEGURADO())
                                .documentTypeId(documentTypeCiIdc)
                                .currencyId(currencyBsIdc)
                                .build();
                        clientMap = clientPort.findOrUpsert(client);
                        clientId = ((Client) clientMap.getOrDefault(KEY_CONTENT_ENTITY, null))
                                .getId();

                        // validate persistence
                        rollbackClient = clientMap.get(KEY_ACTION_ENTITY)
                                .equals(CREATE_ACTION) ? 1 : 0;


                        // assign relationship
                        sinister.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        sinister.setID_CLIENTE(clientId);

                        //sinister.setITEM_DESGRAVAMEN(mortgageReliefItem);
                    } catch (NotDataFoundException | NoSuchFieldException | IllegalAccessException | ClassifierNotFoundException e) {
                        classifierErrors.add("Clasificador Carnet de Identidad o Moneda");
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }

                    }
                });
        if (classifierErrors.isEmpty()) {
            return sinisterPort.registerSinistersForNotRegulatedPolicy(sinisterDhnDTOS, overwrite);
        } else { // send errors to API Controller
            // Observed case
            errors.put(ClassifierTypeEnum.DocumentType
                    .getName(), classifierErrors);
            errors.put(ClassifierTypeEnum.Currency
                    .getName(), classifierErrors);

            return persistenceResponse.setData(errors);
        }
    }

    @Override
    public List<SinisterDhlDTO> getSinistersDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return sinisterPort.getSinistersDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<SinisterDhnDTO> getSinistersDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return sinisterPort.getSinistersDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }
}
