package com.scfg.core.application.port.out;

import com.scfg.core.domain.PaymentPlan;

public interface PaymentPlanPort {
    long saveOrUpdate(PaymentPlan paymentPlan);
}
