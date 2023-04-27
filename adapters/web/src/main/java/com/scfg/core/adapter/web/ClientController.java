package com.scfg.core.adapter.web;


import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ClientUserCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ClientEndpoint.CLIENT_BASE_ROUTE)
@Api(value = "API Clientes")
public class ClientController implements ClientEndpoint {

    private final ClientUserCase clientUserCase;

    @GetMapping
    @ApiOperation(value = "Lista todos los clientes")
    @Override
    public ResponseEntity getAllClients() {
        List<Client> clients = clientUserCase.getAllClients();
        if (clients.isEmpty()) {
            return CustomErrorType.notContent(CLIENT_TITLE, "No data");
        }
        return ok(clients);
    }

    @PostMapping
    @ApiOperation(value = "Registra nuevo cliente")
    @Override
    public ResponseEntity<PersistenceResponse> saveClient(@RequestBody Client client) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = clientUserCase.registerClient(client);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest(CLIENT_TITLE, e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

}
