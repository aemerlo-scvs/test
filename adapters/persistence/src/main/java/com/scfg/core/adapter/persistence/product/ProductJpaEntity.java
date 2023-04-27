package com.scfg.core.adapter.persistence.product;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ProductJpaEntity extends BaseJpaEntity {

    @Column(name = "renewalIdc")
    private Integer renewalIdc;

    @Column(name = "businessLineIdc")
    private Integer businessLineIdc;

    @Column(name = "agreementCode")
    private Integer agreementCode;

    @Column(name = "apsCode")
    private String apsCode;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "minimumEntryAge")
    private Integer minimumEntryAge;

    @Column(name = "entryAgeLimit")
    private Integer entryAgeLimit;

    @Column(name = "ageLimitStay")
    private Integer ageLimitStay;

    @Column(name = "nomenclature", length = 50)
    private String nomenclature;

    @Column(name = "claimNotificationLimit")
    private Integer claimNotificationLimit;

    @Column(name = "settlementLimit")
    private Integer settlementLimit;

    @Column(name = "resolution", length = 200)
    private String resolution;

    @Column(name = "numberDayPastDue")
    private Integer numberDayPastDue;

    @Column(name = "branchId")
    private Long branchId;

    @Column(name = "initials")
    private String initials;

    @Column(name = "correlativeNumber")
    private Long correlativeNumber;

    @Column(name = "billable")
    private Integer billable;
}
