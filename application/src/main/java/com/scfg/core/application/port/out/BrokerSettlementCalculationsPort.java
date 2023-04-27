package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrokerSettlementCalculationsPort {

    PersistenceResponse registerCalculationsForRegulatedPolicy(
            List<BrokerSettlementCalculationsDhlDTO> brokerSettlementCalculationsDhlDTO,
            long overwrite);

    PersistenceResponse registerCalculationsForNotRegulatedPolicy(
            List<BrokerSettlementCalculationsDhnDTO> brokerSettlementCalculationsDhnDTO,
            long overwrite);

    List<BrokerSettlementCalculationsDhlDTO> getBrokerSettlementCalculationsDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<BrokerSettlementCalculationsDhnDTO> getBrokerSettlementCalculationsDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);


}
