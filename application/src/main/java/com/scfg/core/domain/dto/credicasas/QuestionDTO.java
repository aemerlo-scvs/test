package com.scfg.core.domain.dto.credicasas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scfg.core.domain.common.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@Getter
@Setter
@SuperBuilder
public class QuestionDTO extends BaseDomain {


    @JsonProperty("content")
    private String content;

    @JsonProperty("screenContent")
    private String screenContent;

    @JsonProperty("questionCategoryIdc")
    private Integer questionCategoryIdc;

    @JsonProperty("questionTypeIdc")
    private Integer questionTypeIdc;

    @JsonProperty("designTypeIdc")
    private Integer designTypeIdc;

    @JsonProperty("parentId")
    private Long parentId;

    public QuestionDTO() {}

    public QuestionDTO(String content, String screenContent, Integer questionCategoryIdc, Integer questionTypeIdc, Integer designTypeIdc, Long parentId) {
        this.content = content;
        this.screenContent = screenContent;
        this.questionCategoryIdc = questionCategoryIdc;
        this.questionTypeIdc = questionTypeIdc;
        this.designTypeIdc = designTypeIdc;
        this.parentId = parentId;
    }
}
