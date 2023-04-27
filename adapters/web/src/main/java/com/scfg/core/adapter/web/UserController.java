package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.AuthUserUseCase;
import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.in.UserUseCase;
import com.scfg.core.common.enums.CompanyInitialEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;

import com.scfg.core.domain.common.AuthUser;
import com.scfg.core.domain.common.AuthenticationRequest;
import com.scfg.core.domain.ChangePasswordRequest;
import com.scfg.core.domain.common.BfsAuthenticationRequest;
import com.scfg.core.domain.common.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = UserEndPoint.BASE)
@Api(tags = "API REST Usuarios")
public class UserController {

    private final AuthUserUseCase authUserUseCase;
    private final UserUseCase userUseCase;

    private final GeneratePdfUseCase generatePdfUseCase; //Remover ESto al finalizar las generaciones de PDF

    @GetMapping
    @ApiOperation(value = "Retorna una lista de usuarios")
    ResponseEntity get() {
        List<User> users = userUseCase.get();
        return ok(users);
    }

    @GetMapping(value = UserEndPoint.PARAM_PAGE)
    @ApiOperation(value = "Retorna una lista paginada de usuarios")
    ResponseEntity getByPage(@PathVariable int page, @PathVariable int size) {
        Object users = userUseCase.getByPage(page, size);
        return ok(users);
    }

    @GetMapping(value = UserEndPoint.PARAM_ID)
    @ApiOperation(value = "Retorna un Usuario por Id")
    ResponseEntity getById(@PathVariable long userId) {
        try{
            User user = userUseCase.getById(userId);
            return ok(user);
        }catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", userId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", userId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Guarda un Usuario")
    ResponseEntity save(@RequestBody User user) {
        try {
            User userAux = userUseCase.save(user);
            return  ok(userAux);
        } catch (OperationException e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", user, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", user, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza un Usuario")
    ResponseEntity update(@RequestBody User user) {
        try {
            User userAux = userUseCase.update(user);
            return  ok(userAux);
        } catch (OperationException | NotDataFoundException e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", user, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", user, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(UserEndPoint.PARAM_ID)
    @ApiOperation(value = "Elimina un Usuario")
    ResponseEntity delete(@PathVariable long userId) {
        try {
            User user = userUseCase.delete(userId);;
            return  ok(user);
        } catch (NotDataFoundException e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", userId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al obtener el usuario: [{}]", userId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(UserEndPoint.AUTH)
    @ApiOperation("Autentica a un Usuario")
    ResponseEntity auth(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            AuthUser authUser = this.authUserUseCase.auth(authenticationRequest);
            return ResponseEntity.ok(authUser);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al obtener el usuario: [{}]", authenticationRequest, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (AuthenticationException e) {
            log.error("Ocurrio un error al obtener el usuario: [{}]", authenticationRequest, e);
            return CustomErrorType.unAuthorized("unAuthorized", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la autenticación del usuario: [{}]", authenticationRequest, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(UserEndPoint.AUTH_LDAP_AUTOMATIC)
    @ApiOperation("Autentica a un Usuario")
    ResponseEntity automaticLdapAuth(@RequestBody BfsAuthenticationRequest authenticationRequest) {
        try {
            AuthUser authUser = this.authUserUseCase.automaticLdapAuth(authenticationRequest);
            return ResponseEntity.ok(authUser);
        } catch (OperationException | UsernameNotFoundException e) {
            log.error("Ocurrio un error al obtener el usuario: [{}]", authenticationRequest.toString(), e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (AuthenticationException e) {
            log.error("Ocurrio un error al obtener el usuario: [{}]", authenticationRequest.toString(), e);
            return CustomErrorType.unAuthorized("unAuthorized", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la atenticación del usuario: [{}]", authenticationRequest.toString(), e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(UserEndPoint.AUTH_VALIDATE)
    @ApiOperation("Renueva Autenticación de un Usuario")
    ResponseEntity authRenew(@RequestHeader("Authorization") String token) {
        try {
            AuthUser authUser = this.authUserUseCase.verifyToken(token);
            return ResponseEntity.ok(authUser);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al validar el token", e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (AuthenticationException e) {
            log.error("Ocurrio un error al validar el token", e);
            return CustomErrorType.unAuthorized("unAuthorized", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al validar el token", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(UserEndPoint.CHANGE_PASSWORD)
    @ApiOperation(value = "Cambia la contraseña de un Usuario")
    ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            Boolean passwordChanged = userUseCase.changePassword(changePasswordRequest);
            return  ok(passwordChanged);
        } catch (OperationException | NotDataFoundException e){
            log.error("Ocurrio un error al cambiar el password del usuario: [{}]", changePasswordRequest.getUsername(), e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e){
            log.error("Ocurrio un error al cambiar el password del usuario: [{}]", changePasswordRequest.getUsername(), e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
