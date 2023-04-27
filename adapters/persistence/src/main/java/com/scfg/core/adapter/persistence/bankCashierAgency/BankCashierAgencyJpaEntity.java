package com.scfg.core.adapter.persistence.bankCashierAgency;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "bankCashierAgency")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BankCashierAgencyJpaEntity extends BaseJpaEntity {
    @Column(name = "agencyId")
    private Long agencyId;
    @Column(name = "bankCashierId")
    private Long bankCashierId;
    @Column(name = "courtDate")
    private Date courtDate;
    @Column(name = "admissionDate")
    private Date admissionDate;
    @Column(name = "objetives")
    private Integer objetives;
}
