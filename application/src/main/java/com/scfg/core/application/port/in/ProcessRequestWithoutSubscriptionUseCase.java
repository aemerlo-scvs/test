package com.scfg.core.application.port.in;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ProcessRequestWithoutSubscriptionUseCase {
    void processRequest(Object request);
}
