package com.scfg.core.application.port.in;

import com.scfg.core.domain.PolicyManager;
import com.scfg.core.domain.dto.PolicyDto;

import java.util.List;

public interface PolicyManagerBs {
    List<PolicyManager> getAllPolicyManager();
    PolicyManager getPolycyManagerByPolicyNumber(String number);
    //PolicyManager getProductoByNombre(String nombre);
//    boolean addPolicyManager(PolicyManager policyManager); //No borrar para simplificar la migracion cuando toque
    PolicyManager updateProdcuto(PolicyManager policyManager);
    boolean loadPolicyManagers(List<PolicyDto> dtoList);
//    Optional<PolicyManager> findById(String id); //No borrar para simplificar la migracion cuando toque
    List<PolicyDto>findByManager_Code(long cod);
}
