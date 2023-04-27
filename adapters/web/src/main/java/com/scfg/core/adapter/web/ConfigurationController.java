package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ActionUseCase;
import com.scfg.core.application.port.in.ConfigurationUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Configuration;
import com.scfg.core.domain.common.Role;
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
@RequestMapping(path = ConfigurationEndPoint.BASE)
@Api(value = "API REST Configuración")
public class ConfigurationController implements ConfigurationEndPoint{
    private final ConfigurationUseCase configurationUseCase;

    @GetMapping(value = PARAM_FIRST)
    @ApiOperation(value = "Retorna la configuración")
    ResponseEntity get() {
        Configuration config = configurationUseCase.getConfiguration();
        return ok(config);
    }

    @PostMapping
    @ApiOperation(value = "Guarda una configuración")
    ResponseEntity save(@RequestBody Configuration configuration) {
        try {
            Configuration config = configurationUseCase.save(configuration);
            return ok(config);
        } catch (OperationException e){
            log.error("Ocurrio un error al realizar el guardado de la configuración: [{}]", configuration, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar el guardado de la configuración: [{}]", configuration, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza una configuración")
    ResponseEntity update(@RequestBody Configuration configuration) {
        try {
            Configuration config = configurationUseCase.update(configuration);
            return  ok(config);
        } catch (OperationException | NotDataFoundException e){
            log.error("Ocurrio un error al realizar la modificación de la configuración: [{}]", configuration, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar la modificación de la configuración: [{}]", configuration, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
