package com.scfg.core.domain.dto.credicasas;

import com.scfg.core.common.enums.CLFResponseEnum;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class ClfProcessRequestDTO {
    private List<FileDocumentDTO> files;
    private Integer response;
    private String message;
}
