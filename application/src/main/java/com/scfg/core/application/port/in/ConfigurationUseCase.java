package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.Action;
import com.scfg.core.domain.common.Configuration;
import com.scfg.core.domain.common.Role;

public interface ConfigurationUseCase {
    Configuration getConfiguration();

    Configuration save(Configuration configuration);

    Configuration update(Configuration configuration);

}
