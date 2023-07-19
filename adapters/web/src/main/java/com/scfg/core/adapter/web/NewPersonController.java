package com.scfg.core.adapter.web;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.NewPersonUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.person.NewPerson;
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
@RequestMapping(path ="/newPerson")
@Api(tags = "API REST NewPersona")
public class NewPersonController {
    private final NewPersonUseCase newPersonUseCase;
    @PostMapping
    @ApiOperation(value = "Guarda una Persona")
    ResponseEntity save(@RequestBody NewPerson newPerson) {
        try {
            Boolean saved = newPersonUseCase.save(newPerson);
            return ok(saved);
        } catch (OperationException e){
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
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
