package com.scfg.core.adapter.persistence.fabolousAccount.fabolousUploadReport;

import com.scfg.core.domain.dto.fabolous.FabolousUploadDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FabolousUploadReportRepository extends JpaRepository<FabolousUploadReportJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.dto.fabolous.FabolousUploadDTO( " +
            "U.id, U.reportDateUpload, usr.userName, rt.description, " +
            "p.description, m.description, y.description, U.createdAt, U.totalUpload) " +
            "FROM FabolousUploadReportJpaEntity U " +
            "JOIN U.user usr " +
            "JOIN ClassifierJpaEntity m " +
            "ON m.id = U.monthIdc " +
            "JOIN ClassifierJpaEntity y " +
            "ON y.id = U.yearIdc " +
            "JOIN ClassifierJpaEntity p " +
            "ON p.id = U.policyIdc " +
            "JOIN ClassifierJpaEntity rt " +
            "ON rt.id = U.reportTypeIdc")
    List<FabolousUploadDTO> getAllUploads();

    default String getClientInFbs(String findSearch) {
        String val = "drop table if exists #tempFbsInsure\n" +
                "\n" +
                "select fabolousClientId, min(id) as idFirst, min(createdAt) as dateMin, min(startDate) as dateStart,min(finishDate) as dateFinish\n" +
                "into #tempFbsInsure\n" +
                "from FabolousInsurance\n" +
                "group by fabolousClientId\n" +
                "\n" +
                "select i.fullName, i.identification, i.extension, d.fullName as beneficiary, d.relationshipIdc, d.percentage, l.dateStart, l.dateFinish\n" +
                "from FabolousClient i\n" +
                "join #tempFbsInsure l on l.fabolousClientId = i.id\n" +
                "join FabolousBeneficiary d on d.fabolousInsurancetId = l.idFirst\n" +
                findSearch +
                "order by l.dateStart asc";
        return val;
    }

    default String getClientCountInFbs(String findSearch) {
        String val = "drop table if exists #tempFbsInsure\n" +
                "\n" +
                "select fabolousClientId, min(id) as idFirst, min(createdAt) as dateMin, min(startDate) as dateStart,min(finishDate) as dateFinish\n" +
                "into #tempFbsInsure\n" +
                "from FabolousInsurance\n" +
                "group by fabolousClientId\n" +
                "\n" +
                "select count(i.fullName)\n" +
                "from FabolousClient i\n" +
                "join #tempFbsInsure l on l.fabolousClientId = i.id\n" +
                "join FabolousBeneficiary d on d.fabolousInsurancetId = l.idFirst\n" +
                findSearch;

        return val;
    }
}
