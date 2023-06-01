package com.scfg.core.application.port.in;

import com.scfg.core.domain.FileDocument;

import java.util.Date;
import java.util.List;

public interface GenerateCertificateCoverageUseCase {

    FileDocument generateVINCertificateCoverage(Long requestId, Object object);
    String regenerateVINCertificateCoverage(Long planId, Integer creditTermInYears, Date date);
}
