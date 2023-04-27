package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;

import com.scfg.core.domain.Broker;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;

import java.util.List;

public interface BrokerPort {
    PersistenceResponse save(Broker broker, boolean returnEntity);

    PersistenceResponse update(Broker broker);

    PersistenceResponse delete(BrokerDTO broker);

    List<BrokerDTO> getAllBrokers();

    List<BrokerDTO> getfilterParamenters(FilterParamenter paramenter);

    Broker getBrokerById(long brokerId);
}
