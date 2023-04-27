package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface OldMonthlyDisbursementCreditLineUseCase {
    PersistenceResponse saveOldMonthlyDisbursementsForRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<OldMonthlyDisbursementCreditLineDhlDTO> oldMonthlyDisbursementCreditLineDhlDTOS, long overwrite);

    PersistenceResponse saveOldMonthlyDisbursementsForNotRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<OldMonthlyDisbursementCreditLineDhnDTO> oldMonthlyDisbursementCreditLineDhnDTOS, long overwrite);

    List<OldMonthlyDisbursementCreditLineDhlDTO> getOldMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<OldMonthlyDisbursementCreditLineDhnDTO> getOldMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
