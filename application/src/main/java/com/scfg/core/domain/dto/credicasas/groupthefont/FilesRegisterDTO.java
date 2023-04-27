package com.scfg.core.domain.dto.credicasas.groupthefont;

import com.scfg.core.domain.dto.FileDocumentDTO;
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
public class FilesRegisterDTO {

    FileDocumentDTO file;
    Integer isSigned;

}
