package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;

import java.util.List;

public interface OldMonthlyDisbursementCreditLinePort {
    PersistenceResponse registerOldMonthlyDisbursementsForRegulatedPolicy(List<OldMonthlyDisbursementCreditLineDhlDTO> oldMonthlyDisbursementCreditLineDhlDTOS, long overwrite);

    PersistenceResponse registerOldMonthlyDisbursementsForNotRegulatedPolicy(List<OldMonthlyDisbursementCreditLineDhnDTO> oldMonthlyDisbursementCreditLineDhnDTOS, long overwrite);

    List<OldMonthlyDisbursementCreditLineDhlDTO> getOldMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<OldMonthlyDisbursementCreditLineDhnDTO> getOldMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
