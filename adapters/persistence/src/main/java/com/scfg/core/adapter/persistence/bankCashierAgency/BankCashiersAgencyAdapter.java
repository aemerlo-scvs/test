package com.scfg.core.adapter.persistence.bankCashierAgency;

import com.scfg.core.application.port.out.BankCashiersAgencyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.DetailsLoadCashiers1;
import lombok.RequiredArgsConstructor;

import java.util.List;
@PersistenceAdapter
@RequiredArgsConstructor
public class BankCashiersAgencyAdapter implements BankCashiersAgencyPort {
    private final BankCashierAgencyRepository repository;
    @Override
    public List<DetailsLoadCashiers1> findDetailsLoadCashiers() {

        return repository.findByDetailsLoadCashiers();
    }
}
