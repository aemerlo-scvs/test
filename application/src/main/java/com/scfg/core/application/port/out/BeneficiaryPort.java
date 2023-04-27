package com.scfg.core.application.port.out;

import com.scfg.core.domain.Beneficiary;

import java.util.List;

public interface BeneficiaryPort {
    List<Beneficiary> findAll();
    Boolean saveOrUpdate(Beneficiary o) throws Exception;
    Boolean saveAll(List<Beneficiary> lis);
    Boolean saveAllOrUpdateCLF(List<Beneficiary> lis);
    List<Beneficiary> findAllByPolicyItemId(Long policyItemId);
    List<Beneficiary> findAllByGeneralRequestId(Long generalRequestId);
}
