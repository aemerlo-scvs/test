package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;
import com.scfg.core.domain.dto.FileDocumentDTO;

import java.util.List;
import java.util.Map;

public interface ClientPort {


    List<Client> getAllClients();
    Client getClientById(long clientId);
    Client getClientByName(String clientName);
    Client getClientByCI(String clientCI);

    PersistenceResponse save(Client client, boolean returnEntity);

    PersistenceResponse update(Client client);

    PersistenceResponse delete(Client client);

    Map<String, Object>findOrUpsert(Client client) throws NoSuchFieldException, IllegalAccessException;

    void updateAccumulatedAmount(long policyTypeReferenceId, String clientDocumentNumber, Double accumulatedAmount);
}
