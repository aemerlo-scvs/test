package com.scfg.core.adapter.persistence.productDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDocumentRepository  extends JpaRepository<ProductDocumentJpaEntity, Long>  {
    @Query("SELECT pd " +
            "FROM ProductDocumentJpaEntity pd " +
            "WHERE pd.id = :id AND pd.status = :status")
    ProductDocumentJpaEntity customFindById(@Param("id") long productDocumentId, @Param("status") Integer status);

}
