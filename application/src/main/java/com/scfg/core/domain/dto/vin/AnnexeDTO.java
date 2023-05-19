package com.scfg.core.domain.dto.vin;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "DTO para la lista de anexo")
public class AnnexeDTO {
    private Long id;
    private Integer annexeNumber;
    private Integer annexeTypeIdc;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime issuanceDate;
    private Long policy;
    private Long requestAnnexe;
    private Integer status;
}
