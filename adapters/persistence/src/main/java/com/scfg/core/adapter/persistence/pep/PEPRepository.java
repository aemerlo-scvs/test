package com.scfg.core.adapter.persistence.pep;

import com.scfg.core.domain.common.Pep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PEPRepository extends JpaRepository<PEPJpaEntity, Long> {

    @Query("SELECT p " +
            "FROM PEPJpaEntity p " +
            "WHERE p.identificationNumber = :identificationNumber")
    PEPJpaEntity findByIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Query("SELECT new com.scfg.core.domain.common.Pep(p.id, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, p.name, p.lastName, p.motherLastName, \n" +
            "p.identificationNumber, p.issuancePlace, p.charge, p.countryCharge, p.pepType)" +
            "FROM PEPJpaEntity p " +
            "WHERE p.identificationNumber LIKE %:keyWord% OR p.name LIKE %:keyWord% OR p.lastName LIKE %:keyWord% \n" +
            "OR p.motherLastName LIKE %:keyWord% OR p.charge LIKE %:keyWord% OR p.pepType LIKE %:keyWord%")
    List<Pep> findByKeyWord(@Param("keyWord") String keyWord);

    //#region Dynamic Query

    default String getFindAllByFiltersBaseQuery() {
        return "SELECT p " +
                "FROM PEPJpaEntity p " +
                "WHERE";
    }

    default String getFindAllByKeyWordQuery(String keyWord) {
        return "SELECT p " +
                "FROM PEPJpaEntity p " +
                "WHERE p.identificationNumber LIKE '%" + keyWord + "%' OR " +
                "p.name LIKE '%" + keyWord + "%' OR p.lastName LIKE '%" + keyWord + "%' OR p.motherLastName LIKE '%" + keyWord + "%' OR " +
                "p.charge LIKE '%" + keyWord + "%' OR p.pepType LIKE '%" + keyWord + "%'";
    }

    //#endregion

}
