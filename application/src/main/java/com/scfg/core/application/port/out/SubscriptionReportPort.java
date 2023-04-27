package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionReportDTO;

import java.util.List;

public interface SubscriptionReportPort {

    List<SubscriptionReportDTO> getSubscriptionReportSingleFiltered(String ci, String operationNumber);

    List<SubscriptionReportDTO> getSubscriptionReportFiltered(String policyName, long monhtNumber, long year);
}
