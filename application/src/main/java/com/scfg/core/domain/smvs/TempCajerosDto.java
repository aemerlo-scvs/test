package com.scfg.core.domain.smvs;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class TempCajerosDto extends BaseDomain {

    private int cajeros;

    private Integer codAgency;


    private Long codClient;

    private String descriptionPosition;

    private String statusLoad;

    private String names;

    private String namesAgency;

    private int objectives;


    private String rural;

    private String branch;

    private String typePaf;

    private String zones;

    private Date dateNF;
    private Integer codBranch;
    private Date dateCt;
    private Integer status;
}
