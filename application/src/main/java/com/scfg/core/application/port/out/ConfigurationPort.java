package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Action;
import com.scfg.core.domain.common.Configuration;

public interface ConfigurationPort {
    Configuration find();

    Configuration saveOrUpdate(Configuration configuration);

}
