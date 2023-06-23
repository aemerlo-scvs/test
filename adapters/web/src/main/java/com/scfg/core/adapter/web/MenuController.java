package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.MenuUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Menu;
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
@RequestMapping(path = MenuEndPoint.BASE)
@Api(tags = "API REST Menus")
public class MenuController implements MenuEndPoint {

    private final MenuUseCase menuUseCase;
    String className=MenuController.class.getName();
    @GetMapping
    @ApiOperation(value = "Retorna una lista de Menus")
    ResponseEntity getAll() {
        List<Menu> menus = menuUseCase.getAll();
        return ok(menus);
    }

    @GetMapping(value = MenuEndPoint.PARAM_DETAIL)
    @ApiOperation(value = "Retorna una lista de Menus")
    ResponseEntity getAllWithDetail() {
        List<Menu> menus = menuUseCase.getAllWithDetail();
        return ok(menus);
    }

    @GetMapping(value = MenuEndPoint.PARAM_ID)
    @ApiOperation(value = "Retorna un Menu por Id")
    ResponseEntity getById(@PathVariable long menuId) {
        try{
            Menu menu = menuUseCase.getById(menuId);
            return ok(menu);
        }catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menuId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menuId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = MenuEndPoint.GET_BY_ROLEID)
    @ApiOperation(value = "Retorna una lista de Menus por Id Rol")
    ResponseEntity getByRoleId(@PathVariable long roleId) {
        try {
            List<Menu> menus = menuUseCase.getByRoleId(roleId);
            return ok(menus);
        } catch (Exception e){
            log.error("Ocurrio un error al obtener los menus por el id rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Guarda un Menu")
    ResponseEntity save(@RequestBody Menu menu) {
        try {
            Boolean menuAux = menuUseCase.save(menu);
            return ok(menuAux);
        } catch (OperationException e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menu, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menu, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza un Menu")
    ResponseEntity update(@RequestBody Menu menu) {
        try {
            Boolean menuAux = menuUseCase.update(menu);
            return  ok(menuAux);
        } catch (OperationException | NotDataFoundException e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menu, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al obtener el menu: [{}]", menu, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/exists/{name}")
    @ApiOperation(value = "Retorna si el nombre del rol existe")
    ResponseEntity existsNameFather(@PathVariable String name) {
        try{
            Boolean sw = menuUseCase.existNameFather(name);
            return ok(sw);
        }catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el rol: [{}]", className, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener el rol: [{}]", className, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @DeleteMapping(MenuEndPoint.PARAM_ID)
    @ApiOperation(value = "Elimina un Menu")
    ResponseEntity delete(@PathVariable long menuId) {
        try {
            Boolean menu = menuUseCase.delete(menuId);
            return  ok(menu);
        } catch (NotDataFoundException e){
            log.error("Ocurrio un error al realizar la eliminación del rol: [{}]", menuId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al realizar la eliminación del rol: [{}]", menuId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
