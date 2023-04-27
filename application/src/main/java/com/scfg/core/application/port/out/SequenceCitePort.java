package com.scfg.core.application.port.out;

import com.scfg.core.domain.SequenceCite;

public interface SequenceCitePort {
    SequenceCite getSequence(long companyIdc, String year);

    long saveOrUpdate(SequenceCite sequenceCite);
}
