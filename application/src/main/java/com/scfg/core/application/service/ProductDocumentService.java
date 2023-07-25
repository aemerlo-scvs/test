package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductDocumentUseCase;
import com.scfg.core.application.port.out.ProductDocumentPort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDocumentService implements ProductDocumentUseCase {

    private final ProductDocumentPort productDocumentPort;

    @Override
    public List<ProductDocument> getAll() {
        return productDocumentPort.getList();
    }

    @Override
    public ProductDocument getById(Long id) {
        return productDocumentPort.getProductDocumentById(id);
    }

    @Override
    public PersistenceResponse save(ProductDocument productDocument) {
        PersistenceResponse persistenceResponse = productDocumentPort.save(productDocument);
        return persistenceResponse;
    }

    @Override
    public PersistenceResponse update(ProductDocument productDocument) {
        PersistenceResponse persistenceResponse = productDocumentPort.update(productDocument);
        return persistenceResponse;

    }

    @Override
    public PersistenceResponse delete(Long id) {
        PersistenceResponse persistenceResponse = productDocumentPort.delete(id);
        return persistenceResponse;
    }
}
