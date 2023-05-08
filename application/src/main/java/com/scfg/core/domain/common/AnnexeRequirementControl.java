package com.scfg.core.domain.common;

import com.scfg.core.domain.Document;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "Model para registrar los requisitos de la solicitud del anexo")
public class AnnexeRequirementControl extends BaseDomain {
    private String description;
    private String comment;
    private LocalDateTime requestDate;
    private LocalDateTime receptionDate;
    private Long fileDocumentId;
    private Long requirementId;
    private Long requestAnnexeId;
    private Boolean signed;

    private Document fileDocument;


    public AnnexeRequirementControl(String description, String comment, LocalDateTime requestDate, LocalDateTime receptionDate,
                                    Long requirementId, Boolean signed, Document fileDocument) {
        this.description = description;
        this.comment = comment;
        this.requestDate = requestDate;
        this.receptionDate = receptionDate;
        this.requirementId = requirementId;
        this.signed = signed;
        this.fileDocument = fileDocument;
    }

}
