package com.scfg.core.adapter.web.util;

import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ReportsGenericUseCase {

    public static List<ManualCertificateDhlDTO> getManualCertificateDHL() {
        return null;
        /*return Arrays.asList(
                ManualCertificateDhlDTO.builder()
                        .NRO_DJS_MANUAL(521L)
                        .TIPO_POLIZA("Licitada")
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_OPERACION(1L)
                        .NOMBRE("Agustin Fernando Coaquira Choque")
                        .CI("8878155")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .GENERO("M")
                        .NACIONALIDAD("Boliviana")
                        .PESO(89.0)
                        .ESTATURA(169.0)
                        .GESTOR("Liceth Quispe Apaza ")
                        .AGENCIA("La pampa")
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(20000.0)
                        .MONTO_ACUMULADO_USD(50000.0)
                        .PLAZO_CREDITO(32)
                        .DIAS_MESES_ANHOS(2)
                        .TIPO_CREDITO("Vivienda")
                        .FECHA_ACEPTACION(LocalDate.now())
                        .NRO_CERTIFICADO(521L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(45000.0)
                        .VALOR_ASEGURADO_USD(7000.0)
                        .TASAX(0.10)
                        .COBERTURA("Muerte accidental")
                        .TASA_EXTRAPRIMA(0.20)
                        .PRIMA_BS(400.0)
                        .TASA_EXTRAPRIMA_BANCO(0.02)
                        .EXTRAPRIMA_BS(200.0)

                        .build()
        );*/
    }

    public static List<ManualCertificateDhnDTO> getManualCertificateDHN() {
        return null;
        /*return Arrays.asList(
                ManualCertificateDhnDTO.builder()
                        .NRO_DJS_MANUAL(528L)
                        .TIPO_POLIZA("No Licitada")
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_OPERACION(1L)
                        .NOMBRE_COMPLETO("Guadalupe Peñarrieta")
                        .CI("258117")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .GENERO("M")
                        .NACIONALIDAD("Boliviana")
                        .PESO(89.0)
                        .ESTATURA_CM(169.0)
                        .GESTOR("Mabel Quispe Apaza ")
                        .AGENCIA("Los pozos")
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(20000.0)
                        .MONTO_ACUMULADO_USD(50000.0)
                        .PLAZO_CREDITO(32)
                        .DIAS_MESES_ANHOS(2)
                        .TIPO_CREDITO("Vivienda")
                        .FECHA_ACEPTACION(LocalDate.now())
                        .NRO_CERTIFICADO(521L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(45000.0)
                        .VALOR_ASEGURADO_USD(7000.0)
                        .TASAX(0.10)
                        .COBERTURA("Muerte accidental")
                        .TASA_EXTRAPRIMA(0.20)
                        .PRIMA_BS(708.5)
                        .TASA_EXTRAPRIMA_BANCO(0.02)
                        .EXTRAPRIMA_BS(250.5)

                        .build()
        );*/
    }


    public static List<Object> getConsolidatedObservedCaseDHL() {

        return null;
        /*return Arrays.asList(
                ConsObsCaseDHLDto.builder()
                        .NRO_OPERACION(1L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .CLIENTE("GARCIA SALAS IVON CLAUDIA")
                        .CI("1144717")
                        .DESEMBOLSOS_ANTERIORES(40430000.0)
                        .ACUMULADO(95360000.0)
                        .COMENTARIOS_MES_ACTUAL("No cuenta con seguro de nuestra compania. Operacion reportada desde agosto.") // change
                        .CAPITAL_USD(4539359.0)
                        .FECHA(LocalDate.now()) // renombrar el DTO
                        .CONDICION("")
                        .NRO_SOL_WEB("1")
                        .COMENTARIO("No se cuenta con registro de aceptaciones vigentes. Corresponde depuracion de la operacion hasta regularizar el proceso de suscripcion de riesgo.")
                        .ESTADO("DEPURAR")
                        .build(),
                ConsObsCaseDHLDto.builder()
                        .NRO_OPERACION(1L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .CLIENTE("FERNANDEZ ALIAGA PEDRO")
                        .CI("1144717")
                        .DESEMBOLSOS_ANTERIORES(40430000.0)
                        .ACUMULADO(95360000.0)
                        .COMENTARIOS_MES_ACTUAL("No cuenta con seguro de nuestra compania. Operacion reportada desde agosto.") // change
                        .CAPITAL_USD(4539359.0)
                        .FECHA(LocalDate.now()) // renombrar el DTO
                        .CONDICION("")
                        .NRO_SOL_WEB("1")
                        .COMENTARIO("No se cuenta con registro de aceptaciones vigentes. Corresponde depuracion de la operacion hasta regularizar el proceso de suscripcion de riesgo.")
                        .ESTADO("DEPURAR")
                        .build()
        );*/
    }

    public static List<Object> getConsolidatedObservedCaseDHN() {
        return null;

        /*return Arrays.asList(
                ConsObsCaseDHNDto.builder()
                        .NRO_OPERACION(1L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .CLIENTE("GONZALES MARTINES ALFONSO")
                        .CI("1100717")
                        .DESEMBOLSOS_ANTERIORES(40430000.0)
                        .MONTO_ACUMULADO(95360000.0)
                        .COMENTARIOS_MES_ACTUAL("No cuenta con seguro de nuestra compania. Operacion reportada desde agosto.") // change
                        .CAPITAL_USD(4539359.0)
                        .FECHA(LocalDate.now()) // renombrar el DTO
                        .CONDICION("")
                        .NRO_SOLICITUD_WEB("1")
                        .COMENTARIOS_BROKER("No se cuenta con registro de aceptaciones vigentes. Corresponde depuracion de la operacion hasta regularizar el proceso de suscripcion de riesgo.")
                        .ESTADO("DEPURAR")
                        .build(),
                ConsObsCaseDHNDto.builder()
                        .NRO_OPERACION(1L)
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .CLIENTE("RAUL JIMENEZ GONZALES")
                        .CI("113242717")
                        .DESEMBOLSOS_ANTERIORES(40430000.0)
                        .MONTO_ACUMULADO(95360000.0)
                        .COMENTARIOS_MES_ACTUAL("No cuenta con seguro de nuestra compania. Operacion reportada desde agosto.") // change
                        .CAPITAL_USD(4539359.0)
                        .FECHA(LocalDate.now()) // renombrar el DTO
                        .CONDICION("")
                        .NRO_SOLICITUD_WEB("1")
                        .COMENTARIOS_BROKER("No se cuenta con registro de aceptaciones vigentes. Corresponde depuracion de la operacion hasta regularizar el proceso de suscripcion de riesgo.")
                        .ESTADO("DEPURAR")
                        .build()
        );*/
    }

    public static List<MonthlyDisbursementDhlDTO> getMonthlyDisbursementDHL() {
        return Arrays.asList(
                MonthlyDisbursementDhlDTO.builder()
                        .NRO_OPERACION(1L)
                        .NOMBRES("PEDRO FERNANDO")
                        .APELLIDO_PATERNO("PAYO")
                        .APELLIDO_MATERNO("NALLAR")
                        .APELLIDO_CASADA("dsa")
                        .TIPO_DOCUMENTO("I")
                        .NRO_DOCUMENTO("2851214")
                        .COPIA_DUPLICADO("")
                        .EXTENSION("SC")
                        .PLAZA("SANTA CRUZ DE LA SIERRA")
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(891800.00)
                        .TASAX(1.548)
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONTO_DESEMBOLSADO(891800.0)
                        .FECHA_VENCIMIENTO(LocalDate.now())
                        .MONEDA("")
                        .TIPO_CREDITO("PYMES")
                        .ASEGURADO("DEUDOR")
                        .COBERTURA("Titular")
                        .SEXO("Masculino")
                        .PERIODO("6=Semestral")
                        .LINEA_CREDITO("SI")
                        .PLAZO_CREDITO_DIAS(406)
                        .EXTRAPRIMA(0.0)
                        .NACIONALIDAD("BO")
                        .AGENCIA("NORTE")
                        .MONTO_PRIMA(1188.77)
                        .PAGADO_DESDE(LocalDate.now())
                        .PAGADO_HASTA(LocalDate.now())
                        .build(),
                MonthlyDisbursementDhlDTO.builder()
                        .NRO_OPERACION(1L)
                        .NOMBRES("MARTIN RIBERA")
                        .APELLIDO_PATERNO("AGUILAR")
                        .APELLIDO_MATERNO("NALLAR")
                        .APELLIDO_CASADA("dsa")
                        .TIPO_DOCUMENTO("I")
                        .NRO_DOCUMENTO("42151214")
                        .COPIA_DUPLICADO("")
                        .EXTENSION("SC")
                        .PLAZA("SANTA CRUZ DE LA SIERRA")
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(891800.00)
                        .TASAX(1.548)
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONTO_DESEMBOLSADO(891800.0)
                        .FECHA_VENCIMIENTO(LocalDate.now())
                        .MONEDA("4")
                        .TIPO_CREDITO("PYMES")
                        .ASEGURADO("DEUDOR")
                        .COBERTURA("Titular")
                        .SEXO("Masculino")
                        .PERIODO("6=Semestral")
                        .LINEA_CREDITO("SI")
                        .PLAZO_CREDITO_DIAS(406)
                        .EXTRAPRIMA(0.0)
                        .NACIONALIDAD("BO")
                        .AGENCIA("NORTE")
                        .MONTO_PRIMA(1188.77)
                        .PAGADO_DESDE(LocalDate.now())
                        .PAGADO_HASTA(LocalDate.now())
                        .build()
        );
    }

    public static List<MonthlyDisbursementDhnDTO> getMonthlyDisbursementDHN() {
        return Arrays.asList(
                MonthlyDisbursementDhnDTO.builder()
                        .NRO_OPERACION(1L)
                        .NOMBRES("PEDRO FERNANDO")
                        .APELLIDO_PATERNO("PAYO")
                        .APELLIDO_MATERNO("NALLAR")
                        .APELLIDO_CASADA("dsa")
                        .TIPO_DOCUMENTO("I")
                        .NRO_DOCUMENTO("2851214")
                        .COPIA_DUPLICADO("")
                        .EXTENSION("SC")
                        .PLAZA("SANTA CRUZ DE LA SIERRA")
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(891800.00)
                        .TASA_PRIMA(1.548)
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONTO_DESEMBOLSADO(891800.0)
                        .FECHA_VENCIMIENTO(LocalDate.now())
                        .MONEDA("2")
                        .TIPO_CREDITO("PYMES")
                        .ASEGURADO("DEUDOR")
                        .COBERTURA("Titular")
                        .TIPO_COBERTURA("Normal")
                        .SEXO("Masculino")
                        .PERIODO("6=Semestral")
                        .LINEA_CREDITO("SI")
                        .PLAZO_CREDITO(406)
                        .TASA_EXTRAPRIMA_BANCO(0.5)
                        .TASA_EXTRAPRIMA_SCVS(0.45)
                        .NACIONALIDAD("BO")
                        .AGENCIA("NORTE")
                        .PRIMA_BS(1188.77)
                        .TASA_EXTRAPRIMA(893.9)
                        .EXTRAPRIMA_BS(25.67)
                        .build(),
                MonthlyDisbursementDhnDTO.builder()
                        .NRO_OPERACION(1L)
                        .NOMBRES("MARCELA TRUJILLO")
                        .APELLIDO_PATERNO("PAYO")
                        .APELLIDO_MATERNO("NALLAR")
                        .APELLIDO_CASADA("dsa")
                        .TIPO_DOCUMENTO("I")
                        .NRO_DOCUMENTO("2851214")
                        .COPIA_DUPLICADO("")
                        .EXTENSION("SC")
                        .PLAZA("SANTA CRUZ DE LA SIERRA")
                        .FECHA_DESEMBOLSO(LocalDate.now())
                        .VALOR_ASEGURADO(891800.00)
                        .TASA_PRIMA(1.548)
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONTO_DESEMBOLSADO(891800.0)
                        .FECHA_VENCIMIENTO(LocalDate.now())
                        .MONEDA("3")
                        .TIPO_CREDITO("PYMES")
                        .ASEGURADO("DEUDOR")
                        .COBERTURA("Titular")
                        .TIPO_COBERTURA("Normal")
                        .SEXO("Masculino")
                        .PERIODO("6=Semestral")
                        .LINEA_CREDITO("SI")
                        .PLAZO_CREDITO(406)
                        .TASA_EXTRAPRIMA_BANCO(0.5)
                        .TASA_EXTRAPRIMA_SCVS(0.45)
                        .NACIONALIDAD("BO")
                        .AGENCIA("NORTE")
                        .PRIMA_BS(1188.77)
                        .TASA_EXTRAPRIMA(893.9)
                        .EXTRAPRIMA_BS(25.67)
                        .build()
        );
    }

    public static List<OldMonthlyDisbursementCreditLineDhnDTO> getOldMonthlyDisbursementCreditLineDHN() {
        return Arrays.asList(
                OldMonthlyDisbursementCreditLineDhnDTO.builder()
                        .USUARIO("MORENO CARREÑO RUDDY ALFREDO")
                        .NRO_POLIZA("POL-DHB-SC-000018-2017-00")
                        .CONTRATANTE("BANCO FASSIL S.A.")
                        .CARTERA("DEUDOR, CODEUDOR O GARANTE")
                        .NOMBRE_COMPLETO("JENNY IRALA FLORES")
                        .CI("9713658")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .NRO_SOLICITUD("2457L")
                        .FECHA_SOLICITUD(LocalDate.now())
                        .ESTADO("Anulado")
                        .ACEPTACION("")
                        .PORCENTAJE_EXTRAPRIMA(0.0)
                        .MONEDA("BOB")
                        .MONTO_SOLICITADO(343000.00)
                        .MONTO_ACUMULADO(343000.00)
                        .FECHA_POSICION_COMPANIA(LocalDate.now())
                        .REGIONAL("Santa Cruz")
                        .AGENCIA("EL CARMEN")
                        .ADJUNTO_SOLICITUD("NO")
                        .FECHA_DJS(null)
                        .ADJUNTO_CERTIFICADO("NO")
                        .FECHA_CERTIFICADO(null)
                        .build(),
                OldMonthlyDisbursementCreditLineDhnDTO.builder()
                        .USUARIO("FAUSTINO PEREIRA ROJAS")
                        .NRO_POLIZA("POL-DHB-SC-000018-2017-00")
                        .CONTRATANTE("BANCO FASSIL S.A.")
                        .CARTERA("DEUDOR, CODEUDOR O GARANTE")
                        .NOMBRE_COMPLETO("JENNY IRALA FLORES")
                        .CI("5224714")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .NRO_SOLICITUD("2457L")
                        .FECHA_SOLICITUD(LocalDate.now())
                        .ESTADO("Aceptadp")
                        .ACEPTACION("SI")
                        .PORCENTAJE_EXTRAPRIMA(0.0)
                        .MONEDA("BOB")
                        .MONTO_SOLICITADO(343000.00)
                        .MONTO_ACUMULADO(343000.00)
                        .FECHA_POSICION_COMPANIA(LocalDate.now())
                        .REGIONAL("Santa Cruz")
                        .AGENCIA("EL CARMEN")
                        .ADJUNTO_SOLICITUD("NO")
                        .FECHA_DJS(null)
                        .ADJUNTO_CERTIFICADO("NO")
                        .FECHA_CERTIFICADO(null)
                        .build()
        );
    }

    public static List<SubscriptionTrackingDhlDTO> getSubscriptionTrackingDHL() {

        return null;

        /*return Arrays.asList(
                SubscriptionTrackingDhlDTO.builder()
                        .CORRELATIVO_CONTROL(4)
                        .ITEM("4")
                        .FECHA_RECEPCION_DJS_REQUERIMIENTO(LocalDate.now())
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_DECLARACION("46281")
                        .ASEGURADO("ESPEJO SANTANDER, ONASIS")
                        .GENERO("M")
                        .CEDULA_IDENTIDAD("3424304")
                        .EX("LP")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(1979900.05)
                        .MONTO_ACUMULADO_USD(288615.1)
                        .REQUISITOS("DS+EM+AO+LAB+HIV+ECG")
                        .FECHA_CUMPLIMIENTO_REQUISITOS(null)
                        .ESTADO("Atendido")
                        .CAUSA("A la espera del certificado individual firmado")
                        .COMENTARIOS_ADICIONALES("Obesidad")
                        .FECHA_PRONUNCIAMIENTO_AL_BANCO(null)
                        .FECHA_DESEMBOLSO(null)
                        .TIEMPO_VALIDEZ_DJS(2)
                        .TIEMPO_RESPUESTA(22)
                        .CARTA_RECHAZO_EXCLUSION("NO")
                        .RIESGO("Aceptado con extraprima")
                        .PORCENTAJE_EXTRAPRIMA(0.75)
                        .MOTIVO_EXTRAPRIMA("obesidad tipo II")
                        .COBERTURA_OTORGADA("Cobertura Total")
                        .DETALLE_MOTIVO("")
                        .EXAMENES_REALIZADOS("DS+EM+AO+LAB+HIV+ECG")
                        .OFICINA("LA PAZ")
                        .SUCURSAL_OFICINA("AGENCIA SATELITE")
                        .GESTOR("ANGELA RITA ALEJO YUJRA")
                        .TIPO_REQUERIMIENTO("Nuevo")
                        .FECHA_ENVIO_REASEGURO(null)
                        .TIPO_OPERACION("Automatica")
                        .FECHA_RESPUESTA(null)
                        .TIEMPO_RESPUESTA_REASEGURO(null)
                        .CAPITAL_ASEGURADO_USD(88615.17)
                        .NIVEL("De 1 a 300.000")
                        .PLAZO_CREDITO_MESES(120)
                        .FECHA_EMISION(null)
                        .COMENTARIOS("")
                        .PROCESO("")
                        .ID("24388")
                        .FECHA_AGENDAMIENTO(null)
                        .CARTERA("NO REGULADO")
                        .build(),
                SubscriptionTrackingDhlDTO.builder()
                        .CORRELATIVO_CONTROL(4)
                        .ITEM("4")
                        .FECHA_RECEPCION_DJS_REQUERIMIENTO(LocalDate.now())
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_DECLARACION("46281")
                        .ASEGURADO("AGUSTIONO SALAZAR, TRUJILLO")
                        .GENERO("M")
                        .CEDULA_IDENTIDAD("3424304")
                        .EX("LP")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(1979900.05)
                        .MONTO_ACUMULADO_USD(288615.1)
                        .REQUISITOS("DS+EM+AO+LAB+HIV+ECG")
                        .FECHA_CUMPLIMIENTO_REQUISITOS(null)
                        .ESTADO("Atendido")
                        .CAUSA("A la espera del certificado individual firmado")
                        .COMENTARIOS_ADICIONALES("Obesidad")
                        .FECHA_PRONUNCIAMIENTO_AL_BANCO(null)
                        .FECHA_DESEMBOLSO(null)
                        .TIEMPO_VALIDEZ_DJS(2)
                        .TIEMPO_RESPUESTA(22)
                        .CARTA_RECHAZO_EXCLUSION("NO")
                        .RIESGO("Aceptado con extraprima")
                        .PORCENTAJE_EXTRAPRIMA(0.75)
                        .MOTIVO_EXTRAPRIMA("obesidad tipo II")
                        .COBERTURA_OTORGADA("Cobertura Total")
                        .DETALLE_MOTIVO("")
                        .EXAMENES_REALIZADOS("DS+EM+AO+LAB+HIV+ECG")
                        .OFICINA("LA PAZ")
                        .SUCURSAL_OFICINA("AGENCIA SATELITE")
                        .GESTOR("ANGELA RITA ALEJO YUJRA")
                        .TIPO_REQUERIMIENTO("Nuevo")
                        .FECHA_ENVIO_REASEGURO(null)
                        .TIPO_OPERACION("Automatica")
                        .FECHA_RESPUESTA(null)
                        .TIEMPO_RESPUESTA_REASEGURO(null)
                        .CAPITAL_ASEGURADO_USD(88615.17)
                        .NIVEL("De 1 a 300.000")
                        .PLAZO_CREDITO_MESES(120)
                        .FECHA_EMISION(null)
                        .COMENTARIOS("")
                        .PROCESO("")
                        .ID("24388")
                        .FECHA_AGENDAMIENTO(null)
                        .CARTERA("NO REGULADO")
                        .build()

        );*/
    }

    public static List<SubscriptionTrackingDhnDTO> getSubscriptionTrackingDHN() {

        return null;


        /*return Arrays.asList(
                SubscriptionTrackingDhnDTO.builder()
                        .CORRELATIVO_CONTROL(4)
                        .ITEM("4")
                        .FECHA_RECEPCION_DJS(LocalDate.now())
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_DECLARACION("46281")
                        .ASEGURADO("ESPEJO SANTANDER, ONASIS")
                        .GENERO("M")
                        .CI("3424304")
                        .EXTENSION("LP")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(1979900.05)
                        .MONTO_ACUMULADO_USD(288615.1)
                        .REQUISITOS("DS+EM+AO+LAB+HIV+ECG")
                        .FECHA_CUMPLIMIENTO_REQUISITOS(null)
                        .ESTADO("Atendido")
                        .CAUSA("A la espera del certificado individual firmado")
                        .COMENTARIOS_ADICIONALES("Obesidad")
                        .FECHA_PRONUNCIAMIENTO_AL_BANCO(null)
                        .FECHA_DESEMBOLSO(null)
                        .TIEMPO_VALIDEZ_DJS(2)
                        .TIEMPO_RESPUESTA(22)
                        .CARTA_EXCLUSION("NO")
                        .RIESGO("Aceptado con extraprima")
                        .TASA_EXTRAPRIMA(0.75)
                        .MOTIVO_EXTRAPRIMA("obesidad tipo II")
                        .COBERTURA_OTORGADA("Cobertura Total")
                        .DETALLE_MOTIVO("")
                        .EXAMENES_REALIZADOS("DS+EM+AO+LAB+HIV+ECG")
                        .OFICINA("LA PAZ")
                        .SUCURSAL_OFICINA("AGENCIA SATELITE")
                        .GESTOR("ANGELA RITA ALEJO YUJRA")
                        .TIPO_REQUERIMIENTO("Nuevo")
                        .FECHA_ENVIO_REASEGURO(null)
                        .TIPO_OPERACION("Automatica")
                        .FECHA_RESPUESTA(null)
                        .TIEMPO_RESPUESTA_REASEGURO(null)
                        .CAPITAL_ASEGURADO_USD(88615.17)
                        .NIVEL("De 1 a 300.000")
                        .PLAZO_CREDITO_MESES(120)
                        .FECHA_EMISION(null)
                        .COMENTARIOS("")
                        .PROCESO("")
                        .ID("24388")
                        .FECHA_AGENDAMIENTO(null)
                        .CARTERA("NO REGULADO")
                        .build(),

                SubscriptionTrackingDhnDTO.builder()
                        .CORRELATIVO_CONTROL(4)
                        .ITEM("4")
                        .FECHA_RECEPCION_DJS(LocalDate.now())
                        .FECHA_LLENADO_DJS(LocalDate.now())
                        .NRO_DECLARACION("46281")
                        .ASEGURADO("ESPEJO SANTANDER, ONASIS")
                        .GENERO("M")
                        .CI("3424304")
                        .EXTENSION("LP")
                        .FECHA_NACIMIENTO(LocalDate.now())
                        .MONEDA("BOLIVIANOS")
                        .MONTO_SOLICITADO_BS(1979900.05)
                        .MONTO_ACUMULADO_USD(288615.1)
                        .REQUISITOS("DS+EM+AO+LAB+HIV+ECG")
                        .FECHA_CUMPLIMIENTO_REQUISITOS(null)
                        .ESTADO("Atendido")
                        .CAUSA("A la espera del certificado individual firmado")
                        .COMENTARIOS_ADICIONALES("Obesidad")
                        .FECHA_PRONUNCIAMIENTO_AL_BANCO(null)
                        .FECHA_DESEMBOLSO(null)
                        .TIEMPO_VALIDEZ_DJS(2)
                        .TIEMPO_RESPUESTA(22)
                        .CARTA_EXCLUSION("NO")
                        .RIESGO("Aceptado con extraprima")
                        .TASA_EXTRAPRIMA(0.75)
                        .MOTIVO_EXTRAPRIMA("obesidad tipo II")
                        .COBERTURA_OTORGADA("Cobertura Total")
                        .DETALLE_MOTIVO("")
                        .EXAMENES_REALIZADOS("DS+EM+AO+LAB+HIV+ECG")
                        .OFICINA("LA PAZ")
                        .SUCURSAL_OFICINA("AGENCIA SATELITE")
                        .GESTOR("ANGELA RITA ALEJO YUJRA")
                        .TIPO_REQUERIMIENTO("Nuevo")
                        .FECHA_ENVIO_REASEGURO(null)
                        .TIPO_OPERACION("Automatica")
                        .FECHA_RESPUESTA(null)
                        .TIEMPO_RESPUESTA_REASEGURO(null)
                        .CAPITAL_ASEGURADO_USD(88615.17)
                        .NIVEL("De 1 a 300.000")
                        .PLAZO_CREDITO_MESES(120)
                        .FECHA_EMISION(null)
                        .COMENTARIOS("")
                        .PROCESO("")
                        .ID("24388")
                        .FECHA_AGENDAMIENTO(null)
                        .CARTERA("NO REGULADO")
                        .build()
        );*/
    }
}
