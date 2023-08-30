package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.application.service.VIRHProcessService;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.exception.ResponseMessage;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/virh")
@Api(tags = "API REST VIRH")
public class VIRHController {
    private final   VIRHProcessService service;
    private final PlanUseCase planUseCase;
    @GetMapping (value = "/policyInformation/{unique}")
    @ApiOperation(value = "Servicio para recuperar información (plan, asegurado, beneficiario)")
    ResponseEntity informationPolicy(@PathVariable("unique") String param) {
        try {
           String data= this.service.getDataInformationPolicy(param);
            return ok(data);
        } catch (OperationException e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }
    @GetMapping (value = "/test")
    @ApiOperation(value = "Servicio de prueba")
    ResponseEntity generate() {
        try {
            FileDocumentDTO data= this.service.generate();
            return ok(data);
        } catch (OperationException e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @PostMapping(value = "/saveInformationPolicy")
    @ApiOperation(value = "Servicio  que guarda informacion principal")
    ResponseEntity saveInformationPolicy(@RequestBody String data ) {
        try {
            String result= this.service.saveInformationPolicy(data);
            return ok(result);
        } catch (OperationException e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @GetMapping(value = "/download-doc")
    @ApiOperation(value = "Api para descargar un archivo desde sistema")
    ResponseEntity downloadDoc(@RequestParam Long id,@RequestParam Integer enviroment) {
        try {
            FileDocument file = this.service.getDocument(id);
            if(enviroment==1){
                byte[] attachment = Base64.getDecoder().decode(file.getContent().getBytes(StandardCharsets.UTF_8));
                InputStream stream = new ByteArrayInputStream(attachment);
                InputStreamResource resource = new InputStreamResource(stream);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMime()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getDescription() + "\"")
                        .body(resource);
            }
            else {
                return  ok(file);
            }
        } catch (Exception e) {
            log.error("Error al queres descargar el documento de formato", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/virh-plans")
    @ApiOperation(value = "Retorna una lista de planes para VIRH")
    ResponseEntity getAllPlansVIRH(@RequestParam String apsCode) {
        try {
            Object list = planUseCase.getALlPlansVirh(apsCode);
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/send-whatsapp-test")
    @ApiOperation(value = "WhatsApp prueba")
    ResponseEntity WppTest(@RequestParam String message,@RequestParam String number, @RequestParam long docId) {
        service.testWhatsAppSender(number, message, docId);
        boolean res = false;
        return ok("HOla mundo");
    }

    @GetMapping(value = "/send-whatsapp-manual")
    @ApiOperation(value = "WhatsApp envios manuales")
    ResponseEntity WppSenderManually(@RequestParam Integer priority,@RequestParam Integer limitMessage) {
        boolean res = service.manualSenderNotificationToRenew(priority, limitMessage);
        ResponseMessage message = new ResponseMessage();
        message.setResponseStatus(res);
        if (res) {
            message.setResponse("Finalizado con exito");
        } else {
            message.setResponse("Fallo en el proceso");
        }
        return ok(message);
    }
}
