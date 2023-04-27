package com.scfg.core.domain.dto.fabolous;

import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class FabolusResultResponseClient {
    private List<FabolousFindClient> clientList;
    private Integer clientListQuantity;
}
