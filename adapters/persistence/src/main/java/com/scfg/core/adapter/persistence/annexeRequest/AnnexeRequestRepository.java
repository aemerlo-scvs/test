package com.scfg.core.adapter.persistence.annexeRequest;

import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.RequestAnnexeStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnexeRequestRepository extends JpaRepository<AnnexeRequestJpaEntity, Long> {

    @Query("SELECT r " +
            "FROM AnnexeRequestJpaEntity r " +
            "INNER JOIN PolicyJpaEntity p on r.policyId = p.id " +
            "WHERE p.id =:policyId " +
            "AND r.annexeTypeId =:annexeTypeId " +
            "AND r.statusIdc IN (:requuestAnnexeStatusList) " +
            "AND r.status =:status " +
            "AND p.status =:status " +
            "ORDER BY r.id desc ")
    List<AnnexeRequestJpaEntity> findAllByPolicyIdAndAnnexe(
            @Param("policyId")Long policyId,
            @Param("annexeTypeId") Long annexeTypeId,
            @Param("status") Integer status,
            @Param("requuestAnnexeStatusList") List<Integer> requuestAnnexeStatusList);

    default String getFindAllPageByFiltersSelectQuery() {
        return "SELECT new com.scfg.core.domain.dto.vin.ResponseAnnexeRequestDto(ra.id, np.identificationNumber, np.complement, \n" +
                "np.name, np.lastName, np.motherLastName, np.marriedLastName, ra.statusIdc, ra.createdAt, gr.id, ra.annexeTypeId, \n" +
                "p.id , p.numberPolicy, ra.requestDate )\n" +
                this.getFindAllPageByFiltersBaseQuery();
    }
    default String getFindAllPageByFiltersCountQuery() {
        return "SELECT COUNT (DISTINCT ra.id)\n" +
                this.getFindAllPageByFiltersBaseQuery();
    }
    default String getFindAllPageByFiltersBaseQuery() {
        return "FROM AnnexeRequestJpaEntity ra \n " +
                "INNER JOIN PolicyJpaEntity p ON p.id  = ra.policyId \n" +
                "INNER JOIN GeneralRequestJpaEntity gr ON p.generalRequestId = gr.id \n " +
                "INNER JOIN PersonJpaEntity pe ON pe.id = gr.personId \n" +
                "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = pe.id \n " +
                "WHERE ra.statusIdc <> " + RequestAnnexeStatusEnum.PENDING.getValue() +" \n "+
                "AND ra.status = " + PersistenceStatusEnum.CREATED_OR_UPDATED.getValue() + " ";
    }

    @Query("SELECT r " +
            "FROM AnnexeRequestJpaEntity r " +
            "WHERE r.status = :status")
    List<AnnexeRequestJpaEntity> findAll(@Param("status") Integer status);

    @Query(value = "SELECT p " +
            "FROM AnnexeRequestJpaEntity p " +
            "WHERE p.id = :requestAnnexeId AND p.status = :status")
    Optional<AnnexeRequestJpaEntity> findOptionalById(@Param("requestAnnexeId") Long requestAnnexeId, @Param("status") Integer status);

}
