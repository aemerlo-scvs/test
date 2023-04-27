package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.CoverageProduct;

import java.util.List;

public interface CoverageProductPort {
    List<CoverageProduct> getAll();
}
