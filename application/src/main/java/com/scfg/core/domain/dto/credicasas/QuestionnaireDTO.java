package com.scfg.core.domain.dto.credicasas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scfg.core.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class QuestionnaireDTO extends BaseDomain {

    @JsonProperty("name")
    private String name;

    @JsonProperty("typeIdc")
    private Integer typeIdc;

    @JsonProperty("questions")
    private List<QuestionDTO> questions;

    public QuestionnaireDTO() {}

    public QuestionnaireDTO(String name, Integer typeIdc, List<QuestionDTO> questions) {
        this.name = name;
        this.typeIdc = typeIdc;
        this.questions = questions;
    }
}
