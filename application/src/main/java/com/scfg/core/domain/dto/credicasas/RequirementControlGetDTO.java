package com.scfg.core.domain.dto.credicasas;

import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequirementControlGetDTO {

    private Long id;
    private String description;
    private LocalDate requestDate;
    private LocalDate receptionDate;
    private String comment;
    private Long requirementId;
    private Long policyItemId;
    private FileDocumentDTO fileDocument;
}
