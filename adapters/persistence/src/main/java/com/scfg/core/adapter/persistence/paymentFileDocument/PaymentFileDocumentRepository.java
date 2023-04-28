package com.scfg.core.adapter.persistence.paymentFileDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentFileDocumentRepository extends JpaRepository<PaymentFileDocumentJpaEntity, Long> {

    @Query("SELECT MAX(a.documentNumber) FROM PaymentFileDocumentJpaEntity a")
    Long getMaxNumberPaymentFileDocument();

}
