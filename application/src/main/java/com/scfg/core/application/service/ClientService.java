package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ClientUserCase;
import com.scfg.core.application.port.out.ClientPort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientUserCase {

    private final ClientPort clientPort;

    @Override
    public List<Client> getAllClients() {
        return clientPort.getAllClients();
    }

    @Override
    public PersistenceResponse registerClient(Client client) {
        return clientPort.save(client, true);
    }


}
