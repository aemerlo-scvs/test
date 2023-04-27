package com.scfg.core.adapter.persistence.messageResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageResponseRepository extends JpaRepository<MessageResponseJpaEntity,Long> {

    @Query("SELECT m " +
            "FROM MessageResponseJpaEntity m \n" +
            "WHERE m.status = :status AND m.messageSentId = :messageSentId")
    MessageResponseJpaEntity findByMessageSentId(@Param("messageSentId") Long messageSentId, @Param("status") Integer status);

    @Query("SELECT m " +
            "FROM MessageResponseJpaEntity m \n" +
            "WHERE m.messageSentId IN (:messageSentIds) AND m.status = :status \n" +
            "ORDER BY m.id DESC")
    List<MessageResponseJpaEntity> findAllByMessageSentIds(@Param("messageSentIds") List<Long> messageSentIds, @Param("status") Integer status);

}
