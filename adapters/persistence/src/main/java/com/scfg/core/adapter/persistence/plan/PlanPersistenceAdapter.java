package com.scfg.core.adapter.persistence.plan;

import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements PlanPort {

    private final PlanRepository planRepository;

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
        List<PlanJpaEntity> planJpaEntityList = planRepository.findAllByFinancialGroupId(financialGroupIdc, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
//        List<Plan> list = new ArrayList<>();
//        planJpaEntityList.forEach(planJpaEntity -> {
//            list.add(mapToDomain(planJpaEntity));
//        });
        return planJpaEntityList.size() > 0 ? ObjectMapperUtils.mapAll(planJpaEntityList, Plan.class) : new ArrayList<>();
    }

    @Override
    public List<Plan> getPlanByProductId(Long productId) {
        List<PlanJpaEntity> planJpaEntities = planRepository.findAllByProductId(productId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
//        List<Plan> list = new ArrayList<>();
//        planJpaEntities.forEach(planJpaEntity -> {
//            list.add(mapToDomain(planJpaEntity));
//        });
        return planJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(planJpaEntities, Plan.class) : new ArrayList<>();
    }

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
    public PersistenceResponse save(Plan plan, boolean returnEntity) {
        PlanJpaEntity planJpaEntity = ObjectMapperUtils.map(plan, PlanJpaEntity.class);
        planJpaEntity = planRepository.save(planJpaEntity);

        return new PersistenceResponse<>(PlanPersistenceAdapter.class.getSimpleName(), ActionRequestEnum.CREATE, ObjectMapperUtils.map(planJpaEntity, Plan.class));
    }

    @Override
    public PersistenceResponse update(Plan plan) {
        PlanJpaEntity planJpaEntity = ObjectMapperUtils.map(plan, PlanJpaEntity.class);
        planJpaEntity = planRepository.save(planJpaEntity);

        return new PersistenceResponse<>(PlanPersistenceAdapter.class.getSimpleName(), ActionRequestEnum.CREATE, ObjectMapperUtils.map(planJpaEntity, Plan.class));
    }

    @Override
    public PersistenceResponse delete(Long planId) {
        PlanJpaEntity planJpaEntity = planRepository.findById(planId).get();
        planJpaEntity.setStatus(0);
        planJpaEntity.setLastModifiedAt(new Date());
        planJpaEntity = planRepository.save(planJpaEntity);
        return new PersistenceResponse(PlanPersistenceAdapter.class.getSimpleName(), ActionRequestEnum.DELETE, ObjectMapperUtils.map(planJpaEntity, Plan.class));
    }

    @Override
    public List<Plan> getfilterParamenters(FilterParamenter paramenter) {
        return null;
    }

    //#region Mappers

//    private Plan mapToDomain(PlanJpaEntity planJpaEntity) {
//        Plan plan = Plan.builder()
//                .id(planJpaEntity.getId())
//                .name(planJpaEntity.getName())
//                .description(planJpaEntity.getDescription())
//                .totalPremium(planJpaEntity.getTotalPremium())
//                .rate(planJpaEntity.getRate())
//                .totalInsuredCapital(planJpaEntity.getTotalInsuredCapital())
//                .applyDiscount(planJpaEntity.getApplyDiscount())
//                .creditPremiumSurcharge(planJpaEntity.getCreditPremiumSurcharge())
//                .currencyTypeIdc(planJpaEntity.getCurrencyTypeIdc())
//                .createdAt(planJpaEntity.getCreatedAt())
//                .lastModifiedAt(planJpaEntity.getLastModifiedAt())
//                .productId(planJpaEntity.getProductId())
//                .agreementCode(planJpaEntity.getAgreementCode())
//                .build();
//
//        return plan;
//    }

    //#endregion


}
