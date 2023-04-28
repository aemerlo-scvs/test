package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.common.AnnexeRequirementControl;
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
@ApiModel(description = "DTO para actualizar la solicitud de anexo")
public class UpdateRequestAnnexeDTO {
    private Long requestAnnexeId;
    private Integer annulmentReasonIdc;
    private List<AnnexeRequirementControl> requestList;
}
