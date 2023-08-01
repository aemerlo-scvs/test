package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.*;
import com.scfg.core.application.service.SMVSAutomaticGeneratePolicy;
import com.scfg.core.application.service.SMVSGeneratePolicy;
import com.scfg.core.common.enums.SMVSResponseEnum;
import com.scfg.core.common.exception.NotFileWriteReadException;
import com.scfg.core.domain.Emailbody;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.person.Person;
import com.scfg.core.domain.smvs.*;

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

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = SMVSEndpoint.BASE)
@Api(tags = "API REST Sepelio")
public class SMVSController implements SMVSEndpoint {

    private final PersonUseCase personUseCase;

    private final SMVSCommonUseCase smvsCommonUseCase;
    private final SMVSIntegrationUseCase smvsIntegrationUseCase;
    private final ClassifierUseCase classifierUseCase;
    private final SMVSGeneratePolicy generatePolicy;
    private final SMVSAutomaticGeneratePolicy automaticGeneratePolicy;

    private final EmailPortIn emailUseCase;

    // Configuration Twilio Test
    private final SendMessageUseCase sendMessageUseCase;

    @PostMapping(value = BASE_VERIFY_ACTIVATION_CODE)
    @ApiOperation(value = "Verifica el código de activación")
    ResponseEntity verifyActivationCode(@RequestBody IngressFormDTO ingressForm) {
        try {
            SMVSResponseDTO response = smvsCommonUseCase.verifyActivationCode(ingressForm);
            return ok(response);
        } catch (Exception e) {
            log.error("Ocurrió un error al verificar el código de activación: [{}]", ingressForm, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = BASE_PARAM_PERSON_ID)
    @ApiOperation(value = "Obtiene a una persona por el Id")
    ResponseEntity getPerson(@PathVariable long personId) {
        try {
            Person person = personUseCase.getByIdWitDirections(personId);
            return ok(person);
        } catch (Exception e) {
            log.error("Ocurrió un error al obtener a la persona: [{}]", personId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = BASE_PARAM_CLASSIFIER_REFERENCE_ID)
    @ApiOperation(value = "Listar los clasificadores por tipo de clasificador (codigo de referencia")
    public ResponseEntity getAllClassifiersByReferenceId(@PathVariable long referenceId) {
        List<Classifier> classifiers = classifierUseCase.getAllClassifiersByClassifierTypeReferenceId(referenceId);
        if (classifiers.isEmpty()) {
            return CustomErrorType.notContent("Classifier Resource", "No data");
        }
        return ok(classifiers);
    }

    @PutMapping(value = BASE_PERSON)
    @ApiOperation(value = "Actualiza los datos de una persona")
    ResponseEntity updatePerson(@RequestBody Person person) {
        try {
            boolean response = smvsCommonUseCase.updatePerson(person);
            return ok(response);
        } catch (Exception e) {
            log.error("Ocurrió un error al modificar la persona: [{}]", person, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = BASE_MAKE_PAYMENT)
    @ApiOperation(value = "Realiza el pago")
    ResponseEntity makePayment(@RequestBody MakePaymentDTO paymentDTO) {
        try {
            PaymentResponseDTO paymentResponseDTO = smvsIntegrationUseCase.makePayment(paymentDTO);
            return ok(paymentResponseDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error al realizar el pago: [{}]", paymentDTO, e);
            PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                    .codigo_respuesta(SMVSResponseEnum.ERROR.getValue())
                    .codigo_activacion("")
                    .mensaje("Operación no completada, error interno del servidor: " + e.getMessage())
                    .build();
            return badRequest().body(paymentResponseDTO);
            //return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = BASE_SAVE_POLICY)
    @ApiOperation(value = "Genera la policy y devuelve el certifiacado de cobertura")
    ResponseEntity generatePolicy(@RequestBody SavePolicyDTO savePolicyDTO) {
        try {
            if (savePolicyDTO == null)
                return CustomErrorType.badRequest("Bad Request", "Objecto principal està sin datos");
            if (savePolicyDTO.getPerson() == null)
                return CustomErrorType.badRequest("Bad Request", "No se encontro datos de la pesona");
            if (savePolicyDTO.getBeneficiaryList().size() == 0)
                return CustomErrorType.badRequest("Bad Request", "No tiene beneficiarios");
            if (savePolicyDTO.getDocumentList().size() == 0)
                return CustomErrorType.badRequest("Bad Request", "No tiene los documentos personales");
            if (savePolicyDTO.getDocumentFirm() == null)
                return CustomErrorType.badRequest("Bad Request", "No tiene firma");
            FileDocumentDTO policy = generatePolicy.generatePolicy(savePolicyDTO);
            if (policy != null) {
                if (policy.getTypeId() == 1000L) {
                    return CustomErrorType.badRequest("Bad Request", "El Código de activación, ya cuenta con un certificado de cobertura activa");
                }
                return ok(policy);
            } else {
                return CustomErrorType.badRequest("Bad Request", "No se pudo generar el certificado de cobertura, conectese con el core de seguros");
            }
        } catch (NotFileWriteReadException eFile) {
            while (eFile.getCause() != null)
                eFile = (NotFileWriteReadException) eFile.getCause();
            log.error("Ocurrio el error al generar, craer el certificado de cobertura", eFile);
            return CustomErrorType.badRequest("Bad Request", eFile.getMessage());

        } catch (Exception e) {
            while (e.getCause() != null)
                e = (Exception) e.getCause();
            log.error("Ocurrió un error en generar en la Activacion del seguro", savePolicyDTO.getRequestId(), e);
            return CustomErrorType.serverError("Server Error", e.getMessage());

        }
    }

    @PostMapping(value = BASE_REVERSE_PAYMENT)
    @ApiOperation(value = "Revetir Pagos SMVS")
    ResponseEntity reversePayment(@RequestBody ReversalPaymentDTO reversalPaymentDTO) {
        try {
            ReversalPaymentResponseDTO reversalPaymentResponseDTO = smvsIntegrationUseCase.reversalPayment(reversalPaymentDTO);
            return ok(reversalPaymentResponseDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error al revertir el pago: [{}]", reversalPaymentDTO, e);
            ReversalPaymentResponseDTO responseDTO = ReversalPaymentResponseDTO.builder()
                    .codigo_respuesta(SMVSResponseEnum.ERROR.getValue())
                    .mensaje("Operación no completada, error interno del servidor: " + e.getMessage())
                    .build();
            return badRequest().body(responseDTO);
        }
    }

    @GetMapping(value = "/sendTest")
    @ApiOperation(value = "Correo de Prueba")
    public boolean EmailTest(@RequestParam String emails) throws MessagingException {
        Emailbody emailbody = new Emailbody();
        emailbody.setEmail(emails.split(";"));
        emailbody.setEmailcopy(new String[]{});
        emailbody.setContent("Email de prueba !");
        emailbody.setSubject("Email de prueba !");
        return emailUseCase.sendEmail(emailbody);
    }

    @PostMapping(value = ACTIVATE_PENDING_IN_PERIOD)
    @ApiOperation(value = "Metodo via Swagger para activar las solicitudes pendientes de un rango de tiempo")
    ResponseEntity ActivatePendingInPeriod(String fromDate, String toDate) {
        try {
            List<PendingActivateErrorDTO> list = automaticGeneratePolicy.generatePendingFromPeriod(fromDate,toDate);
            if (list != null && list.size() > 0) {
                return ok("Proceso realizado con éxito, se procesaron un total de: " + list.size() + " solicitudes");
            } else {
                return CustomErrorType.badRequest("Bad Request", "No se pudo generar el certificado de cobertura, conectese con el core de seguros");
            }
        } catch (NotFileWriteReadException eFile) {
            while (eFile.getCause() != null)
                eFile = (NotFileWriteReadException) eFile.getCause();
            log.error("Ocurrio el error al generar, craer el certificado de cobertura", eFile);
            return CustomErrorType.badRequest("Bad Request", eFile.getMessage());
        } catch (Exception e) {
            while (e.getCause() != null)
                e = (Exception) e.getCause();
            log.error("Ocurrió un error al realizar la activación del seguro en el rango de periodo", "Fechas: " + fromDate + " hasta:"  + toDate, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/send-whatsapp-test")
    @ApiOperation(value = "WhatsApp prueba")
    ResponseEntity WppTest(@RequestParam String message,@RequestParam String number, @RequestParam long docId) throws MessagingException {
        automaticGeneratePolicy.testWhatsAppSender(number, message, docId);
        boolean res = false;
        return ok("HOla mundo");
    }

    @GetMapping(value = "/download-doc")
    @ApiOperation(value = "Descarga de archivo formato")
    ResponseEntity<Resource> downloadDoc(@RequestParam long id) {
        try {
            FileDocument file = automaticGeneratePolicy.getDocument(id);
            byte[] attachment = Base64.getDecoder().decode(file.getContent().getBytes(StandardCharsets.UTF_8));
            InputStream stream = new ByteArrayInputStream(attachment);
            InputStreamResource resource = new InputStreamResource(stream);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMime()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getDescription() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error al queres descargar el documento de formato", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
