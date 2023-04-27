package com.scfg.core.adapter.persistence.messageSent;

import com.scfg.core.adapter.persistence.messageTosend.MessageToSendJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageSentRepository extends JpaRepository<MessageSentJpaEntity, Long> {
    @Query("SELECT m FROM MessageSentJpaEntity m \n" +
            "WHERE m.referenceId = :referenceId AND m.referenceTableIdc = :referenceTableIdc AND \n" +
            "m.status = :status ORDER BY m.id DESC ")
    List<MessageSentJpaEntity> findByReferenceIdAndReferenceTableIdc(@Param("referenceId") Long referenceId,
                                                                    @Param("referenceTableIdc") Integer referenceTableIdc,
                                                                    @Param("status") Integer status);
}
