package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.smvs.SavePolicyDTO;

public interface SMVSUseGeneratePolicy {
    FileDocumentDTO generatePolicy(SavePolicyDTO o);

}
