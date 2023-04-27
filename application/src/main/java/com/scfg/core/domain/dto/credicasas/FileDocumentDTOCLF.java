package com.scfg.core.domain.dto.credicasas;

//import org.hibernate.validator.constraints.NotEmpty;

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
public class FileDocumentDTOCLF {
    private Long id;
    private Long typeId;
    private String name;
    private String content;
    private String mime;
    private String path;

}
