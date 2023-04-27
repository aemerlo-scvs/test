package com.scfg.core.domain.dto.credicasas.groupthefont.requestDto;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.JuridicalPersonDTO;
import com.scfg.core.domain.person.Person;
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
public class RequestFontDTO {
    private Long userId;
    private Long userOfficeIdc;
    private Long userRegionalIdc;
    private JuridicalPersonDTO company;
    private AcceptanceCriteriaDTO acceptanceCriteria;
    private Person person;
    private List<Beneficiary> beneficiaries;
    private CreditDTO credit;
    private List<AnswerDTO> answers;
    private FileDocumentDTO sign;
    private Integer legalHeirs;
}
