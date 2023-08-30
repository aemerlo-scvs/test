package com.scfg.core.adapter.web;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.NewPersonUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.exception.ResponseMessage;
import com.scfg.core.domain.person.NewPerson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path ="/newPerson")
@Api(tags = "API REST NewPerson")
public class NewPersonController {
    private final NewPersonUseCase newPersonUseCase;
    private static final Logger logger = LoggerFactory.getLogger(NewPersonController.class);
    @PostMapping
    @ApiOperation(value = "Guarda una Persona")
    ResponseEntity save(@RequestBody NewPerson newPerson) {
        try {
            Boolean saved = newPersonUseCase.saveOrUpdate(newPerson);
            ResponseMessage res = new ResponseMessage();
            res.setSuccess(saved);
            if (saved) {
                res.setResponseMessage("Guardado exitoso");
            } else {
                res.setResponseMessage("Error al guardar");
            }
            return ok(res);
        } catch (OperationException e){
            logger.error("Bad Request Error: ", e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            logger.error("Server Error", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/searchPerson")
    @ApiOperation(value = "Retorna un listado de personas")
    ResponseEntity searchPerson(@RequestParam Long documentTypeIdc, @RequestParam(required = false) String identificationNumber, @RequestParam(required = false) String name) {
        try {
            Object newPerson = newPersonUseCase.searchPerson(documentTypeIdc,identificationNumber, name);
            return ok(newPerson);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
