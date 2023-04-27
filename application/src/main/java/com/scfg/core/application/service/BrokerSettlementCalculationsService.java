package com.scfg.core.application.service;

import com.scfg.core.application.port.in.BrokerSettlementCalculationsUseCase;
import com.scfg.core.application.port.out.BrokerSettlementCalculationsPort;
import com.scfg.core.application.port.out.ClassifierPort;
import com.scfg.core.application.port.out.MortgageReliefItemPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@UseCase
@RequiredArgsConstructor
public class BrokerSettlementCalculationsService implements BrokerSettlementCalculationsUseCase {

    private final BrokerSettlementCalculationsPort brokerSettlementCalculationsPort;
    private final MortgageReliefItemPort mortgageReliefItemPort;
    private final ClassifierPort classifierPort;
    private long rollbackMortgageReliefItem = 1;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
            NotDataFoundException.class,
            ClassifierNotFoundException.class,
            Exception.class,
            NoSuchFieldException.class
    })
    @Override
    public PersistenceResponse saveCalculationsForRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<BrokerSettlementCalculationsDhlDTO> brokerSettlementCalculationsDhlDTO,
            long overwrite) {

        // Cast object for persistence db
        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> observationTypeErrors = new ArrayList<>();

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
        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        brokerSettlementCalculationsDhlDTO.stream()
                .forEach(brokerCalculation -> {
                    String insuredCovergeName = brokerCalculation.getITEMS();
                    long insuredCovergeId = 0, rollbackCreditOperation = 0l,
                            rollbackInsuranceRequest = 0l, rollbackClient = 0l;
                    try {

                        // coverage
                        insuredCovergeId = classifierPort.getClassifierByName(insuredCovergeName, ClassifierTypeEnum.InsuranceCoverage)
                                .getId();

                        // assign relationship
                        brokerCalculation.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        brokerCalculation.setID_COBERTURA_ASEGURADOS(insuredCovergeId);

                        //brokerCalculation.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | ClassifierNotFoundException e) {
                        observationTypeErrors.add(insuredCovergeName);

                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });
        if (observationTypeErrors.isEmpty()) {
            return brokerSettlementCalculationsPort.registerCalculationsForRegulatedPolicy(brokerSettlementCalculationsDhlDTO, overwrite);
        } else { // send errors to API Controller
            // InsuranceCoverage
            errors.put(ClassifierTypeEnum.InsuranceCoverage
                    .getName(), observationTypeErrors);
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
    public PersistenceResponse saveCalculationsForNotRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<BrokerSettlementCalculationsDhnDTO> brokerSettlementCalculationsDhnDTO,
            long overwrite) {

        PersistenceResponse persistenceResponse = new PersistenceResponse()
                .setResourceName(NotDataFoundException.class.getSimpleName())
                .setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);

        Map<String, List<String>> errors = new HashMap<>();
        List<String> observationTypeErrors = new ArrayList<>();

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
        }
        long mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        brokerSettlementCalculationsDhnDTO.stream()
                .forEach(brokerCalculation -> {

                    String insuredCovergeName = brokerCalculation.getLINEA();

                    long insuredCovergeId = 0, rollbackCreditOperation = 0l,
                            rollbackInsuranceRequest = 0l, rollbackClient = 0l;
                    try {

                        // coverage
                        insuredCovergeId = classifierPort.getClassifierByName(insuredCovergeName, ClassifierTypeEnum.InsuranceCoverage)
                                .getId();

                        // assign relationship
                        brokerCalculation.setID_ITEM_DESGRAVAMEN(mortgageReliefItemId);
                        brokerCalculation.setID_COBERTURA_ASEGURADOS(insuredCovergeId);

                        //brokerCalculation.setITEM_DESGRAVAMEN(mortgageReliefItem);

                    } catch (NotDataFoundException | ClassifierNotFoundException e) {
                        observationTypeErrors.add(insuredCovergeName);
                        // rollback related entities
                        mortgageReliefItemPort.callSpRollbackRelatedEntities(
                                rollbackCreditOperation, rollbackInsuranceRequest,
                                rollbackClient, rollbackMortgageReliefItem);
                        if (rollbackMortgageReliefItem == 1) {
                            rollbackMortgageReliefItem = 0;
                        }
                    }
                });

        if (observationTypeErrors.isEmpty()) {

            return brokerSettlementCalculationsPort.registerCalculationsForNotRegulatedPolicy(brokerSettlementCalculationsDhnDTO, overwrite);
        } else { // send errors to API Controller
            // InsuranceCoverage
            errors.put(ClassifierTypeEnum.InsuranceCoverage
                    .getName(), observationTypeErrors);
            return persistenceResponse.setData(errors);
        }
    }

    @Override
    public List<BrokerSettlementCalculationsDhlDTO> getBrokerSettlementCalculationsDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return brokerSettlementCalculationsPort.getBrokerSettlementCalculationsDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<BrokerSettlementCalculationsDhnDTO> getBrokerSettlementCalculationsDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return brokerSettlementCalculationsPort.getBrokerSettlementCalculationsDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
