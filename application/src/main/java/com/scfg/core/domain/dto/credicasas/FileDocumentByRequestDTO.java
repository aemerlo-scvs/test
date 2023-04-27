package com.scfg.core.domain.dto.credicasas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class FileDocumentByRequestDTO {
    private Long id;
    private Integer typeId;
    @NotEmpty(message = "name cannot be empty")
    private String description;
    @NotEmpty(message = "content cannot be empty")
    private String content;
    private String mime;
    private Integer isSigned;
}
