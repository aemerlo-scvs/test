package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RoleMenuUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.RoleMenuDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = RoleEndPoint.BASE_ROLE_MENU)
@Api(tags = "API REST Rol Menu")
public class RoleMenuController implements RoleEndPoint {

    private final RoleMenuUseCase roleMenuUseCase;

    @PostMapping
    @ApiOperation(value = "Guarda los Menus y Roles")
    ResponseEntity saveRoleMenu(@RequestBody RoleMenuDTO roleMenuDTO) {
        try {
            Boolean menuAux = roleMenuUseCase.save(roleMenuDTO);
            return ok(menuAux);
        } catch (OperationException e){
            log.error("Ocurrio un error al asignar los menus: [{}]", roleMenuDTO, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al asignar los menus: [{}]", roleMenuDTO, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }


}
