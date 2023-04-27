package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;

import java.util.List;

public interface MonthlyDisbursementGELUseCase {
    PersistenceResponse saveMonthlyDisbursements(long policyId, long monthId, long yearId, long userId, long overwrite,
                                                                   List<Object> data);

}
