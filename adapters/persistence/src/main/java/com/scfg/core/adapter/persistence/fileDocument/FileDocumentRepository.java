package com.scfg.core.adapter.persistence.fileDocument;

import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileDocumentRepository extends JpaRepository<FileDocumentJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO(fd.id," +
            "fd.typeDocument, fd.description, fd.content, fd.mimeType, pfd.isSigned ) " +
            "FROM GeneralRequestJpaEntity gr " +
            "inner join PolicyItemJpaEntity pi on pi.generalRequestId = gr.id " +
            "inner join PolicyFileDocumentJpaEntity pfd on pfd.policyItemId = pi.id " +
            "inner join FileDocumentJpaEntity fd on fd.id = pfd.fileDocumentId " +
            "where gr.id = :requestId and pfd.isSigned = 0 ")
    List<FileDocumentByRequestDTO> findAllDocumentsByRequestId(@Param("requestId") Long requestId);

    @Query("SELECT new com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO(fd.id," +
            "fd.typeDocument, fd.description, fd.content, fd.mimeType, pfd.isSigned ) " +
            "FROM GeneralRequestJpaEntity gr " +
            "inner join PolicyItemJpaEntity pi on pi.generalRequestId = gr.id " +
            "inner join PolicyFileDocumentJpaEntity pfd on pfd.policyItemId = pi.id " +
            "inner join FileDocumentJpaEntity fd on fd.id = pfd.fileDocumentId " +
            "where gr.id = :requestId and pfd.isSigned = 1 ")
    List<FileDocumentByRequestDTO> findAllSignedDocumentsByRequestId(@Param("requestId") Long requestId);

    //TODO verificar la firma del documento
    @Query("SELECT new com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO(fd.id," +
            "fd.typeDocument, fd.description, fd.content, fd.mimeType, pd.isSigned ) " +
            "FROM PolicyItemJpaEntity pit " +
            "INNER JOIN PolicyFileDocumentJpaEntity pd ON pit.id = pd.policyItemId " +
            "INNER JOIN FileDocumentJpaEntity fd ON fd.id = pd.fileDocumentId " +
            "INNER JOIN PolicyJpaEntity pl ON pl.id = pit.policyId " +
            "WHERE fd.typeDocument =:typeDocument " +
            "AND pd.isSigned = 0 " +
            "AND pit.id =:policyItemId AND pl.policyStatusIdc = :policyStatusIdc\n" +
            "ORDER BY fd.id DESC")
    List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(
            @Param("policyItemId") Long policyItemId,
            @Param("typeDocument") Integer  typeDocument,
            @Param("policyStatusIdc") Integer  policyStatusIdc);



    @Query("SELECT fd from FileDocumentJpaEntity fd " +
            "join PolicyFileDocumentJpaEntity pfd on pfd.fileDocumentId = fd.id " +
            "join PolicyItemJpaEntity pi on pi.id = pfd.policyItemId " +
            "where pi.id = :policyItemId and fd.status= :status " +
            "and pfd.status= :status " +
            "and pi.status= :status")
    List<FileDocumentJpaEntity> getFileDocumentsByPolicyItemId(
            @Param("policyItemId") Long policyItemId,
            @Param("status") Integer  status);

}
