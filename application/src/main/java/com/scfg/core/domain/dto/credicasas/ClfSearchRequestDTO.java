package com.scfg.core.domain.dto.credicasas;

import com.scfg.core.domain.dto.SearchRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ClfSearchRequestDTO {
    private Boolean isUnsigned;
    private Boolean isPendingRequirement;
    private SearchRequestDTO searchRequest;
}
