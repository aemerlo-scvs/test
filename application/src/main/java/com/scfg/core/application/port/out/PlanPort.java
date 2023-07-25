package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;

import java.util.List;

public interface PlanPort {

    List<Plan> getList();

    List<ClfPlanDTO> clfFindAll();
    List<ClfPlanDTO> clfFindAllByBusinessGroupIdc();

    Plan getPlanById(Long planId);

    List<Plan> getPlanByFinancialGroup(Integer financialGroupIdc);

    List<Plan> getPlanByProductId(Long productId);
    PersistenceResponse deleteByProductId(Long productId);
    List<PlanInformation> getPlanByRequestId(List<Long> requestList);

    Plan getPlanByAgreementCodePlandAndAgreementCodeProduct(Integer agreementCodePlan, Integer agreementCodeProduct);

    Plan findPlanByAgreementCode(Integer agreementCode);

    PersistenceResponse saveOrUpdate(Plan plan);

    PersistenceResponse delete(Long planId);


    List<Plan> getfilterParamenters(FilterParamenter paramenter);
}
