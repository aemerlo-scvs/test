package com.scfg.core.application.port.in;

import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhnDTO;

import java.util.List;
import java.util.Map;

public interface ManualCertificateUseCase {


    PersistenceResponse registerManualCertificatesForRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<ManualCertificateDhlDTO> manualCertificatesDhl,
            long overwrite);

    PersistenceResponse registerManualCertificatesForNotRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<ManualCertificateDhnDTO> manualCertificatesDhn,
            long overwrite);

    List<ManualCertificateDhlDTO> getManualCertificateDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<ManualCertificateDhnDTO> getManualCertificateDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}


