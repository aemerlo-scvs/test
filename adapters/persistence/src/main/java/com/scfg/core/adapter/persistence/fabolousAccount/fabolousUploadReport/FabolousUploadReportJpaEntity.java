package com.scfg.core.adapter.persistence.fabolousAccount.fabolousUploadReport;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = HelpersConstants.TABLE_FBS_UPLOAD_REPORT)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousUploadReportJpaEntity extends BaseJpaEntity {

//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "reportDateUpload")
    private Date reportDateUpload;

    private Long reportTypeIdc; //Necessary to get Name of report

    private Long policyIdc; //Necessary to relation with Fabulosa policy

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "userId")
    private UserJpaEntity user;

    private Long monthIdc;

    private Long yearIdc;

    private Long totalUpload;
}
