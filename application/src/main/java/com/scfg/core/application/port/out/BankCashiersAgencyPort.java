package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.DetailsLoadCashiers1;

import java.util.List;

public interface BankCashiersAgencyPort {

    List<DetailsLoadCashiers1> findDetailsLoadCashiers();
}
