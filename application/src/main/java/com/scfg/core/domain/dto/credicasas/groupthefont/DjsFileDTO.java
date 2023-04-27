package com.scfg.core.domain.dto.credicasas.groupthefont;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.QuestionDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AcceptanceCriteriaDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.CreditDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AnswerDTO;
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
public class DjsFileDTO {
    private Person insuredValues;
    private List<Beneficiary> beneficiaryList;
    private String companyName;
    private Double insuredAge;
    private AcceptanceCriteriaDTO insuredCriteria;
    private List<AnswerDTO> answers;
    private String answerFieldsConcat;
    private CreditDTO credit;
    private Long userOfficeIdc;
    private Long userRegionalIdc;
    private String userName;
    private Integer currencyType;
    private FileDocumentDTO digitalFirm;
    private List<Classifier> classifierListForRelationTypes;
    private List<Classifier> classifierListForExtensionCi;
    private List<Classifier> listActivity;
    private List<Classifier> nationalityList;
    private List<Classifier> regionalList;
    private String maritalStatus;
    private List<QuestionDTO> questionDTOList;
    private Long djsNumber;
    private Integer legalHeirs;
}
