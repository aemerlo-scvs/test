package com.scfg.core.application.port.out;

import com.scfg.core.domain.MathReserve;

import java.util.List;

public interface MathReservePort {
    
    long saveOrUpdate(MathReserve mathReserve);

    MathReserve getById(Long Id);

    List<MathReserve> getAllByVersionInsurerYearAndTotalYear(String version, Integer insurerYear, Integer totalYear);
}
