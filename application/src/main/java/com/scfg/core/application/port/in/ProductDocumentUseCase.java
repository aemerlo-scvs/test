package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.ProductDocument;

import java.util.List;

public interface ProductDocumentUseCase {
    List<ProductDocument> getAll();

    ProductDocument getById(Long id);

    PersistenceResponse save(ProductDocument productDocument);

    PersistenceResponse update(ProductDocument productDocument);

    PersistenceResponse delete(Long id);
}
