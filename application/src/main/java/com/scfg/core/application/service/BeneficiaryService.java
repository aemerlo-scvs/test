package com.scfg.core.application.service;

import com.scfg.core.application.port.in.BeneficiaryUseCase;
import com.scfg.core.application.port.out.BeneficiaryPort;
import com.scfg.core.domain.Beneficiary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService implements BeneficiaryUseCase {
    private final BeneficiaryPort beneficiaryPort;
    @Override
    public List<Beneficiary> getAll() {
        return  beneficiaryPort.findAll();
    }

    @Override
    public Boolean save(Beneficiary o) throws Exception {
        return beneficiaryPort.saveOrUpdate(o);
    }

    @Override
    public Boolean update(Beneficiary o) throws Exception {
        return beneficiaryPort.saveOrUpdate(o);
    }

    @Override
    public Boolean delete(Beneficiary o) {
        return null;
    }
}
