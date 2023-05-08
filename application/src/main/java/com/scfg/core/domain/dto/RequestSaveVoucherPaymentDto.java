package com.scfg.core.domain.dto;

import com.scfg.core.domain.Document;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class RequestSaveVoucherPaymentDto {
    private Long voucherNumber;
    private LocalDateTime payDate;
    private Document voucher;

}
