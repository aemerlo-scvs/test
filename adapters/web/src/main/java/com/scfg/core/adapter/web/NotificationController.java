package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.NotificationUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.NotificationDTO;
import com.scfg.core.domain._NotificationDTO;
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
@RequestMapping(path = NotificationEndpoint.BASE)
@Api(tags = "API REST Notificaciones")
public class NotificationController implements NotificationEndpoint {

    private final NotificationUseCase notificationUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna una lista de Notificaciones")
    ResponseEntity getAll() {
        List<NotificationDTO> notificationDTOList = notificationUseCase.getAll();
        return ok(notificationDTOList);
    }

    @GetMapping(value = PARAM_ID)
    @ApiOperation(value = "Retorna una lista de Notificaciones")
    ResponseEntity getAllByToUserId(@PathVariable long toUserId) {
        List<NotificationDTO> notificationDTOList = notificationUseCase.getAllToUserId(toUserId);
        return ok(notificationDTOList);
    }

    @PutMapping(value = PARAM_READ)
    @ApiOperation(value = "Actualiza una notificación a leida")
    ResponseEntity updateToRead(@RequestBody NotificationDTO notificationDTO) {
        try {
            Boolean isRead = notificationUseCase.updateToRead(notificationDTO);
            return ok(isRead);
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al realizar la modificación de la notificación: [{}]", notificationDTO, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la modificación de la notificación: [{}]", notificationDTO, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping(value = PARAM_NOTE)
    @ApiOperation(value = "Actualiza la nota de la notificación leida")
    ResponseEntity updateNote(@RequestBody NotificationDTO notificationDTO) {
        try {
            Boolean note = notificationUseCase.updateNote(notificationDTO);
            return ok(note);
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al guardar la nota de la notificación: [{}]", notificationDTO, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al guardar la nota de la notificación: [{}]", notificationDTO, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Guarda una notificación a leida")
    ResponseEntity save(@RequestBody _NotificationDTO _notificationDTO) {
        try {
            Boolean isRead = notificationUseCase.save(_notificationDTO);
            return ok(isRead);
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al guardar la notificación: [{}]", _notificationDTO, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al guardar la notificación: [{}]", _notificationDTO, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
