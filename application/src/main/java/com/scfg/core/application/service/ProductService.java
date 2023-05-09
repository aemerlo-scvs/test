package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductUseCase;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.enums.SMVSResponseEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.smvs.PlanDTO;
import com.scfg.core.domain.smvs.SMVSResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    @Override
    public List<Product> getAll() {
        return productPort.getProductList();
    }

    @Override
    public SMVSResponseDTO getPlansByAgreementCode(int agreementCode) {
        Integer responseCode = SMVSResponseEnum.OK.getValue();
        String message = "Producto encontrado correctamente";

        Boolean existsProduct = productPort.existsProductByAgreementCode(agreementCode);
        if(!existsProduct){
            responseCode = SMVSResponseEnum.ERROR.getValue();
            message = "Producto con código de convenio: " + agreementCode + ", no encontrado";
            return setSMVSResponseDTO(responseCode, message, new ArrayList<>());
        }

        List<PlanDTO> data = productPort.findAllPlansByAgreementCode(agreementCode);
        if(data.size() <= 0){
            message = "Producto sin planes";
        }

        return setSMVSResponseDTO(responseCode, message, data);
    }

    @Override
    public List<Product> getAllProduct() {
        return productPort.getProductList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public PersistenceResponse registerProduct(Product product) {
        //Product product = coverageProductDto.getProduct()
        PersistenceResponse persistenceResponse = productPort.save(product, true);
//        Object data = persistenceResponse.getData();
//        Product product1 = (Product) data;
//        List<CoverageProduct> coverageProductPortList = coverageProductDto.getCoverageProducts();
//        List<CoverageProduct> coverageProductList = new ArrayList<>();
//        coverageProductPortList.forEach(x -> {
//            CoverageProduct product2 = new CoverageProduct();
//            product2.setProductId(product1.getId());
//            product2.setCoverageId(x.getCoverageId());
//            product2.setStatus(x.getStatus());
//            coverageProductList.add(product2);
//        });
//        coverageProductPort.saveAll(coverageProductList);
        return persistenceResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public PersistenceResponse updateProduct(Product product) {
        //Product product = coverageProductDto.getProduct();
        PersistenceResponse persistenceResponse = productPort.update(product);
//        coverageProductPort.saveAll(coverageProductDto.getCoverageProducts());
//        List<CoverageProduct> coverageProduct = coverageProductDto.getCoverageProducts();
//        List<CoverageProduct> coverageProductList = coverageProductPort.getAllCoverageProdcut();
//        coverageProductList = coverageProductList.stream().filter(s -> s.getProductId() == product.getId()).collect(Collectors.toList());
//        List<CoverageProduct> newList = new ArrayList<>();
//        for (CoverageProduct element : coverageProductList) {
//            for (CoverageProduct j : coverageProduct) {
//                if (element.getProductId() == j.getProductId() && element.getCoverageId() != j.getCoverageId()) {
//                    element.setStatus(0);
//                    element.setLastModifiedAt(new Date());
//                    coverageProductPort.update(element);
//                }
//            }

//        }
//        coverageProductPort.saveAll(coverageProductDto.getCoverageProducts());
        return persistenceResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public PersistenceResponse deleteProduct(Long productId) {
        PersistenceResponse persistenceResponse=productPort.delete(productId);
        //en caso de realizar la eliminacion logica del producto, deberia deberia darselo todo lo relacionado con el producto(plan, coverage,clause, anaxxe,etc.)
//        List<CoverageProduct> coverageProductList = coverageProductPort.getAllCoverageProdcut();
//        coverageProductList = coverageProductList.stream().filter(s -> s.getProductId() == productId).collect(Collectors.toList());
//        coverageProductList.forEach(x->{
//            x.setStatus(0);
//            x.setLastModifiedAt(new Date());
//            coverageProductPort.update(x);
//        });
        return persistenceResponse;
    }


    @Override
    public List<Product> getfilterParamenter(FilterParamenter parameters) {
        List<Product> list1 = productPort.getfilterParamenters(parameters);

        if (parameters.getName() != null && !parameters.getName().isEmpty()) {
            list1 = list1.stream().filter(re -> re.getName().contains(parameters.getName())).collect(Collectors.toList());
        }
        if (list1.size() > 0 && parameters.getDateto() != null && parameters.getDatefrom() != null) {
            list1 = list1.stream().filter(re -> re.getCreatedAt().after(parameters.getDatefrom()) && re.getCreatedAt().before(parameters.getDateto())).collect(Collectors.toList());
        }
        if (list1.size() > 0 && parameters.getStatus() != null) {
            list1 = list1.stream().filter(re -> re.getStatus() == parameters.getStatus()).collect(Collectors.toList());
        }
        if (list1.size() > 0 && parameters.getBranchId() != null) {
            list1 = list1.stream().filter(re -> re.getBranchId() == parameters.getBranchId()).collect(Collectors.toList());
        }
        return list1;
    }

    @Override
    public List<Product> getAllProductWithBranchName(Long productId) {
        return productPort.getProductByBranchId(productId);
    }
    @Override
    public List<ObjectDTO> getAllProductsByBranchId(long branchId){
        return productPort.getAllProductsByBranchId(branchId);
    }


    //#region Helpers
    SMVSResponseDTO setSMVSResponseDTO(Integer responseCode, String message, Object data) {
        return SMVSResponseDTO.builder()
                .codigo_respuesta(responseCode)
                .mensaje(message)
                .data(data)
                .build();
    }
    //#endregion
}
