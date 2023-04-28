package com.scfg.core.application.port.in;

import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;

import java.util.List;

public interface PolicyUseCase {
    List<Policy> getAll();
    List<GELPolicyDTO> getAllGELPolicies(); //#ToDo Verificar para deprecar o no
    Policy save(Policy o);
    Policy update(Policy o);
    Boolean delete(Policy o);
    List<GELPolicyDTO> getAllActualGELPolicies(Long businessGroupIdc);
    String getAllByPersonFilters(PersonDTO personDTO);
}
