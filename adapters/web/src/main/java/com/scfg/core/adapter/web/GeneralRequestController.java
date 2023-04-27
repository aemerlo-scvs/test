package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.GeneralRequestUseCase;
import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.InactiveRequestDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.ClfSearchRequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = GeneralRequestEndPoint.BASE)
@Api(tags = "API REST Solicitudes")
public class GeneralRequestController implements GeneralRequestEndPoint {

    private final GeneralRequestUseCase generalRequestUseCase;

    @PostMapping(value = INACTIVE)
    @ApiOperation(value = "Inactiva una solicitud")
    ResponseEntity inactiveRequest(@RequestBody InactiveRequestDTO requestDTO) {
        try {
            Boolean response = generalRequestUseCase.inactiveRequest(requestDTO);
            return ok(response);
        } catch (NotDataFoundException e) {
            log.error("No se encontró la solicitud a inactivar: [{}], error: [{}]", requestDTO.toString(), e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al inactivar la solicitud: [{}], error: [{}]", requestDTO.toString(), e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = PARAM_FILTERS)
    @ApiOperation(value = "")
    ResponseEntity getByClfFilters(@RequestBody PersonDTO personDTO) {
        try {
            List<RequestDTO> list = generalRequestUseCase.getAllClfByFilters(personDTO);
            return ok(list);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/clf/paginated-search")
    @ApiOperation(value = "")
    ResponseEntity getPaginatedRequests(@RequestParam Integer page, @RequestParam Integer size,
                                        @RequestBody ClfSearchRequestDTO searchRequestDTO) {
        try {
            return ok(generalRequestUseCase.getPaginatedRequests(searchRequestDTO, page, size));
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }


    @PostMapping(value = SUBSCRIPTION_FILTERS)
    @ApiOperation(value = "")
    ResponseEntity getByClfSubscriptionFilters(@RequestBody ClfSearchRequestDTO searchRequestDTO) {
        try {
            List<RequestDetailDTO> list = generalRequestUseCase.getAllClfBySubscriptionFilters(searchRequestDTO);
            return ok(list);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = VERIFY_OPERATION_NUMBER)
    @ApiOperation(value = "")
    ResponseEntity verifyOperationNumber(@RequestParam String operationNumber, @RequestParam Integer requestId) {
        try {
            Boolean validation = generalRequestUseCase.isOperationNumberDuplicated(operationNumber, requestId);
            return ok(validation);
        } catch (NotDataFoundException e) {
            log.error("Ocurrió un error al verificar la duplicidad del número de operación: {}", e.getMessage());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al verificar la duplicidad del número de operación: {}", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
