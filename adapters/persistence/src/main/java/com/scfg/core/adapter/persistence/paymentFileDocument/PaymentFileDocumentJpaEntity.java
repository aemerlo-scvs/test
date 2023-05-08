package com.scfg.core.adapter.persistence.paymentFileDocument;

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
import java.util.Date;

@Entity
@Table(name = "PaymentFileDocument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PaymentFileDocumentJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;
    @Column(name = "documentNumber")
    private String documentNumber;
    @Column(name = "content", length = 900)
    private String content;
    @Column(name = "mimeType")
    private String mimeType;

}
