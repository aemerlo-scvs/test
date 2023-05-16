package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PersonUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.SearchClientDTO;
import com.scfg.core.domain.person.Person;
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
@RequestMapping(path = PersonEndPoint.BASE)
@Api(tags = "API REST Persona")
public class PersonController {

    private final PersonUseCase personUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna una lista de personas")
    ResponseEntity getAll() {
        List<Person> persons = personUseCase.getAll();
        return ok(persons);
    }

    @GetMapping(value = PersonEndPoint.PARAM_ASSIGNEDGROUPIDC)
    @ApiOperation(value = "Retorna una lista de personas de un grupo especifico")
    ResponseEntity getAllByAssignedGroup(@PathVariable Integer assignedGroup) {
        List<Person> persons = personUseCase.getAllByAssignedGroup(assignedGroup);
        return ok(persons);
    }

    @GetMapping(value = PersonEndPoint.PARAM_IDENTIFICATION)
    @ApiOperation(value = "Retorna un Rol por Id")
    ResponseEntity getByIdentificationNumberAndType(@PathVariable String identificationNumber, @PathVariable Integer documentType) {
        try {
            Person person = personUseCase.getByIdentificationNumberAndType(identificationNumber, documentType);
            return ok(person);
        } catch (NotDataFoundException e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/getJuridicalPersonByNit/{nitNumber}")
    @ApiOperation(value = "Retorna una Persona Juridica")
    ResponseEntity getJuridicalPersonByNit(@PathVariable Long nitNumber) {
        try {
            Person person = personUseCase.getJuridicalPersonByNitNumber(nitNumber);
            return ok(person);
        } catch (NotDataFoundException e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = PersonEndPoint.PARAM_IDENTIFICATION_CLF)
    @ApiOperation(value = "Retorna un objeto de cliente personalizado")
    ResponseEntity getAllByParametersClf(@RequestBody PersonDTO personDTO) {
        try {
            List<SearchClientDTO> searchClientDTOList = personUseCase.getAllByParametersClf(personDTO);
            return ok(searchClientDTOList);
        } catch (Exception e) {
            log.error("Ocurrió un error: [{}], al obtener a las personas: [{}]", e.getMessage(), personDTO.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = PersonEndPoint.PARAM_IDENTIFICATION_CLF_BY_ID)
    @ApiOperation(value = "Retorna un objeto de cliente personalizado")
    ResponseEntity getByParametersClf(@RequestBody Person person) {
        try {
            SearchClientDTO searchClientDTO = personUseCase.getByParametersClf(person);
            return ok(searchClientDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error: [{}], al obtener a la persona: [{}]", e.getMessage(), person.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Guarda una Persona")
    ResponseEntity save(@RequestBody Person person) {
        try {
            Boolean saved = personUseCase.save(person);
            return ok(saved);
        } catch (OperationException e){
            // log.error("Ocurrio un error al realizar el guardado del rol: [{}]", role, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            // log.error("Ocurrio un error al realizar el guardado del rol: [{}]", role, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza una persona")
    ResponseEntity update(@RequestBody Person person) {
        try {
            Boolean updated = personUseCase.update(person);
            return ok(updated);
        } catch (OperationException | NotDataFoundException e){
            // log.error("Ocurrio un error al realizar la modificación del rol: [{}]", role, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            // log.error("Ocurrio un error al realizar la modificación del rol: [{}]", role, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/searchPerson/{docType}/{documentNumber}/{name}")
    @ApiOperation(value = "Retorna un listado de personas")
    ResponseEntity searchPerson(@PathVariable Long docType, @PathVariable(required = false) String documentNumber, @PathVariable(required = false) String name) {
        try {
            List<Object> person = personUseCase.searchPerson(docType,documentNumber, name);
            return ok(person);
        } catch (NotDataFoundException e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
