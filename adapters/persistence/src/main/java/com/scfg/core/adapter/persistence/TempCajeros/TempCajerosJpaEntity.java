package com.scfg.core.adapter.persistence.TempCajeros;

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
@Table(name = "tempCajeros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TempCajerosJpaEntity extends BaseJpaEntity {

    @Column(name = "codClient",unique = true)
    private Long codClient;

    @Column(name = "cajeros")
    private Integer cajeros;
    @Column(name = "codAgency")
    private Integer codAgency;

    @Column(name = "descriptionPosition")
    private String descriptionPosition;
    @Column(name = "statusLoad")
    private String statusLoad;
    @Column(name = "names")
    private String names;
    @Column(name = "namesAgency")
    private String namesAgency;
    @Column(name = "objectives")
    private Integer objectives;
    @Column(name = "rural")
    private String rural;

    @Column(name = "codBranch")
    private Integer codBranch;

    @Column(name = "branch")
    private String branch;
    @Column(name = "typePaf")
    private String typePaf;
    @Column(name = "zones")
    private String zones;
    @Column(name = "dateNF")
    private Date dateNF;
    @Column(name = "dateCt")
    private Date dateCt;
}
