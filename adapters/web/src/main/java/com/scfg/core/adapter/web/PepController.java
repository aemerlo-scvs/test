package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PepUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Pep;
import com.scfg.core.domain.dto.SearchPepDTO;
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
@RequestMapping(path = PepEndpoint.BASE)
@Api(tags = "API REST PEP")
public class PepController implements PepEndpoint {

    private final PepUseCase pepUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna una lista de PEPs")
    ResponseEntity getAll() {
        List<Pep> list = pepUseCase.getAll();
        return ok(list);
    }

    @GetMapping(value = PARAM_PAGE)
    @ApiOperation(value = "Retorna una lista paginada de PEPs")
    ResponseEntity getAllByPage(@PathVariable int page, @PathVariable int size) {
        Object list = pepUseCase.getAllByPage(page, size);
        return ok(list);
    }

    @PostMapping(value = PARAM_EXISTS)
    @ApiOperation(value = "Retorna un Boolean si existe el cliente PEP por número de identificación o nombre completo")
    ResponseEntity existsByIdentificationNumberOrName(@RequestBody SearchPepDTO pepDTO) {
        boolean exists = pepUseCase.existsByIdentificationNumberOrName(pepDTO);
        return ok(exists);
    }

    @GetMapping(value = PARAM_SEARCH_KEY_WORD)
    @ApiOperation(value = "Busca un cliente PEP por una palabra clave")
    ResponseEntity searchByKeyWord(@PathVariable String keyWord) {
        List<Pep> list = pepUseCase.getAllByKeyWord(keyWord);
        return ok(list);
    }

    @PostMapping(value = BASE_IMPORT)
    @ApiOperation(value = "Importa la lista de clientes pep")
    ResponseEntity importPeps(@RequestBody List<Pep> pepList) {
        try {
            Boolean saved = pepUseCase.saveOrUpdateAll(pepList);
            return ok(saved);
        } catch (OperationException e) {
            log.error("Ocurrio un error al realizar la importación de los clientes PEP: [{}], excepción: [{}]", pepList.toString(), e.getMessage());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la importación de los clientes PEP: [{}], excepción: [{}]", pepList.toString(), e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Guarda un cliente PEP")
    ResponseEntity save(@RequestBody Pep pep) {
        try {
            Boolean saved = pepUseCase.saveOrUpdate(pep);
            return ok(saved);
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar el guardado del cliente PEP: [{}], error: [{}]", pep.toString(), e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza un cliente PEP")
    ResponseEntity update(@RequestBody Pep pep) {
        try {
            Boolean updated = pepUseCase.saveOrUpdate(pep);
            return ok(updated);
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la modificación del cliente PEP: [{}], error: [{}]", pep.toString(), e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(PARAM_ID)
    @ApiOperation(value = "Elimina un cliente pep")
    ResponseEntity delete(@PathVariable long pepId) {
        try {
            Boolean deleted = pepUseCase.delete(pepId);
            return ok(deleted);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al realizar la eliminación del cliente pep: [{}], excepción: [{}]", pepId, e.getMessage());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la eliminación del cliente pep: [{}], excepción: [{}]", pepId, e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
