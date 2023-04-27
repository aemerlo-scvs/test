package com.scfg.core.application.port.in;

import com.scfg.core.domain.Beneficiary;

import java.util.List;

public interface BeneficiaryUseCase {
    List<Beneficiary> getAll();
    Boolean save(Beneficiary o) throws Exception;
    Boolean update(Beneficiary o) throws Exception;
    Boolean delete(Beneficiary o);
}
