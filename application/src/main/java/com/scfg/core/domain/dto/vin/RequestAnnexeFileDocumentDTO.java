package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Document;
import com.scfg.core.domain.common.AnnexeRequirementControl;
import com.scfg.core.domain.common.RequestAnnexe;
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
public class RequestAnnexeFileDocumentDTO {
    RequestAnnexe requestAnnexe;
    AnnexeRequirementControl annexeRequirementControl;
}
