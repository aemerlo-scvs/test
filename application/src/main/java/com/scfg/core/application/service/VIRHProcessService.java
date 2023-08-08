package com.scfg.core.application.service;

import com.scfg.core.application.port.in.VIRHUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

@Service
@Slf4j
@RequiredArgsConstructor
public class VIRHProcessService implements VIRHUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public String getDataInformationPolicy(String param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_view_data_policy_propose");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("param", param);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }
}
