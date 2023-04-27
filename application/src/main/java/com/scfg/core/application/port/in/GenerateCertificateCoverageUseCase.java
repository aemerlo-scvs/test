package com.scfg.core.application.port.in;

import com.scfg.core.domain.FileDocument;

public interface GenerateCertificateCoverageUseCase {

    FileDocument generateVINCertificateCoverage(Long requestId, Object object);
}
