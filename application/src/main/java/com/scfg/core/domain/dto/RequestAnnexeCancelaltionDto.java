package com.scfg.core.domain.dto;

import com.scfg.core.domain.dto.vin.RequirementEditedDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RequestAnnexeCancelaltionDto {
    private Long policyId;
    private Long annexeTypeId;
    private Date endDate;
    private Long statusTechnicalPosition;
    private List<RequirementEditedDTO> requirements;
    private String commentTechnicalPosition;
}
