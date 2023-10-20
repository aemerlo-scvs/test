package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.smvs.SMVSResponseDTO;

import java.util.List;

public interface ProductUseCase {

    List<Product> getAll();

    SMVSResponseDTO getPlansByAgreementCode(int agreementCode);
    List<Product> getAllProduct();
    PersistenceResponse registerProduct(Product product);
    PersistenceResponse updateProduct(Product product);
    PersistenceResponse deleteProduct(Long productId);
    List<Product> getfilterParamenter(FilterParamenter paramenter);
    List<Product> getAllProductWithBranchName(Long branchId);

    List<ObjectDTO> getAllProductsByBranchId(long branchId);
}
