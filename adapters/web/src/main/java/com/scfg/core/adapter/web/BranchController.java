package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.BranchUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.scfg.core.domain.Branch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = BranchEndPoint.BASE)
@Api(value = "API REST Ramo")
public class BranchController implements BranchEndPoint{

    private final BranchUseCase branchUseCase;

    @GetMapping(value = BranchEndPoint.GETBRANCHALL)
    @ApiOperation(value = "Lista todos los ramos")
    public ResponseEntity getAllBranchs() {
        List<Branch> branchList = branchUseCase.getAllBranchs();
        if (branchList.isEmpty()) {
            return CustomErrorType.notContent("Get branchs", "No data");
        }
        return ok(branchList);
    }
}
