package com.scfg.core.archunit;

import com.scfg.core.application.port.out.PersonPort;
import com.scfg.core.application.service.SMVSIntegrationService;
import com.scfg.core.domain.smvs.MakePaymentDTO;
import com.scfg.core.domain.smvs.PaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SMVSValidationsTest {
    @Autowired
    SMVSIntegrationService smvsIntegrationService;

    @Autowired
    PersonPort personPort;

    @Test
    void makePaymentConcurrency() {
        int count = 100;
        int initNumber = 1;

        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid).minusYears(24L);

        for(int i=0; i< count; i++) {

            MakePaymentDTO PaymentDTO = new MakePaymentDTO();
            PaymentDTO.setCod_cliente(initNumber);
            PaymentDTO.setNro_documento(initNumber + "");
            PaymentDTO.setExtension("SC");
            PaymentDTO.setComplemento("");
            PaymentDTO.setTipo_documento(1);
            PaymentDTO.setPrimer_nombre("PRUEBA");
            PaymentDTO.setSegundo_nombre("");
            PaymentDTO.setApellido_paterno("APELLIDO DE PRUEBA");
            PaymentDTO.setApellido_materno("APELLIDO DE PRUEBA");
            PaymentDTO.setApellido_casada("");
            PaymentDTO.setEstado_civil(1);
            PaymentDTO.setGenero(1);
            PaymentDTO.setFecha_nacimiento(today);
            PaymentDTO.setPais("BOLIVIA");
            PaymentDTO.setDomicilio("B/Cupesi 2020 #100");
            PaymentDTO.setTelefono_movil(78122035);
            PaymentDTO.setCorreo("gonzalo.ap.dev@gmail.com");
            PaymentDTO.setCodigo_actividad(1111);
            PaymentDTO.setActividad("");
            PaymentDTO.setProfesion("Ingeniero de Pruebas");
            PaymentDTO.setPlan_id(1L);
            PaymentDTO.setMonto_pago(90.00);
            PaymentDTO.setNro_comprobante("200");
            PaymentDTO.setFecha_pago(today);
            PaymentDTO.setId_usuario(1);
            PaymentDTO.setNombre_usuario("Pedro Murillo");
            PaymentDTO.setId_agencia(102);
            PaymentDTO.setNombre_agencia("Aroma");
            PaymentDTO.setTipo_moneda(1);
            PaymentDTO.setLugar_venta("Santa Cruz");
            PaymentDTO.setTipo_cliente(2);
            PaymentResponseDTO responseDTO = smvsIntegrationService.makePayment(PaymentDTO);
            initNumber++;
            assertTrue(responseDTO.getCodigo_respuesta() == 1);
        }


    }
}
