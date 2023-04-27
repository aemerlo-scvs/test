package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.JuridicalPersonUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.dto.JuridicalPersonDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = JuridicalPersonEndPoint.BASE)
@Api(tags = "API REST Persona Juridica")
public class JuridicalPersonController implements JuridicalPersonEndPoint {

    private final JuridicalPersonUseCase juridicalPersonUseCase;

    @GetMapping(value = JuridicalPersonEndPoint.FINDALLJURIDIC)
    @ApiOperation(value = "Retorna un listado de personas juridicas con logo y polizas")
    ResponseEntity findAllByAssignedGroup(@PathVariable int assignedGroup) {
        try {
            List<JuridicalPersonDTO> juridicalPerson = juridicalPersonUseCase.getAllJuridicalPerson(assignedGroup);
            return ok(juridicalPerson);
        } catch (NotDataFoundException e) {
             log.error("Ocurrio un error al obtener el listado de personas juridicas: [{}]", e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
             log.error("Ocurrio un error al obtener el listado de personas juridicas: [{}]", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
