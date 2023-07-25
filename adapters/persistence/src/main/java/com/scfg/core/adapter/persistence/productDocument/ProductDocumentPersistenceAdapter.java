package com.scfg.core.adapter.persistence.productDocument;

import com.scfg.core.application.port.out.ProductDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProductDocumentPersistenceAdapter implements ProductDocumentPort {
    private final ProductDocumentRepository productDocumentRepository;

    @Override
    public List<ProductDocument> getList() {
        Object list = productDocumentRepository.findAll();
        return  (List<ProductDocument>) list;
    }

    @Override
    public ProductDocument getProductDocumentById(Long id) {
        ProductDocumentJpaEntity productDocumentJpaEntity = productDocumentRepository.customFindById(id, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(productDocumentJpaEntity);
    }

    @Override
    public PersistenceResponse save(ProductDocument productDocument) {
        ProductDocumentJpaEntity productDocumentJpaEntity = mapToJpaEntity(productDocument);
        productDocumentJpaEntity = productDocumentRepository.save(productDocumentJpaEntity);
        productDocument =  mapToDomain(productDocumentJpaEntity);
        return new PersistenceResponse(
                ProductDocumentPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                productDocument
        );
    }

    @Override
    public PersistenceResponse update(ProductDocument productDocument) {
        ProductDocumentJpaEntity productDocumentJpaEntity = mapToJpaEntity(productDocument);
        productDocumentJpaEntity = productDocumentRepository.save(productDocumentJpaEntity);
        productDocument =  mapToDomain(productDocumentJpaEntity);
        return new PersistenceResponse(
                ProductDocumentPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                productDocument
        );
    }

    @Override
    public PersistenceResponse delete(Long productDocumentId){
    Optional<ProductDocumentJpaEntity> result = productDocumentRepository.findById(productDocumentId);
        ProductDocumentJpaEntity productDocumentJpaEntity = result.isPresent() ? result.get() : null;
        productDocumentJpaEntity.setStatus(0);
        productDocumentJpaEntity.setLastModifiedAt(new Date());
        productDocumentJpaEntity = productDocumentRepository.save(productDocumentJpaEntity);
        return new PersistenceResponse(
                ProductDocumentPersistenceAdapter .class.getSimpleName(),
    ActionRequestEnum.DELETE,
                productDocumentJpaEntity
        );
    }

    private ProductDocumentJpaEntity mapToJpaEntity(ProductDocument productDocument) {
        return new ModelMapperConfig().getStrictModelMapper().map(productDocument, ProductDocumentJpaEntity.class);
    }
    public static ProductDocument mapToDomain(ProductDocumentJpaEntity productDocumentJpaEntity) {
        return new ModelMapper().map(productDocumentJpaEntity, ProductDocument.class);
    }
}
