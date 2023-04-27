package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.VinUseCase;
import com.scfg.core.common.exception.OperationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/sender")
@Api(tags = "API Rest - Mensajeria")
public class SenderController {

    private final VinUseCase vinUseCase;

    @PostMapping(value = "/resend")
    @ApiOperation(value = "Llamado para reenvío de mensajeria")
    ResponseEntity reSend(@RequestParam Long requestId, @RequestParam String productInitial,
                          @RequestParam Integer reSendBy, @RequestParam String to){
        try {
            switch (productInitial) {
                case "VIN":
                    vinUseCase.reSend(requestId, reSendBy, to);
                    return ok(true);
                default:
                    log.error("Ocurrió un error al querer procesar la solicitud: [{}]", "Error al procesar solicitud de: " + productInitial);
                    return CustomErrorType.serverError("Server Error", "Error al procesar la solicitud");
            }
        }
        catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        }
        catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
