package com.scfg.core.adapter.persistence.plan;

import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements PlanPort {

    private final PlanRepository planRepository;
    private final EntityManager em;

    @Override
    public List<Plan> getList() {
        List<PlanJpaEntity> planJpaEntityList = planRepository.findAll();
        return planJpaEntityList.size() > 0 ? ObjectMapperUtils.mapAll(planJpaEntityList, Plan.class) : new ArrayList<>();
    }

    @Override
    public List<ClfPlanDTO> clfFindAll() {
        return planRepository.clfFindAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public List<ClfPlanDTO> clfFindAllByBusinessGroupIdc() {
        return null;
    }

    @Override
    public Plan getPlanById(Long id) {
        PlanJpaEntity planJpaEntity = planRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Plan: " + id + " No encontrado"));
        return ObjectMapperUtils.map(planJpaEntity, Plan.class);
    }

    @Override
    public List<Plan> getPlanByFinancialGroup(Integer financialGroupIdc) {
        List<PlanJpaEntity> planJpaEntityList = planRepository.findAllByFinancialGroupId(financialGroupIdc,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return planJpaEntityList.size() > 0 ? ObjectMapperUtils.mapAll(planJpaEntityList, Plan.class) : new ArrayList<>();
    }

    @Override
    public List<Plan> getPlanByProductId(Long productId) {
        List<PlanJpaEntity> planJpaEntities = planRepository.findAllByProductId(productId);
        return planJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(planJpaEntities, Plan.class) : new ArrayList<>();
    }

    @Override
    public PersistenceResponse deleteByProductId(Long productId) {
        planRepository.deleteByProductId(productId);
        return new PersistenceResponse(
                Plan.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );    }

    @Override
    public List<PlanInformation> getPlanByRequestId(List<Long> requestList) {
        return planRepository.findAllByRequestId(requestList, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public Plan getPlanByAgreementCodePlandAndAgreementCodeProduct(Integer agreementCodePlan, Integer agreementCodeProduct) {
        PlanJpaEntity planJpaEntity = planRepository.findByAgreementCodePlanAndAgreementCodeProduct(agreementCodePlan,
                agreementCodeProduct, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return ObjectMapperUtils.map(planJpaEntity, Plan.class);
    }

    @Override
    public Plan findPlanByAgreementCode(Integer agreementCode) {
        PlanJpaEntity planJpaEntity = planRepository.findByAgreementCode(agreementCode,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return new ModelMapper().map(planJpaEntity, Plan.class);
    }

    @Override
    public PersistenceResponse saveOrUpdate(Plan plan) {
        PlanJpaEntity planJpaEntity = ObjectMapperUtils.map(plan, PlanJpaEntity.class);
        planJpaEntity = planRepository.save(planJpaEntity);

        return new PersistenceResponse<>(PlanPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                ObjectMapperUtils.map(planJpaEntity, Plan.class));
    }


    @Override
    public PersistenceResponse delete(Long planId) {
        PlanJpaEntity planJpaEntity = planRepository.findById(planId).get();
        planJpaEntity.setStatus(0);
        planJpaEntity.setLastModifiedAt(new Date());
        planJpaEntity = planRepository.save(planJpaEntity);
        return new PersistenceResponse(
                PlanPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                ObjectMapperUtils.map(planJpaEntity, Plan.class));
    }

    @Override
    public List<Plan> getfilterParamenters(FilterParamenter paramenter) {
        return null;
    }

    @Override
    public Object getPlanVirh(String apsCode) {
       String query ="select pl.id, pl.name, pl.description, pr.name as productName, " +
                "pl.totalPremium as price, pl.bfsAgreementCode as orderParticularCondition, " +
                "(select * from (select cv.commercialName as name, cv.[order], cvp.insuredCapital as amount, cv.coverageTypeIdc from [Plan] pla" +
                " join Product pr on pr.id = pla.productId and pr.status = 1 " +
                "join CoveragePlan cvp on cvp.planId = pla.id and cvp.status = 1 " +
                "join Coverage cv on cv.id = cvp.coverageId and cv.status = 1 " +
                "where pr.apsCode = '"+ apsCode + "' and cvp.planId = pl.id and pla.status = 1) as x" +
                " for JSON PATH) as 'coverageList' " +
                "from [Plan] pl " +
                "join Product pr on pr.id = pl.productId and pr.status = 1" +
                "where pl.status = 1  and pr.apsCode = '" + apsCode +
                "' for JSON PATH";
       List<Object> obj = em.createNativeQuery(query).getResultList();
        em.close();
        String aux = "";
        for (Object o: obj) {
            aux += o.toString();
        }
        return aux;
    }


}
