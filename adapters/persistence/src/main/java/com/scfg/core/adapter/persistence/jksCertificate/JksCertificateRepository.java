package com.scfg.core.adapter.persistence.jksCertificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface JksCertificateRepository extends JpaRepository<JksCertificateJpaEntity,Long> {

    List<JksCertificateJpaEntity> findAllByStatus(Integer status);

    Optional<JksCertificateJpaEntity> findByAbbreviationAndStatus(String abbreviation, Integer status);


    //#region Dynamic Query

    default String getFindAllByAbbreviationStatusQuery() {
        return "SELECT c \n" +
                "FROM JksCertificateJpaEntity c \n" +
                "WHERE c.abbreviation IN (:abbreviations) AND c.status = :status";
    }

    //#endregion
}
