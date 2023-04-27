package com.scfg.core.domain.dto.credicasas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PepValidationDTO {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate pepValidationDate;
//    private Date pepValidationDate;
    private FileDocumentDTO fileDocument;

}
