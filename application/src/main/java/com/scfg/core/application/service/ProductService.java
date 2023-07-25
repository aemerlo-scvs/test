package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductUseCase;
import com.scfg.core.application.port.out.ClausePort;
import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.application.port.out.PlanPort;
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
    private final ClausePort clausePort;
    private final PlanPort planPort;
    private final CoveragePort coveragePort;

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
        PersistenceResponse persistenceResponse = productPort.save(product, true);
        return persistenceResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public PersistenceResponse updateProduct(Product product) {
        return  productPort.update(product);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public PersistenceResponse deleteProduct(Long productId) {
        PersistenceResponse persistenceResponse=productPort.delete(productId);
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
