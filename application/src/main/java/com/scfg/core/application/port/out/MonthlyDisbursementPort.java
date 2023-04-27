package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MonthlyDisbursement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonthlyDisbursementPort {
    PersistenceResponse registerMonthlyDisbursementsForRegulatedPolicy(List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite);

    PersistenceResponse registerMonthlyDisbursementsForNotRegulatedPolicy(List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhlDTOS, long overwrite);

    List<MonthlyDisbursementDhlDTO> getMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFilteredForPeriod(long monthNumber, long yearNumber, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFilteredForPeriod(long monthNumber, long yearNumber, long insurancePolicyHolderId);

    void updateCaseStatus(MonthlyDisbursement monthlyDisbursement);

    void updateAllCaseStatus(long monthId, long yearId, long insurancePolicyHolderId, long policyTypeId, int caseStatus);
}
