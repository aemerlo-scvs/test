package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Document;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "DTO para la lista de requerimientos para una solicitud de anexo")
public class RequirementDTO {
    private Long id;
    private String description;
    private String requestDate;
    private String receptionDate;
    private String comment;

    private Document fileDocument;
    private Long requirementId;
}
