package com.scfg.core.adapter.persistence.broker;


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
@Table(name = "Broker")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BrokerJpaEntity extends BaseJpaEntity {
    @Column(name = "businessName", length = 100)
    private String businessName;

    @Column(name = "nit")
    private Long nit;

    @Column(name = "address", length = 150)
    private String address;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "cityIdc")
    private long cityIdc;

    @Column(name = "approvalCode", length = 50)
    private String approvalCode;
}
