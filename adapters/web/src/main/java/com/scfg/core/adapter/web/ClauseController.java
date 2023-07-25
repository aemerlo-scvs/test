package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ClauseUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
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
@RequestMapping(path ="/clause")
@Api(tags = "API REST Clausulas")
public class ClauseController {
    private  final ClauseUseCase clauseUseCase;
    @GetMapping(value ="/all")
    @ApiOperation(value = "Listado de las clausulas")
    public ResponseEntity findAll() {
        List<Clause> clauseList = clauseUseCase.getAllClause();
        return ok(clauseList);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "Guardar las clausulas")
    public ResponseEntity save(@RequestBody Clause clause) {
        try {
            PersistenceResponse response = clauseUseCase.saveOrUpdate(clause);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Clause", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }

    }
    @PostMapping(value = "/update")
    @ApiOperation(value = "Actualizar las Clausulas")
    public ResponseEntity update(@RequestBody Clause clause) {
        try {
            PersistenceResponse response =clauseUseCase.saveOrUpdate(clause);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Clause", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @DeleteMapping(value =  "/delete/{id}")
    @ApiOperation(value = "Dar de baja la clasulas")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            PersistenceResponse response = clauseUseCase.delete(id);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Clause", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @GetMapping(value = "/findById/{id}")
    @ApiOperation(value = "Obtener clausula por ID")
    public ResponseEntity findById(@PathVariable Long id) {
        try {
            Clause response = clauseUseCase.getById(id);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Clause", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @GetMapping(value = "/findByProduct/{productId}")
    @ApiOperation(value = "Listado de clausulas por productos id")
    public ResponseEntity findClauseByProductId(@PathVariable Long productId) {
        try {
            List<Clause> clauseList = clauseUseCase.getAllClauseByProductId(productId);
            return ok(clauseList);
        }catch (Exception ex){
            return CustomErrorType.notContent("Clausula por producto id",ex.getMessage());
        }

    }


}
