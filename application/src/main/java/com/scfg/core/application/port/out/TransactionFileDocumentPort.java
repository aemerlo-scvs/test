package com.scfg.core.application.port.out;

import com.scfg.core.domain.TransactionFileDocument;


public interface TransactionFileDocumentPort {
     Long saveOrUpdate(TransactionFileDocument transactionFileDocument);
     TransactionFileDocument findByTransactionFileDocumentId(Long transactionFileDocumentId);
     Long getMaxNumberTransactionFileDocument();
}
