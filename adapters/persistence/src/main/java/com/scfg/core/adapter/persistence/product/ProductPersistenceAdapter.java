package com.scfg.core.adapter.persistence.product;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.smvs.PlanDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPort {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getProductList() {
        Object list = productRepository.findAll();
        return (List<Product>) list;
    }

    @Override
    public List<PlanDTO> findAllPlansByAgreementCode(int agreementCode) {
        List<PlanJpaEntity> listAux = productRepository.findAllPlansByAgreementCode(agreementCode, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<PlanDTO> list = new ArrayList<>();
        listAux.stream().forEach(item -> {
            list.add(mapToDomainPlanDTO(item));
        });
        return list;
    }

    @Override
    public Boolean existsProductByAgreementCode(int agreementCode) {
        ProductJpaEntity productJpaEntity = productRepository.findByAgreementCode(agreementCode);
        return productJpaEntity != null;
    }

    @Override
    public Product getProductById(Long id) {
        ProductJpaEntity productJpaEntity = productRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Producto: " + id + " No encontrado"));
        return mapToDomain(productJpaEntity);
    }

    @Override
    public Product findProductByPlanId(Long id) {
        ProductJpaEntity productJpaEntity = productRepository.findProductByPlanId(id);
        return mapToDomain(productJpaEntity);
    }

    @Override
    public Product findProductByPolicyId(Long id) {
        List<ProductJpaEntity> productJpaEntityList = productRepository.findAllProductsByPolicyId(id, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return productJpaEntityList.size() > 0 ? mapToDomain(productJpaEntityList.get(0)) : null;
    }

    @Override
    public List<ObjectDTO> getAllProductsByBranchId(Long branchId) {
        try {
            return productRepository.getAllProductsByBranchId(branchId);
        } catch (Exception e){
            System.out.println("Error al obtener los datos: " + e.getMessage());
            return null;
        }
    }


    //#region Mappers

    public static Product mapToDomain(ProductJpaEntity productJpaEntity) {

        return new ModelMapper().map(productJpaEntity, Product.class);
    }

    public static PlanDTO mapToDomainPlanDTO(PlanJpaEntity planJpaEntity) {

        PlanDTO planDTO = PlanDTO.builder()
                .id(planJpaEntity.getId())
                .descripcion(planJpaEntity.getName())
                .tipo_moneda(planJpaEntity.getCurrencyTypeIdc())
                .monto(planJpaEntity.getTotalPremium())
                .build();

        return planDTO;
    }
    //#endregion
}
