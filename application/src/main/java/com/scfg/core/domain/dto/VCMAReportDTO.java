package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class VCMAReportDTO implements Serializable {

    private static final long serialVersionUID = 1297236517527724393L;
    private Integer managerId;
    private String names;
    private Integer agencyId;
    private String descriptiona;
    private Integer branchOfficeId;
    private String descriptions;
    private Integer zonesId;
    private String descriptionz;
    private Integer p1;
    private Integer sp1;
    private Integer p2;
    private Integer sp2;
    private Integer p3;
    private Integer sp3;
    private Integer nump;
    private Integer montop;
}
