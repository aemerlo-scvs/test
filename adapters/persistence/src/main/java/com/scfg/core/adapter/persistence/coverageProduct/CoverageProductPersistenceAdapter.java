package com.scfg.core.adapter.persistence.coverageProduct;

import com.scfg.core.application.port.out.CoverageProductPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.CoverageProduct;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CoverageProductPersistenceAdapter implements CoverageProductPort {

    private final CoverageProductRepository coverageProductRepository;

    @Override
    public List<CoverageProduct> getAll() {
        return mapToListDomain(coverageProductRepository.findAll());
    }

    private List<CoverageProduct> mapToListDomain (List<CoverageProductJpaEntity> coverageProductJpaEntities){
        List<CoverageProduct> coverageProducts = new ArrayList<>();
        coverageProductJpaEntities.forEach(e -> {
            coverageProducts.add(mapToDomain(e));
        });
        return coverageProducts;
    }

    private CoverageProduct mapToDomain(CoverageProductJpaEntity coverageProductJpaEntity) {
        CoverageProduct coverageProduct = CoverageProduct.builder()
                .id(coverageProductJpaEntity.getId())
                .coverageId(coverageProductJpaEntity.getCoverageId())
                .productId(coverageProductJpaEntity.getProductId())
                .createdAt(coverageProductJpaEntity.getCreatedAt())
                .lastModifiedAt(coverageProductJpaEntity.getLastModifiedAt())
                .build();

        return coverageProduct;
    }
}
