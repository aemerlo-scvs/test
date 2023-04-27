package com.scfg.core.application.port.out;

import com.scfg.core.domain.SequencePolicy;

public interface SequencePolicyPort {
    SequencePolicy getSequence(long policyId);

    long saveOrUpdate(SequencePolicy sequencePolicy);
}
