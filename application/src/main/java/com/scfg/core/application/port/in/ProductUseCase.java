package com.scfg.core.application.port.in;

import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.smvs.SMVSResponseDTO;

import java.util.List;

public interface ProductUseCase {

    List<Product> getAll();

    SMVSResponseDTO getPlansByAgreementCode(int agreementCode);
    List<ObjectDTO> getAllProductsByBranchId(long branchId);
}
