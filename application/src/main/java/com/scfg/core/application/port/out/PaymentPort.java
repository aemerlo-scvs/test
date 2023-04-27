package com.scfg.core.application.port.out;

import com.scfg.core.domain.Payment;

public interface PaymentPort {

    long saveOrUpdate(Payment payment);

    Payment findByGeneralRequest(Long generalRequestId);

}
