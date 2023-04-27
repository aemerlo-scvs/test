package com.scfg.core.domain;

import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.common.BaseDomain;
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
public class PolicyFileDocument extends BaseDomain {

    private Long policyId; //TODO Will be deprecated
    private Long fileDocumentId;
    private Date uploadDate;
    private Long policyItemId;
    private String documentNumber;
    private Integer isSigned;

    //#region Custom Constructors


    public PolicyFileDocument(Long policyItemId, Long fileDocumentId, LocalDate uploadDate) {
        this.policyItemId = policyItemId;
        this.fileDocumentId = fileDocumentId;
        this.uploadDate = DateUtils.asDate(uploadDate);
        this.isSigned = 1;
    }

    //#endregion

}
