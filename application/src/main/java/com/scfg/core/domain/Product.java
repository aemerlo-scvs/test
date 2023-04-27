package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Product extends BaseDomain {

    private Integer renewalIdc;

    private Integer businessLineIdc;

    private Integer agreementCode;

    private String apsCode;

    private String name;

    private String description;

    private Integer minimumEntryAge;

    private Integer entryAgeLimit;

    private Integer ageLimitStay;

    private String nomenclature;

    private Integer claimNotificationLimit;

    private Integer settlementLimit;

    private String resolution;

    private Integer numberDayPastDue;

    private Long branchId;

    private String initials;

    private Long correlativeNumber;

    private Integer billable;
}
