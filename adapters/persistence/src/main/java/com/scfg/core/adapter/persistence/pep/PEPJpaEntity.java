package com.scfg.core.adapter.persistence.pep;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Pep")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PEPJpaEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "lastName", length = 150)
    private String lastName;

    @Column(name = "motherLastName", length = 150)
    private String motherLastName;

    @Column(name = "identificationNumber", length = 50)
    private String identificationNumber;

    @Column(name = "issuancePlace", length = 25)
    private String issuancePlace;

    @Column(name = "charge", length = 255)
    private String charge;

    @Column(name = "countryCharge", length = 150)
    private String countryCharge;

    @Column(name = "pepType", length = 15)
    private String pepType;

}
