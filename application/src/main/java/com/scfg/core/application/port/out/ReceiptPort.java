package com.scfg.core.application.port.out;

import com.scfg.core.domain.Receipt;

import java.util.List;

public interface ReceiptPort {
    long saveOrUpdate(Receipt receipt);
    Receipt gReceiptForNumberReceipt(String voucherNumber);
    Receipt getReceiptForGeneralRequestById (Long request);
    Receipt getReceiptForGeneralRequestByIdList (List<Long> request);

}
