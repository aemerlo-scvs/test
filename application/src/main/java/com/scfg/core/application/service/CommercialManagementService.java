package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.application.port.out.CommercialManagementPort;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommercialManagementService implements CommercialManagementUseCase {
    private final EntityManager em;
    private final CommercialManagementPort port;

    @Override
    public PersistenceResponse save(CommercialManagement obj) {
        PersistenceResponse response = port.save(obj, true);
        return response;
    }


    @Override
    public PersistenceResponse update(CommercialManagement obj) {
        PersistenceResponse response = port.update(obj);
        return response;
    }


    @Override
    public PersistenceResponse getAll() {
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_commercial_management_all")
                .registerStoredProcedureParameter("data", String.class, ParameterMode.OUT);
        query.execute();
        List<Object> result = query.getResultList();
        em.close();
        PersistenceResponse persistenceResponse = new PersistenceResponse(
                CommercialManagement.class.getSimpleName(),
                ActionRequestEnum.OBSERVATION,
                result
        );
        return persistenceResponse;
    }

    @Override
    public PersistenceResponse getAllByFilters(CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_commercial_management_all");
        query.registerStoredProcedureParameter("policyId", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("data", Long.class, ParameterMode.OUT);
        query.setParameter("fromDate", commercialManagementSearchFiltersDto.getFromDate());
        query.setParameter("toDate", commercialManagementSearchFiltersDto.getToDate());
        query.setParameter("state", commercialManagementSearchFiltersDto.getState());
        query.setParameter("subState", commercialManagementSearchFiltersDto.getSubState());
        query.execute();
        List<Object> result = query.getResultList();
        em.close();
        PersistenceResponse persistenceResponse = new PersistenceResponse(
                CommercialManagement.class.getSimpleName(),
                ActionRequestEnum.OBSERVATION,
                result
        );
        return persistenceResponse;
    }

    @Override
    public PersistenceResponse findByPolicyId(Long policyId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_commercial_management_detail");
        query.registerStoredProcedureParameter("policyId", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("data", Long.class, ParameterMode.OUT);
        query.setParameter("policyId", policyId);
        query.execute();
        String commercialManagementInfo = (String) query.getOutputParameterValue("data");
        em.close();

        PersistenceResponse persistenceResponse = new PersistenceResponse(
                CommercialManagement.class.getSimpleName(),
                ActionRequestEnum.OBSERVATION,
                commercialManagementInfo
        );
        return persistenceResponse;
    }
}
