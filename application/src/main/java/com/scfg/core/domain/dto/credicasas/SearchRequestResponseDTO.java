package com.scfg.core.domain.dto.credicasas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SearchRequestResponseDTO {

    private List<RequestDetailDTO> requestDetailDTOList;
    private Long requestsQuantity;
}
