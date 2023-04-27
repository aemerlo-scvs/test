package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;

import java.util.List;

public interface ClientUserCase {
    List<Client> getAllClients();
    PersistenceResponse registerClient(Client client);



}
