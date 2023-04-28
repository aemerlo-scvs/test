package com.scfg.core.adapter.persistence.vinReport;

import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VinReportRepository extends JpaRepository<PolicyJpaEntity, String> {

    default String getProductionReport() {
        return "EXEC sp_vin_production_report :fromDate, :toDate, :policyStatusIdc";
    }


    default String getCommercialReport() {
        return "EXEC sp_vin_sale_report :fromDate, :toDate, :policyStatusIdc";
    }
}
