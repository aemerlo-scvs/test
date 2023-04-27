package com.scfg.core.application.service;

import com.scfg.core.application.port.in.OldMonthlyDisbursementCreditLineUseCase;
import com.scfg.core.application.port.out.MortgageReliefItemPort;
import com.scfg.core.application.port.out.OldMonthlyDisbursementCreditLinePort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@UseCase
@RequiredArgsConstructor
public class OldMonthlyDisbursementCreditLineService implements OldMonthlyDisbursementCreditLineUseCase {

    private final OldMonthlyDisbursementCreditLinePort oldMonthlyDisbursementCreditLinePort;
    private final MortgageReliefItemPort mortgageReliefItemPort;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse saveOldMonthlyDisbursementsForRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<OldMonthlyDisbursementCreditLineDhlDTO> oldMonthlyDisbursementCreditLineDhlDTOS, long overwrite) {
        // Cast object for persistence db
        /*oldMonthlyDisbursementCreditLineDhlDTOS.stream()
                .forEach(monthlyDisbursement -> {
                    try {
                        // create mortgageReliefItem
                        monthlyDisbursement.setITEM_DESGRAVAMEN(MortgageReliefItem.builder()
                                .policyTypeIdc(policyTypeId)
                                .monthIdc(monthId)
                                .yearIdc(yearId)
                                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                                .reportTypeIdc(reportTypeId)
                                .usersId(usersId)
                                .build());
                    } catch (NotDataFoundException e) {
                        // Registrar nuevo clasificador
                        //classifierPort.save(null);
                    }
                });

                if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION){
            // disable last registers
            mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);
        }
        return oldMonthlyDisbursementCreditLinePort.registerOldMonthlyDisbursementsForRegulatedPolicy(oldMonthlyDisbursementCreditLineDhlDTOS, overwrite);*/
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse saveOldMonthlyDisbursementsForNotRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<OldMonthlyDisbursementCreditLineDhnDTO> oldMonthlyDisbursementCreditLineDhnDTOS, long overwrite) {
        oldMonthlyDisbursementCreditLineDhnDTOS.stream()
                .forEach(monthlyDisbursement -> {
                    try {
                        // mortgageReliefItem
                        monthlyDisbursement.setITEM_DESGRAVAMEN(MortgageReliefItem.builder()
                                .policyTypeIdc(policyTypeId)
                                .monthIdc(monthId)
                                .yearIdc(yearId)
                                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                                .reportTypeIdc(reportTypeId)
                                .usersId(usersId)
                                .build());
                    } catch (NotDataFoundException e) {
                        // Registrar nuevo clasificador
                        //classifierPort.save(null);
                    }
                });
        if (overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION){
            // disable last registers
            mortgageReliefItemPort.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId, usersId, insurancePolicyHolderId);
        }

        return oldMonthlyDisbursementCreditLinePort.registerOldMonthlyDisbursementsForNotRegulatedPolicy(oldMonthlyDisbursementCreditLineDhnDTOS, overwrite);

    }

    @Override
    public List<OldMonthlyDisbursementCreditLineDhlDTO> getOldMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return oldMonthlyDisbursementCreditLinePort.getOldMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<OldMonthlyDisbursementCreditLineDhnDTO> getOldMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return oldMonthlyDisbursementCreditLinePort.getOldMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }


}
