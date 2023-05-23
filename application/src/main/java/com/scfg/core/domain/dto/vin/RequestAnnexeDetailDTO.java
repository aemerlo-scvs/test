package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.common.RequestAnnexe;
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
public class RequestAnnexeDetailDTO {
    RequestAnnexe requestAnnexe;
    String paymentDesc;
    List<AnnexeRequirementDto> annexeRequirementList;
}
