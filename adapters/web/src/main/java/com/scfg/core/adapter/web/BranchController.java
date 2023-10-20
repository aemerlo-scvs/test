package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.BranchUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.scfg.core.domain.Branch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/branch")
@Api(tags = "API REST Ramo")
public class BranchController {

    private final BranchUseCase branchUseCase;

    @GetMapping(value = "/all")
    @ApiOperation(value = "Listado de ramos")
    public ResponseEntity getAllBranchs() {
        List<Branch> branchList = branchUseCase.getAllBranchs();
        if (branchList.isEmpty()) {
            return CustomErrorType.notContent("Get branchs", "No data");
        }
        return ok(branchList);
    }
    @PostMapping(value = "/save")
    @ApiOperation(value = "Registrar un nuevo ramo")
    public ResponseEntity<PersistenceResponse> saveBranch(@RequestBody Branch branch) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = branchUseCase.registerBranch(branch);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Branch", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @PostMapping(value ="/update")
    @ApiOperation(value = "Actualiza el ramo")
    public ResponseEntity<PersistenceResponse> updateBranch(@RequestBody Branch branch) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = branchUseCase.updateBranch(branch);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Branch", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}" )
    @ApiOperation(value = "Dar de baja logicamente")
    public ResponseEntity<PersistenceResponse> deleteBranch(@PathVariable Long id) {
        try {
            // Aplicando validaciones
            PersistenceResponse response = branchUseCase.deleteBranch(id);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Branch", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }



}
