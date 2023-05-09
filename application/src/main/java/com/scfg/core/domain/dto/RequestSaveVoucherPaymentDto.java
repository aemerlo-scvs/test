package com.scfg.core.domain.dto;

import com.scfg.core.domain.Document;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class RequestSaveVoucherPaymentDto {
    private Long voucherNumber;
    private Document voucher;

}
