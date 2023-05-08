package com.scfg.core.application.port.out;

import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;

import java.util.Date;
import java.util.List;

public interface PolicyPort {
    List<Policy> getAllPolicy();
    List<Policy> getAllGELPolicies(); //#ToDo Verificar para deprecar

    List<GELPolicyDTO> getAllActualGELPolicies(Long businessGroup);

    Policy saveOrUpdate(Policy o);

    Long getNumberPolicyMax(Long productId);
    Policy findById(Long policyId); //Todo Verificar y cambiar a PolicyItemId
    Policy findByPlanId(Long planId, Long policyId);

    List<Object[]> getAsciiVentas(Date startDate, Date toDate);

    Policy getByRequestId(Long requestId);
    Policy getByRequestIdOneData(Long requestId);

    List<Object> getAllSMVSSubscriptionReport(Date startDate, Date toDate, Integer statusRequest);

    PageableDTO findAllByPageAndPersonFilters(PersonDTO personDTO, Integer page, Integer size);

    Policy findByOperationNumber(String operationNumber);

    String getNextSequencyPolNumber(String productInitial);
    Policy findByPolicyId(Long policyId);
    Policy findByPolicyIdOrThrowExcepcion (Long policyId);
}
