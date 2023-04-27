package com.scfg.core.adapter.persistence.question;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class QuestionJpaEntity extends BaseJpaEntity {

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "screenContent", length = 500)
    private String screenContent;

    @Column(name = "questionCategoryIdc")
    private Integer questionCategoryIdc;

    @Column(name = "questionTypeIdc")
    private Integer questionTypeIdc;

    @Column(name = "designTypeIdc")
    private Integer designTypeIdc;

    @Column(name = "parentId")
    private Long parentId;

}
