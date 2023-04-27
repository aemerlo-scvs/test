package com.scfg.core.adapter.persistence.receipt;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiptRepository extends JpaRepository<ReceiptJpaEntity, Long> {
    @Query(value = "SELECT TOP 1 cl.* " +
            "FROM  Receipt cl " +
            "WHERE cl.voucherNumber = :voucherNumber ", nativeQuery = true)
    Optional<ReceiptJpaEntity> findByReceiptJpaEntityVoucher(@Param("voucherNumber") String voucherNumber);

    @Query("SELECT r \n" +
            "FROM ReceiptJpaEntity r \n" +
            "INNER JOIN TransactionJpaEntity t ON t.id = r.transactionId \n" +
            "INNER JOIN PaymentPlanJpaEntity pp ON pp.id = t.paymentPlanId \n" +
            "INNER JOIN PaymentJpaEntity p ON p.id = pp.paymentId \n" +
            "INNER JOIN GeneralRequestJpaEntity gr ON gr.id = p.generalRequestId \n" +
            "WHERE gr.id= :requestId")
    List<ReceiptJpaEntity> findReceiptByGeneralRequestId(@Param("requestId") Long requestId);

    //#region Deprecated

    @Query(value = "SELECT TOP 1 R.*FROM GeneralRequest G INNER JOIN Payment P ON G.id=P.generalRequestId " +
            "INNER JOIN PaymentPlan PP ON P.id=PP.paymentId " +
            "INNER JOIN [Transaction] T ON PP.id=T.paymentPlanId " +
            "INNER JOIN Receipt R ON T.id=R.transactionId " +
            "WHERE G.id= :requestId ", nativeQuery = true)
    Optional<ReceiptJpaEntity> findReceiptForGeneralById(@Param("requestId") Long requestId);

    //#endregion

    @Query("SELECT r \n" +
            "FROM ReceiptJpaEntity r \n" +
            "INNER JOIN TransactionJpaEntity t ON t.id = r.transactionId \n" +
            "INNER JOIN PaymentPlanJpaEntity pp ON pp.id = t.paymentPlanId \n" +
            "INNER JOIN PaymentJpaEntity p ON p.id = pp.paymentId \n" +
            "INNER JOIN GeneralRequestJpaEntity gr ON gr.id = p.generalRequestId \n" +
            "WHERE gr.id IN (:requestId)")
    List<ReceiptJpaEntity> findReceiptByGeneralRequestIdList(@Param("requestId") List<Long> requestId);

}
