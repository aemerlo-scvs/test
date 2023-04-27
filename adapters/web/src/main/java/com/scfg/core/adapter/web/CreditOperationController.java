package com.scfg.core.adapter.web;


import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CreditOperationUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.CreditOperation;
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
@RequestMapping(path = CreditOperationEndpoint.CREDIT_OPERATION_BASE_ROUTE)
@Api(value = "API Operaciones crediticias")
public class CreditOperationController implements CreditOperationEndpoint {

    private final CreditOperationUseCase creditOperationUseCase;


    @GetMapping
    @ApiOperation(value = "Lista todas las operaciones crediticias")
    @Override
    public ResponseEntity getAllCreditsOperations() {
        List<CreditOperation> clients = creditOperationUseCase.getAllCreditsOperations();
        if (clients.isEmpty()) {
            return CustomErrorType.notContent(CREDIT_OPERATION_TITLE, "No data");
        }
        return ok(clients);
    }

    @PostMapping
    @ApiOperation(value = "Registra nueva operacion crediticia")
    @Override
    public ResponseEntity<PersistenceResponse> saveCreditOperation(@RequestBody CreditOperation creditOperation) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = creditOperationUseCase.registerCreditOperation(creditOperation);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest(CREDIT_OPERATION_TITLE, e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
