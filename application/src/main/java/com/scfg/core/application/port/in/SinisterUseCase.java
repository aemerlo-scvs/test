package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhnDTO;

import java.util.List;

public interface SinisterUseCase {

    PersistenceResponse saveSinistersForRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<SinisterDhlDTO> sinisterDhlDTOS, long overwrite);

    PersistenceResponse saveSinistersForNotRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<SinisterDhnDTO> sinisterDhnDTOS, long overwrite);

    List<SinisterDhlDTO> getSinistersDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<SinisterDhnDTO> getSinistersDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

}
