package com.scfg.core.domain.dto.vin;


import com.scfg.core.domain.Document;
import com.scfg.core.domain.dto.AnnexeFileDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
public class AnnexeRequirementDto {
   private Long requestAnnexeId;
   private Long id;
   private String description;
   private String requestDate;
   private String receptionDate;
   private String comment;
   private Long requirementId;
   private boolean edited;
   private Boolean signed;
   private Document fileDocument;

    public AnnexeRequirementDto(Long id, Long requestAnnexeId, String description, LocalDateTime requestDate,
                                LocalDateTime receptionDate, String comment, Long requirementId, Long fdId,
                                String fdDescription, Integer fdDocumentTypeIdc, String content, String mimeType, Boolean signed) {
        this.id = id;
        this.requestAnnexeId = requestAnnexeId;
        this.description = description != null ? description : "";
        this.requestDate = requestDate.toString();
        this.receptionDate = receptionDate != null ? receptionDate.toString(): "";
        this.comment = comment;
        this.requirementId = requirementId;
        if (signed == null) {
            this.signed = false;
        } else {
            this.signed = signed;
        }
        if (fdId != null) {
            this.fileDocument = Document.builder()
                    .id(fdId)
                    .content(content)
                    .mimeType(mimeType)
                    .description(fdDescription)
                    .documentTypeIdc(fdDocumentTypeIdc)
                    .build();
        }
    }

}
