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
import java.util.List;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path ="/newPerson")
@Api(tags = "API REST NewPersona")
public class NewPersonController {
    private final NewPersonUseCase newPersonUseCase;


//    @GetMapping
//    @ApiOperation(value = "Retorna una lista de personas")
//    ResponseEntity getAll() {
//        List<NewPerson> persons = newPersonUseCase.getAll();
//        return ok(persons);
//    }
//    @PostMapping
//    @ApiOperation(value = "Guarda una Persona")
//    ResponseEntity save(@RequestBody NewPerson person) {
//        try {
//            Boolean saved = newPersonUseCase.save(person);
//            return ok(saved);
//        } catch (OperationException e){
//            return CustomErrorType.badRequest("Bad Request", e.getMessage());
//        } catch (Exception e){
//            return CustomErrorType.serverError("Server Error", e.getMessage());
//        }
//    }
//
//    @PutMapping
//    @ApiOperation(value = "Actualiza una persona")
//    ResponseEntity update(@RequestBody NewPerson person) {
//        try {
//            Boolean updated = newPersonUseCase.update(person);
//            return ok(updated);
//        } catch (OperationException | NotDataFoundException e){
//            return CustomErrorType.badRequest("Bad Request", e.getMessage());
//        } catch (Exception e){
//            return CustomErrorType.serverError("Server Error", e.getMessage());
//        }
//    }
    @GetMapping(value = "/searchPerson")
    @ApiOperation(value = "Retorna un listado de personas")
    ResponseEntity searchPerson(@RequestParam Long docType, @RequestParam(required = false) String documentNumber, @RequestParam(required = false) String name) {
        try {
            Object person = newPersonUseCase.searchPerson(docType,documentNumber, name);
            return ok(person);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
