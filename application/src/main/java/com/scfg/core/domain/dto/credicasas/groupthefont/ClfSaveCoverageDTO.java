package com.scfg.core.domain.dto.credicasas.groupthefont;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class ClfSaveCoverageDTO {
    private Long requestId;
    private String exclusionComment;
    private List<ClfProductPlanCoverageDTO> coverageList;
}
