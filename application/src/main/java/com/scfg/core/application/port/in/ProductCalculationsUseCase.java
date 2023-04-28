package com.scfg.core.application.port.in;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.PolicyItem;

import java.util.List;

public interface ProductCalculationsUseCase {

    PolicyItem calculateVariables (PolicyItem policyItem);
}
