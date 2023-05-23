package com.scfg.core.adapter.persistence.mathReserve;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MathReserve")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MathReserveJpaEntity extends BaseJpaEntity {

    @Column(name = "[vigencyYears]")
    private Integer vigencyYears;

    @Column(name = "[age]")
    private Integer age;

    @Column(name = "[value]")
    private Float value;

    @Column(name = "[status]")
    private Integer status;

    @Column(name = "[insuredCapital]")
    private Float insuredCapital;

    @Column(name = "[percentageRate]")
    private Float percentageRate;

    @Column(name = "[version]")
    private String version;
}
