package com.scfg.core.application.service.bulkLoadGel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BulkLoadGelRepository extends CrudRepository<BulkLoadGelJpaEntity, Long> {
    @Query(value = "SELECT TOP 1 bl.id \n" +
            "FROM BulkLoadGel bl", nativeQuery = true)
    Long findFirstId();
}
