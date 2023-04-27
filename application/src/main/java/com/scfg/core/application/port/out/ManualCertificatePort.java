package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhnDTO;

import java.util.List;

public interface ManualCertificatePort {


    PersistenceResponse registerManualCertificatesForNotRegulatedPolicy(List<ManualCertificateDhnDTO> manualCertificatesDhn, long overwrite);

    PersistenceResponse registerManualCertificatesForRegulatedPolicy(List<ManualCertificateDhlDTO> manualCertificatesDhl, long overwrite);

    List<ManualCertificateDhlDTO> getManualCertificateDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<ManualCertificateDhnDTO> getManualCertificateDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
