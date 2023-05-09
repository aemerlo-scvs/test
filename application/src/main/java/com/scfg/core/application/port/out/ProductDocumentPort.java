package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.ProductDocument;

import java.util.List;

public interface ProductDocumentPort {
    List<ProductDocument> getList();
    ProductDocument getProductDocumentById(Long id);
    PersistenceResponse save(ProductDocument productDocument);
    PersistenceResponse update(ProductDocument productDocument);
    PersistenceResponse delete(Long productDocumentId);

}
