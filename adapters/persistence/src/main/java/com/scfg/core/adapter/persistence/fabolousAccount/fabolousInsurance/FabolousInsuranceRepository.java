package com.scfg.core.adapter.persistence.fabolousAccount.fabolousInsurance;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FabolousInsuranceRepository extends JpaRepository<FabolousInsuranceJpaEntity, Long> {
    @Query("SELECT B FROM FabolousInsuranceJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousInsuranceJpaEntity> getAll();

    @Query("SELECT B " +
            "FROM FabolousInsuranceJpaEntity B " +
            "LEFT JOIN FabolousUploadReportJpaEntity C ON C.id = B.fabolousUploadReportId " +
            "JOIN FETCH UserJpaEntity U ON U.id = C.user.id " +
            "WHERE B.fabolousUploadReportId = :fabolousUploadReportId")
    List<FabolousInsuranceJpaEntity> findAllByUploadReportId(@Param("fabolousUploadReportId") long fabolousUploadReportId);

//    List<FabolousInsuranceJpaEntity> findAllByFabolousUploadReportId(long fabolousUploadReportId);

    @Modifying
    @Query("update FabolousInsuranceJpaEntity u set u.status = :status where u.fabolousUploadReportId = :fabolousUploadReportId")
    void deleteInsuranceByReportId(@Param("fabolousUploadReportId") long fabolousUploadReportId, @Param("status") int status);
}
