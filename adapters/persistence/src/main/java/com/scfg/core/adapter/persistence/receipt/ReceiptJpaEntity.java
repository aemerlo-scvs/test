package com.scfg.core.adapter.persistence.receipt;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "Receipt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ReceiptJpaEntity extends BaseJpaEntity {
    @Column(name = "voucherNumber", length = 100)
    private String voucherNumber;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "concept", length = 100)
    private String concept;

    @Column(name = "totalAmount")
    private Double totalAmount;

    @Column(name = "literalAmount", length = 250)
    private String literalAmount;

    @Column(name = "sellerId")
    private Integer sellerId;

    @Column(name = "sellerName", length = 100)
    private String sellerName;

    @Column(name = "salePlace", length = 150)
    private String salePlace;

    @Column(name = "agencyId")
    private Integer agencyId;

    @Column(name = "agencyName", length = 100)
    private String agencyName;

    @Column(name = "observation", length = 150)
    private String observation;

    @Column(name = "receiptStatusIdc")
    private Integer receiptStatusIdc;

    @Column(name = "transactionId")
    private Long transactionId;

}
