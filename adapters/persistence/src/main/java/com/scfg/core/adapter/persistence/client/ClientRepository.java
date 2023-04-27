package com.scfg.core.adapter.persistence.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientJpaEntity, Long> {

    Optional<ClientJpaEntity> findByNamesAndLastNameAndMothersLastName(String names, String lastnames, String motherLastNames);

    @Query(value = "SELECT cl " +
            "FROM ClientJpaEntity cl " +
            "LEFT JOIN FETCH cl.documentType doc " +
            "LEFT JOIN FETCH cl.currency cur " +
            "WHERE cl.documentNumber = :documentNumber",
            countQuery = "SELECT COUNT(cl.id) " +
                    "FROM ClientJpaEntity cl " +
                    "LEFT JOIN cl.documentType doc " +
                    "LEFT JOIN cl.currency cur " +
                    "WHERE cl.documentNumber = :documentNumber")
    Page<ClientJpaEntity> findByDocumentNumber(
            @Param("documentNumber") String documentNumber,
            Pageable pageable);

    @Modifying
    @Query("UPDATE ClientJpaEntity cl " +
            "SET cl.accumulatedAmountDhl = :accumulatedAmount " +
            "WHERE cl.documentNumber = :clientDocumentNumber")
    void updateAccumulatedAmountDhl(
            @Param("clientDocumentNumber") String clientDocumentNumber,
            @Param("accumulatedAmount") Double accumulatedAmount);


    @Modifying
    @Query("UPDATE ClientJpaEntity cl " +
            "SET cl.accumulatedAmountDhn = :accumulatedAmount " +
            "WHERE cl.documentNumber = :clientDocumentNumber")
    void updateAccumulatedAmountDhn(
            @Param("clientDocumentNumber") String clientDocumentNumber,
            @Param("accumulatedAmount") Double accumulatedAmount
    );
}
