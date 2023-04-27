package com.scfg.core.adapter.persistence.messageTosend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageToSendRepository extends JpaRepository<MessageToSendJpaEntity, Long> {

    @Query("SELECT m FROM MessageToSendJpaEntity m \n" +
            "WHERE m.referenceId = :referenceId AND m.referenceTableIdc = :referenceTableIdc AND \n" +
            "m.status = :status ORDER BY m.id DESC")
    List<MessageToSendJpaEntity> findByReferenceIdAndReferenceTableIdc(@Param("referenceId") Long referenceId,
                                                                      @Param("referenceTableIdc") Integer referenceTableIdc,
                                                                      @Param("status") Integer status);
}
