package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;

import java.util.List;

public interface BrokerSettlementCalculationsUseCase  {

    PersistenceResponse saveCalculationsForRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long usersId, List<BrokerSettlementCalculationsDhlDTO> brokerSettlementCalculationsDhlDTO, long overwrite);

    PersistenceResponse saveCalculationsForNotRegulatedPolicy(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId,  long usersId, List<BrokerSettlementCalculationsDhnDTO> brokerSettlementCalculationsDhnDTO, long overwrite);

    List<BrokerSettlementCalculationsDhlDTO> getBrokerSettlementCalculationsDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<BrokerSettlementCalculationsDhnDTO> getBrokerSettlementCalculationsDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

}
