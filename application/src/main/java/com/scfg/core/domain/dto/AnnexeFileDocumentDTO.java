package com.scfg.core.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class AnnexeFileDocumentDTO implements Serializable {
    private static final long serialVersionUID = 5441616027440750296L;
    private Long id;
    private Integer typeId;
    @NotEmpty(message = "name cannot be empty")
    private String description;
    @NotEmpty(message = "content cannot be empty")
    private String content;
    private String mime;
    private Integer documentTypeIdc;

    public AnnexeFileDocumentDTO(Long id, Integer typeId, @NotEmpty(message = "name cannot be empty") String name, byte[] content, String mime, Integer documentTypeIdc) {
        this.id = id;
        this.typeId = typeId;
        this.description = name;
        this.content = Base64.getEncoder().encodeToString(content);
        this.mime = mime;
        this.documentTypeIdc = documentTypeIdc;
    }
}
