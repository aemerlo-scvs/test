package com.scfg.core.application.port.out;

import com.scfg.core.domain.PaymentFileDocument;


public interface PaymentFileDocumentPort {
    Long saveOrUpdate(PaymentFileDocument paymentFileDocument);
    Long getMaxNumberTransactionFileDocument();
}
