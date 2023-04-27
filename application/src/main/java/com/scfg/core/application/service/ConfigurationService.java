package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ConfigurationUseCase;
import com.scfg.core.application.port.out.ConfigurationPort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService implements ConfigurationUseCase {

    private final ConfigurationPort configurationPort;

    @Override
    public Configuration getConfiguration() {
        return configurationPort.find();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Configuration save(Configuration configuration) {
        return configurationPort.saveOrUpdate(configuration);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Configuration update(Configuration configuration) {
        return configurationPort.saveOrUpdate(configuration);
    }
}
