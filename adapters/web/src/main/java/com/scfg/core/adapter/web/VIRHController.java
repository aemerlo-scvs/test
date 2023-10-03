package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.application.service.VIRHProcessService;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.exception.ResponseMessage;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.virh.DebtRegisterUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/virh")
@Api(tags = "API REST VIRH")
public class VIRHController {
    private final VIRHProcessService service;
    private final PlanUseCase planUseCase;
    private final CommercialManagementUseCase commercialManagementUseCase;

    @GetMapping(value = "/policyInformation/{unique}")
    @ApiOperation(value = "Servicio para recuperar información (plan, asegurado, beneficiario)")
    ResponseEntity informationPolicy(@PathVariable("unique") String param) {
        try {
            boolean sw = commercialManagementUseCase.existsComercialManagementId(param);
            if (sw) {
                String data = this.service.getDataInformationPolicy(param);
                return ok(data);
            } else {
                return new ResponseEntity<>("Codigo no valido", HttpStatus.NOT_FOUND);
            }

        } catch (OperationException e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "Servicio de prueba")
    ResponseEntity generate() {
        try {
            FileDocumentDTO data = this.service.generate();
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
    ResponseEntity saveInformationPolicy(@RequestBody String data) {
        try {
            String result = this.service.saveInformationPolicy(data);
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
    ResponseEntity downloadDoc(@RequestParam Long id, @RequestParam Integer enviroment) {
        try {
            FileDocument file = this.service.getDocument(id);
            if (enviroment == 1) {
                byte[] attachment = Base64.getDecoder().decode(file.getContent().getBytes(StandardCharsets.UTF_8));
                InputStream stream = new ByteArrayInputStream(attachment);
                InputStreamResource resource = new InputStreamResource(stream);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMime()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getDescription() + "\"")
                        .body(resource);
            } else {
                return ok(file);
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
    ResponseEntity WppTest(@RequestParam String message, @RequestParam String number, @RequestParam long docId) {
        service.testWhatsAppSender(number, message, docId);
        boolean res = false;
        return ok("HOla mundo");
    }

    @GetMapping(value = "/aqwe-tjf-qad")
    @ApiOperation(value = "Da biara anohyeto samufo")
    ResponseEntity ChangeLimitMessageDiary(@RequestParam Integer newLimit) {

        boolean res = service.changeLimitToSendMessageDiary(newLimit);
        return ok(res);
    }

    @GetMapping(value = "/send-whatsapp-manual")
    @ApiOperation(value = "WhatsApp envios manuales")
    ResponseEntity WppSenderManually(@RequestParam Integer priority, @RequestParam Integer limitMessage) {
        boolean res = service.manualSenderNotificationToRenew(priority, limitMessage);
        ResponseMessage message = new ResponseMessage();
        message.setSuccess(res);
        if (res) {
            message.setResponseMessage("Finalizado con exito");
        } else {
            message.setResponseMessage("Fallo en el proceso");
        }
        return ok(message);
    }

    @PostMapping(value = "/updateDebtRegistry")
    @ApiOperation(value = "Servicio  que guarda el registro de deuda")
    ResponseEntity updateDebtRegistry(@RequestBody DebtRegisterUpdateDTO data) {
        try {
            this.service.updateDebtRegister(data);
            return ok("actualizado con exito");
        } catch (OperationException e) {
            log.error("Ocurrió un error al guardar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al guardar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pago", method = RequestMethod.GET)
    @ApiOperation(value = "Confirma el Pago desde Libelula")
    ResponseEntity ConfirmacionPago(@RequestParam Map<String, String> pago) {
        String transaction_id = pago.get("transaction_id");
        String error = pago.get("error");
        String message = pago.get("message");
        String cancel_order = pago.get("cancel_order");
        String payment_method = pago.get("payment_method");
        String payment_method_id = pago.get("payment_method_id");
        String idqr = pago.get("idqr");

        if (Objects.equals(error, "0")) {
            // enviar parametros al procedimiento almacenado
            String data = this.service.savePayment(transaction_id, payment_method);
            String data_modified = data.substring(1, data.length() - 1);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> ObjMap = null;
            try {
                ObjMap = mapper.readValue(data_modified, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            //si el numero de coberturas es igual a 2 (muerte, rh) excluir documento clausulas de cancer
            ArrayList<String> exclusionList = new ArrayList<>();
            int nrocob = Integer.parseInt(ObjMap.get("NroCoberturas").toString());
            if (nrocob == 2) {
                exclusionList.add("CONDICIONES_VCMA");
            }

            //---- generar pdf
            FileDocumentDTO fd = service.generateAndSavePolicyPdf(ObjMap.get("PolicyNumber").toString(), Long.parseLong(ObjMap.get("ProductID").toString()), exclusionList, Long.parseLong(ObjMap.get("PolicyItemId").toString()));

            //---- enviar whatsapp
            //      service.sendWhatsAppWithAttachment(ObjMap.get("phoneNumber").toString(),"Hello!!",Long.parseLong(ObjMap.get("GeneralRequest").toString()), Long.parseLong("1"));
            String Product = ObjMap.get("OldProductInitials").toString().contains("SMVS") ? "SEPELIO" : "VIDA MÁS CÁNCER";
            String welcomemessage = service.getWelcomeMessageText(ObjMap.get("InsuredName").toString(), Product, ObjMap.get("Link").toString());
            //      service.sendWhatsAppWithAttachment("77286265",welcomemessage,Long.parseLong(ObjMap.get("GeneralRequest").toString()), fd.getId() ,1);
            service.sendWhatsApp("77286265", welcomemessage, Long.parseLong(ObjMap.get("GeneralRequest").toString()), 1);
        }

        System.out.println(pago.get("transaction_id").toString());
        System.out.println(pago.values().toString());

        ResponseMessage message3 = new ResponseMessage();
        message3.setResponseMessage(pago.get("transaction_id"));
        message3.setSuccess(true);
        return ok(message3);
    }

    @PostMapping(value ="/updateStatusAndSubstatus")
    @ApiOperation(value = "Actualiza el estado y subestado")
    public ResponseEntity<PersistenceResponse> updateSomeFields(@RequestBody CommercialManagement obj) {
        try {
            PersistenceResponse response = commercialManagementUseCase.updateStatusAndSubstatus(obj);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("CommercialManagement", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
