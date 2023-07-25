package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PolicyUseCase;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.application.port.out.PolicyPort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService implements PolicyUseCase {
    private final PolicyPort policyPort;
    private final GeneralRequestPort generalRequestPort;
    private final PlanPort planPort;
    @Override
    public List<Policy> getAll() {
        return policyPort.getAllPolicy();
    }

    //toDo Verificar si se debe deprecar o no
    //#region Deprecated
    @Override
    public List<GELPolicyDTO> getAllGELPolicies() {
        List<GELPolicyDTO> gelPolicyDTOList = new ArrayList<>();
        List<Policy> policyList = policyPort.getAllGELPolicies();
        policyList.forEach(policy -> {
            Long planId = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId()).getPlanId();
            String planName = planPort.getPlanById(planId).getName();
            gelPolicyDTOList.add(GELPolicyDTO.builder()
                            .id(policy.getId())
                            .name(planName)
                            .build());
        });

        return gelPolicyDTOList;
    }
    //#endregion

    @Override
    public Policy save(Policy o) {
        return  policyPort.saveOrUpdate(o);
    }

    @Override
    public Policy update(Policy o) {
        return policyPort.saveOrUpdate(o);
    }

    @Override
    public Boolean delete(Policy o) {
        return null;
    }

    @Override
    public List<GELPolicyDTO> getAllActualGELPolicies(Long businessGroup) {
        return policyPort.getAllActualGELPolicies(businessGroup);
    }

    @Override
    public PageableDTO getAllByPageAndPersonFilters(Integer page, Integer size, PersonDTO personDTO) {
        if (personDTO.getIdentificationNumber().isEmpty()) {
            throw new OperationException("El campo cédula de identidad no puede estar vacio");
        }
        return this.policyPort.findAllByPageAndPersonFilters(personDTO, page, size);
    }
}
