package com.scfg.core.adapter.persistence.policy;

import com.scfg.core.application.port.out.PolicyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.RequestPolicyDetailDto;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements PolicyPort {
    private final PolicyRepository policyRepository;

    @PersistenceContext
    private EntityManager em;

    private static String GET_REQUEST_ALL_BY_FILTER = "exec proc_repsmvs_solicitudesporestado :startDate,:toDate,:statusRequest";

    @Override
    public List<Policy> getAllPolicy() {
        List<Policy> list = new ArrayList<>();
        List<PolicyJpaEntity> policyRepositoryAll = policyRepository.findAll();
        policyRepositoryAll.forEach(policyJpaEntity -> {
            list.add(mapToDomain(policyJpaEntity));
        });
        return list;
    }

    @Override
    public List<Policy> getAllGELPolicies() {
        List<PolicyJpaEntity> list = policyRepository.getAllGELPolicies(ClassifierTypeEnum.businessGroups.getReferenceId());
        return list.stream().map(x -> new ModelMapper().map(x, Policy.class)).collect(Collectors.toList());
    }

    @Override
    public List<GELPolicyDTO> getAllActualGELPolicies(Long businessGroup) {
        List<Object[]> list = policyRepository.getAllGELPolicies(businessGroup,
                ClassifierTypeEnum.businessGroups.getReferenceId(),
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<GELPolicyDTO> policyDTOList = new ArrayList<>();
        for (Object object[] : list) {
            GELPolicyDTO gelPolicyDTO = GELPolicyDTO.builder()
                    .id(((BigInteger) object[0]).longValue())
                    .name(object[1].toString())
                    .planId(((BigInteger) object[2]).longValue())
                    .build();
            policyDTOList.add(gelPolicyDTO);
        }
        return policyDTOList;
    }

    @Override
    public Policy saveOrUpdate(Policy o) {
        PolicyJpaEntity policyJpaEntity = mapToJpaEntity(o);
        try {
            policyJpaEntity = policyRepository.save(policyJpaEntity);
        } catch (Exception ex) {

        }
        Policy object = mapToDomain(policyJpaEntity);
        return object;
    }

    @Override
    public Long getNumberPolicyMax(Long productId) {
        Optional<Object> results = policyRepository.findTopByNumberPolicy(productId);
        return results.isPresent() ? new Long(results.get().toString()) : 0L;
    }

    @Override
    public List<Object[]> getAsciiVentas(Date startDate, Date toDate) {
        List<Object[]> asciiVentas=policyRepository.getAsciiVentas(startDate, toDate);

        return asciiVentas;
    }

    @Override
    public Policy getByRequestId(Long requestId) {
        List<PolicyJpaEntity> policyJpaEntity = policyRepository.findByGeneralRequestId(requestId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return new ModelMapper().map(policyJpaEntity.get(0), Policy.class);
    }

    @Override
    public Policy getByRequestIdOneData(Long requestId) {
        List<PolicyJpaEntity> policyJpaEntity = policyRepository.findByGeneralRequestIdReturnOneData(requestId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return new ModelMapper().map(policyJpaEntity.get(0), Policy.class);
    }

    @Override
    public Policy findById(Long policyItemId) {
        PolicyJpaEntity policyJpaEntity = policyRepository.findByIdByPolicyItemId(policyItemId);
        Policy policy = mapToDomain(policyJpaEntity);
        return policy;
    }

    @Override
    public Policy findByPlanId(Long planId, Long policyId) {
        PolicyJpaEntity policyJpaEntity = policyRepository.findByIdByPlanId(planId, policyId);
        return mapToDomain(policyJpaEntity);
    }

    @Override
    public List<Object> getAllSMVSSubscriptionReport(Date startDate, Date toDate, Integer statusRequest) {
        Query query = em.createNativeQuery(GET_REQUEST_ALL_BY_FILTER);
        query.setParameter("startDate", startDate);
        query.setParameter("toDate", toDate);
        query.setParameter("statusRequest", statusRequest);

        List<Object> list = query.getResultList();
        em.close();

        return list;
    }

    @Override
    public PageableDTO findAllByPageAndPersonFilters(PersonDTO personDTO, Integer page, Integer size) {
        String filters = this.getFindPolicyFilters(personDTO);

        int initRange = HelpersMethods.getPageInitRange(page, size);

        String query = this.policyRepository.getFindAllByFiltersSelectQuery(
                                            PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()) + filters;

        String countQuery = this.policyRepository.getFindAllByFiltersCountQuery(
                                            PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()) + filters;

        List<RequestPolicyDetailDto> requestPolicyDetailDTOList = em.createQuery(query)
                                                                    .setFirstResult(initRange)
                                                                    .setMaxResults(size).getResultList();
        em.close();

        Long count = (Long) em.createQuery(countQuery).getSingleResult();
        em.close();

        return PageableDTO.builder()
                .content(requestPolicyDetailDTOList)
                .totalElements(count.intValue())
                .build();
    }

    @Override
    public Policy findByOperationNumber(String operationNumber) {
       PolicyJpaEntity policyJpaEntity = this.policyRepository.findByOperationNumber(
               operationNumber,
               AgreementCodePlanFBS.VIN_CODE.getValue(),
               PolicyStatusEnum.ACTIVE.getValue(),
               RequestStatusEnum.CANCELLED.getValue(),
               PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
               .orElseThrow(() -> new OperationException("No se pudo realizar la operacion, el número de operacion no tiene una poliza"));
       return mapToDomain(policyJpaEntity);
    }

    @Override
    public String getNextSequencyPolNumber(String productInitial) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("proc_get_next_sequency_product")
                .registerStoredProcedureParameter("productInitial", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("polNumber", String.class, ParameterMode.OUT)
                .setParameter("productInitial",productInitial);
        query.execute();
        String result = (String) query.getOutputParameterValue("polNumber");
        em.close();
        return result;
    }

    @Override
    public Policy findByPolicyId(Long policyId) {
        PolicyJpaEntity policyJpaEntity = policyRepository.findByPolicyId(policyId,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if (policyJpaEntity == null) {
            return null;
        }
        return new ModelMapper().map(policyJpaEntity,Policy.class);
    }

    private Policy mapToDomain(PolicyJpaEntity policyJpaEntity) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(policyJpaEntity, Policy.class);
    }

    private PolicyJpaEntity mapToJpaEntity(Policy policy) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(policy, PolicyJpaEntity.class);
    }

    public String getFindPolicyFilters(PersonDTO searchRequestDTO) {
        String filters = "";
        List<String> filterList = new ArrayList<>();
        if (!searchRequestDTO.getNames().isEmpty()) {
            filterList.add("np.name LIKE " + "'%" + searchRequestDTO.getNames().toUpperCase().trim() + "%'");
        }
        if (!searchRequestDTO.getLastname().isEmpty()) {
            filterList.add("np.lastName LIKE " + "'%" + searchRequestDTO.getLastname().toUpperCase().trim() + "%'");
        }
        if (!searchRequestDTO.getMothersLastname().isEmpty()) {
            filterList.add("np.motherLastName LIKE " + "'%" + searchRequestDTO.getMothersLastname().toUpperCase().trim() + "%'");
        }
        if (!searchRequestDTO.getIdentificationNumber().isEmpty()) {
            filterList.add("np.identificationNumber = " + "'" + searchRequestDTO.getIdentificationNumber().trim() + "'");
        }
        if (filterList.size() > 0) {
            filters = "AND " + String.join(" AND ", filterList) + " \n";
        }
        return filters;
    }


    @Override
    public Policy findByPolicyIdOrThrowExcepcion (Long policyId) {
        PolicyJpaEntity policyJpaEntity = policyRepository.findOptionalByPolicyId(policyId,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .orElseThrow(() -> new NotDataFoundException("poliza no encontrada"));
        return new ModelMapper().map(policyJpaEntity,Policy.class);
    }
}
