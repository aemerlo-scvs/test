package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.smvs.PlanDTO;

import java.util.List;

public interface ProductPort {

    List<Product> getProductList();

    List<PlanDTO> findAllPlansByAgreementCode(int agreementCode);

    Boolean existsProductByAgreementCode(int agreementCode);

    Product getProductById(Long id);

    Product findProductByPlanId(Long id);
    Product findProductByPolicyId(Long id);

    List<ObjectDTO> getAllProductsByBranchId(Long branchId);


    PersistenceResponse save(Product product, boolean returnEntity);
    PersistenceResponse update(Product product);
    PersistenceResponse delete(Long product);

    List<Product> getfilterParamenters(FilterParamenter paramenter);

    List<Product> getProductByBranchId(Long branchId);
}
