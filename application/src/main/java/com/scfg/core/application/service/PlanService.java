package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.application.port.out.CoveragePlanPort;
import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CoveragePlan;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements PlanUseCase {

    private final PlanPort planPort;
    private final CoveragePlanPort coveragePlanPort;

    @Override
    public List<ClfPlanDTO> getAllPlans() {
        return planPort.clfFindAll();
    }

    @Override
    public List<Plan> getAllPlansByBusinessGroupId(Integer businessGroupIdc) {
        return planPort.getPlanByFinancialGroup(businessGroupIdc);
    }

    @Override
    public Plan getPlanByAgreementCode(Integer agreementCode) {
        return planPort.findPlanByAgreementCode(agreementCode);
    }

    @Override
    public List<Plan> getAll() {
        return planPort.getList();
    }

    @Override
    public Plan getById(Long id) {
        return planPort.getPlanById(id);
    }

    @Override
    public PersistenceResponse save(Plan plan) {
        PersistenceResponse response = planPort.saveOrUpdate(plan);
        Plan aux = (Plan) response.getData();
        long idPlan = aux.getId();
        for (CoveragePlan coveragePlan : plan.getCoveragePlanList()) {
            coveragePlan.setPlanId(idPlan);
            coveragePlanPort.save(coveragePlan);
        }
        return response;
    }

    @Override
    public PersistenceResponse update(Plan plan) {
        PersistenceResponse response = planPort.saveOrUpdate(plan);
        Plan aux = (Plan) response.getData();
        List<CoveragePlan> listCoveragePlan = coveragePlanPort.getAllCoveragePlanByPlanId(aux.getId());
        Long[] newsIds = plan.getCoveragePlanList().stream().map(CoveragePlan::getId).toArray(Long[]::new);
        if (newsIds.length == 0) {
            listCoveragePlan.forEach(item -> {
                coveragePlanPort.delete(item.getId());
            });
        } else {
            listCoveragePlan.forEach(item -> {
                if (Arrays.asList(newsIds).contains(item.getId())) {
                    CoveragePlan coveragePlan = plan.getCoveragePlanList().stream().findFirst().filter(e -> e.getId() == item.getId()).get();
                    coveragePlanPort.update(coveragePlan);
                } else {
                    coveragePlanPort.delete(item.getId());
                }
            });
            plan.getCoveragePlanList().stream().filter(e -> e.getId() == 0).forEach(coveragePlanPort::save);
        }

        return response;
    }

    @Override
    public PersistenceResponse delete(Long id) {
        List<CoveragePlan> coveragePlanList = coveragePlanPort.getAllCoveragePlanByPlanId(id);
        coveragePlanList.forEach((item) -> {
            coveragePlanPort.delete(item.getId());
        });

        return planPort.delete(id);
    }

    @Override
    public List<Plan> getfilterParamenter(FilterParamenter paramenter) {
        return planPort.getfilterParamenters(paramenter);
    }

    @Override
    public List<Plan> findPlanByProductID(Long productId) {
        return planPort.getPlanByProductId(productId);
    }

    @Override
    public List<PlanInformation> findPlanByRequestId(List<Long> requestList) {
        return planPort.getPlanByRequestId(requestList);
    }
}
