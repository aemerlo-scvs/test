package com.scfg.core.domain.smvs;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.person.Person;
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
public class SavePolicyDTO {
    private long requestId;
    private Person person;
    private List<Beneficiary> beneficiaryList;
    private List<FileDocumentDTO> documentList;
    private FileDocumentDTO documentFirm;
}
