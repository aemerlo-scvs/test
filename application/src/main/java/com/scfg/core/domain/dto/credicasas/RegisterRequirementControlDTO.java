package com.scfg.core.domain.dto.credicasas;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RegisterRequirementControlDTO {

    private Long id;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate requestDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate receptionDate;
    private String comment;
    private Long requirementId;
    private Long policyItemId;
    private FileDocumentDTOCLF fileDocument;
    private Long requestId;
}
