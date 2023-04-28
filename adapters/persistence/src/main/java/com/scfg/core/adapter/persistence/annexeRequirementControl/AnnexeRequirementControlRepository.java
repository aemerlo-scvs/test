package com.scfg.core.adapter.persistence.annexeRequirementControl;

import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnexeRequirementControlRepository extends JpaRepository<AnnexeRequirementControlJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.dto.vin.AnnexeRequirementDto(a.id, a.requestAnnexeId, a.description, " +
            " a.requestDate, a.receptionDate, a.comment, a.requirementId, fd.id, fd.description, fd.documentTypeIdc, fd.content, fd.mimeType, a.signed ) " +
            "FROM AnnexeRequirementControlJpaEntity a " +
            "LEFT JOIN AnnexeFileDocumentJpaEntity fd on fd.id = a.fileDocumentId " +
            "WHERE a.requestAnnexeId =:requestAnnexeId " +
            "AND a.status =:status ")
    List<AnnexeRequirementDto> finAllByRequestAnnexeIdAndTypeId(@Param("requestAnnexeId") Long requestAnnexeId,
                                                                @Param("status") Integer status);

    @Query(value = "SELECT a FROM AnnexeRequirementControlJpaEntity a  " +
            "WHERE a.id = :annexeRequirementControlId AND a.status = :status")
    Optional<AnnexeRequirementControlJpaEntity> getOptionalById(
            @Param("annexeRequirementControlId") Long annexeRequirementControlId,
            @Param("status") Integer status);


    @Query("SELECT a " +
            "FROM AnnexeRequirementControlJpaEntity a " +
            "WHERE a.requestAnnexeId =:requestAnnexeId " +
            "AND a.fileDocumentId =:fileDocumentId " +
            "AND a.status =:status ")
    AnnexeRequirementControlJpaEntity findByRequestAnnexeIdAndFileDocumentId(@Param("requestAnnexeId") Long requestAnnexeId,
                                                                             @Param("fileDocumentId") Long fileDocumentId,
                                                                             @Param("status") Integer status);


}
