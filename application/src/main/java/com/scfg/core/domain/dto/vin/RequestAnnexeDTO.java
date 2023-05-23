package com.scfg.core.domain.dto.vin;

import com.scfg.core.common.enums.RequestAnnexeStatusEnum;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.common.AnnexeRequirementControl;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.RequestPolicyDetailDto;
import com.scfg.core.domain.person.Person;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "DTO para crear la solicitud de anexo")
public class RequestAnnexeDTO {

    private Long annexeTypeId;
    private LocalDateTime requestDate;
    private Integer annulmentReasonIdc;
    private String comment;
    private List<AnnexeRequirementControl> requestList;
    private RequestPolicyDetailDto policyDetail;
    private Plan plan;
    private List<CoverageDTO> coverageList;
    private Person insurerCompany;
    private RequestAnnexeStatusEnum requestStatusEnum;

    public String getPersonCompleteName() {
        return this.getPolicyDetail().getNames() + " " + this.getPolicyDetail().getLastName();
    }

}
