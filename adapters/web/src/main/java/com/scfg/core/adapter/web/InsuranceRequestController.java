package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.InsuranceRequestUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.InsuranceRequest;
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
@RequestMapping(path = InsuranceRequestEndpoint.INSURANCE_REQUEST_BASE_ROUTE)
@Api(value = "API Solicitudes de seguro")
public class InsuranceRequestController implements InsuranceRequestEndpoint {

    private final InsuranceRequestUseCase insuranceRequestUseCase;

    @GetMapping
    @ApiOperation(value = "Lista todas las solicitudes de seguro")
    @Override
    public ResponseEntity getAllInsuranceRequests() {
        List<InsuranceRequest> insuranceRequests = insuranceRequestUseCase.getAllInsuranceRequests();
        if (insuranceRequests.isEmpty()) {
            return CustomErrorType.notContent(INSURANCE_REQUEST_TITLE, "No data");
        }
        return ok(insuranceRequests);
    }

    @PostMapping
    @ApiOperation(value = "Registra una nueva solicitud de seguro")
    @Override
    public ResponseEntity<PersistenceResponse> saveInsuranceRequest(@RequestBody InsuranceRequest insuranceRequest) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = insuranceRequestUseCase.registerInsuranceRequest(insuranceRequest);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest(INSURANCE_REQUEST_TITLE, e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
