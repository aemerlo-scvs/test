package com.scfg.core.application.port.out;

import com.scfg.core.domain.Transaction;

public interface TransactionPort {
    long saveOrUpdate(Transaction transaction);
    Transaction findById(long id);
    Transaction findLastByPaymentPlanId(Long paymentPlanId);
}
