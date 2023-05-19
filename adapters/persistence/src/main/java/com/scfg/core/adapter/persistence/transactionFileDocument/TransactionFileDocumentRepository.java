package com.scfg.core.adapter.persistence.transactionFileDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface TransactionFileDocumentRepository extends JpaRepository<TransactionFileDocumentJpaEntity, Long> {

    @Query("SELECT t FROM TransactionFileDocumentJpaEntity t WHERE t.id =:id AND t.status=:status")
    TransactionFileDocumentJpaEntity findById(@Param("id")Long id, @Param("status")Integer status);

    @Query("SELECT MAX(a.id) FROM TransactionFileDocumentJpaEntity a")
    Long getMaxNumberTransactionFileDocument();
}
