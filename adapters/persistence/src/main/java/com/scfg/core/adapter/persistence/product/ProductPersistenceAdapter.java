package com.scfg.core.adapter.persistence.product;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.smvs.PlanDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private ModelMapper modelMapper;

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
    public PersistenceResponse save(Product product, boolean returnEntity) {
        ProductJpaEntity productJpaEntity = mapToJpaEntity(product);
        productJpaEntity=productRepository.save(productJpaEntity);
        product=mapToDomain(productJpaEntity);
        return new PersistenceResponse(
                ProductPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                product
        );
    }

    @Override
    public PersistenceResponse update(Product product) {
        ProductJpaEntity productJpaEntity = mapToJpaEntity(product);
        productJpaEntity = productRepository.save(productJpaEntity);
        product = mapToDomain(productJpaEntity);
        return new PersistenceResponse(
                ProductPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                product
        );
    }

    @Override
    public PersistenceResponse delete(Long product) {
        Optional<ProductJpaEntity> result = productRepository.findById(product);
        ProductJpaEntity productJpaEntity = result.isPresent() ? result.get() : null;
        productJpaEntity.setStatus(0);
        productJpaEntity.setLastModifiedAt(new Date());
        productJpaEntity = productRepository.save(productJpaEntity);
        return new PersistenceResponse(
                ProductPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                productJpaEntity
        );

    }

    @Override
    public List<Product> getfilterParamenters(FilterParamenter paramenter) {
        List<ProductJpaEntity> list = productRepository.findAll();
        List<Product> list2 = new ArrayList<>();
        list.forEach(p -> {
            list2.add(mapToDomain(p));
        });

        return list2;
    }

    @Override
    public List<Product> getProductByBranchId(Long branchId) {
        try {
            List <ProductJpaEntity> product=productRepository.getProductByBranchId(branchId);
            List <Product> productList=new ArrayList<>();
            product.forEach(p -> {
                productList.add(mapToDomain(p));
            });
            return productList;
        } catch (Exception e){
            System.out.println("Error al obtener los datos: " + e.getMessage());
            return null;
        }
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
    private ProductJpaEntity mapToJpaEntity(Product product) {
        return new ModelMapperConfig().getStrictModelMapper().map(product, ProductJpaEntity.class);
    }
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
