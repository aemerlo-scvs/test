package com.scfg.core.adapter.persistence.account;

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
@Table(name = "Account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class AccountJpaEntity extends BaseJpaEntity {
    @Column(name = "accountTypeIdc")
    private Integer accountTypeIdc;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "accountCurrencyTypeIdc")
    private Integer accountCurrencyTypeIdc;

    @Column(name = "personId")
    private Long personId;
    @Column(name = "financialEntityIdc")
    private Integer financialEntityIdc;
    @Column(name = "accountPersonName")
    private String accountPersonName;
    @Column(name = "accountIdentificationNumber")
    private Long accountIdentificationNumber;
    @Column(name = "newPersonId")
    private Long newPersonId;
}
