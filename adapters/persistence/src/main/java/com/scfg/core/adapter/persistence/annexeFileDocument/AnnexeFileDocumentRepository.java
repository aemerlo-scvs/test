package com.scfg.core.adapter.persistence.annexeFileDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AnnexeFileDocumentRepository extends JpaRepository<AnnexeFileDocumentJpaEntity, Long> {

    @Query("SELECT afd " +
            "FROM AnnexeRequirementControlJpaEntity arc " +
            "INNER JOIN AnnexeFileDocumentJpaEntity afd on arc.fileDocumentId = afd.id " +
            "WHERE arc.requestAnnexeId =:requestAnnexeId " +
            "AND afd.documentTypeIdc =:annexeTypeIdc ")
    AnnexeFileDocumentJpaEntity findRequestAnnexeIdAndAnnexeTypeIdc(@Param("requestAnnexeId")Long requestAnnexeId,
                                                                    @Param("annexeTypeIdc")Integer annexeTypeIdc);

    @Query("SELECT afd " +
            "FROM AnnexeRequirementControlJpaEntity arc " +
            "INNER JOIN AnnexeFileDocumentJpaEntity afd on arc.fileDocumentId = afd.id " +
            "WHERE arc.requestAnnexeId =:requestAnnexeId " +
            "AND afd.documentTypeIdc =:annexeTypeIdc " +
            "AND arc.signed =:signed ")
    AnnexeFileDocumentJpaEntity findRequestAnnexeIdAndAnnexeTypeIdcAndSigned(@Param("requestAnnexeId")Long requestAnnexeId,
                                                                          @Param("annexeTypeIdc")Integer annexeTypeIdc,
                                                                          @Param("signed")Boolean signed);
}
