package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RoleUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Role;
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
@RequestMapping(path = RoleEndPoint.BASE)
@Api(tags = "API REST Roles")
public class RoleController implements RoleEndPoint {

    private final RoleUseCase roleUseCase;
    private String className=RoleController.class.getName();

    @GetMapping
    @ApiOperation(value = "Retorna una lista de roles")
    ResponseEntity get() {
        List<Role> roles = roleUseCase.get();
        return ok(roles);
    }

    @GetMapping(value = RoleEndPoint.PARAM_ID)
    @ApiOperation(value = "Retorna un Rol por Id")
    ResponseEntity getById(@PathVariable long roleId) {
        try{
            Role role = roleUseCase.getById(roleId);
            return ok(role);
        }catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/roleMenu/{name}")
    @ApiOperation(value = "Retorna si el nombre del rol existe")
    ResponseEntity getByName(@PathVariable String name) {
        try{
            Boolean role = roleUseCase.existName(name);
            return ok(role);
        }catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el rol: [{}]", className, e);
          return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener el rol: [{}]", className, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @PostMapping
    @ApiOperation(value = "Guarda un Rol")
    ResponseEntity save(@RequestBody Role role) {
        try {
            Role roleAux = roleUseCase.save(role);
            return ok(roleAux);
        } catch (OperationException e){
            log.error("Ocurrio un error al realizar el guardado del rol: [{}]", role, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar el guardado del rol: [{}]", role, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza un Rol")
    ResponseEntity update(@RequestBody Role role) {
        try {
            Role roleAux = roleUseCase.update(role);
            return  ok(roleAux);
        } catch (OperationException | NotDataFoundException e){
            log.error("Ocurrio un error al realizar la modificación del rol: [{}]", role, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar la modificación del rol: [{}]", role, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(RoleEndPoint.PARAM_ID)
    @ApiOperation(value = "Elimina un Rol")
    ResponseEntity delete(@PathVariable long roleId) {
        try {
            Role role = roleUseCase.delete(roleId);
            return  ok(role);
        } catch (NotDataFoundException e){
            log.error("Ocurrio un error al realizar la eliminación del rol: [{}]", roleId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar la eliminación del rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
