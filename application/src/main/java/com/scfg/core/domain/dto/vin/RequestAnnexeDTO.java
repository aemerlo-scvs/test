package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.RequestPolicyDetailDto;
import com.scfg.core.domain.person.Person;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "DTO para realizar la solicitud de anexo")
public class RequestAnnexeDTO {
    private RequestPolicyDetailDto policyDetail;
    private Long annexeTypeId;
    private String annulmentReason;
    private List<RequirementDTO> requestList;
    private Plan plan;
    private List<CoverageDTO> coverageList;
    private Person insurerCompany;
}
