package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class OldMonthlyDisbursementDhnDtoSerialize extends StdSerializer<OldMonthlyDisbursementCreditLineDhnDTO> {

    public OldMonthlyDisbursementDhnDtoSerialize() {
        this(null);
    }

    protected OldMonthlyDisbursementDhnDtoSerialize(Class<OldMonthlyDisbursementCreditLineDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(OldMonthlyDisbursementCreditLineDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeStringField("USUARIO", value.getUSUARIO());
        jgen.writeStringField("NRO_POLIZA", value.getNRO_POLIZA());
        jgen.writeStringField("CONTRATANTE", value.getCONTRATANTE());
        jgen.writeStringField("CARTERA", value.getCARTERA());
        jgen.writeStringField("NOMBRE_COMPLETO", value.getNOMBRE_COMPLETO());
        jgen.writeStringField("CI", value.getCI());
        jgen.writeStringField("FECHA_NACIMIENTO", value.getFECHA_NACIMIENTO()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        jgen.writeStringField("NRO_SOLICITUD", value.getNRO_SOLICITUD());
        jgen.writeStringField("FECHA_SOLICITUD", value.getFECHA_SOLICITUD()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        jgen.writeStringField("ESTADO", value.getESTADO());
        jgen.writeStringField("ACEPTACION", value.getACEPTACION());
        jgen.writeNumberField("PORCENTAJE_EXTRAPRIMA", value.getPORCENTAJE_EXTRAPRIMA());
        jgen.writeStringField("MONEDA", value.getMONEDA());
        jgen.writeNumberField("MONTO_SOLICITADO", value.getMONTO_SOLICITADO());
        jgen.writeNumberField("MONTO_ACUMULADO", value.getMONTO_ACUMULADO());
        jgen.writeStringField("FECHA_POSICION_COMPANIA", value.getFECHA_POSICION_COMPANIA()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        jgen.writeStringField("REGIONAL", value.getREGIONAL());
        jgen.writeStringField("AGENCIA", value.getAGENCIA());
        jgen.writeStringField("ADJUNTO_SOLICITUD", value.getADJUNTO_SOLICITUD());
        jgen.writeStringField("FECHA_DJS", value.getFECHA_DJS()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        jgen.writeStringField("ADJUNTO_CERTIFICADO", value.getADJUNTO_CERTIFICADO());
        jgen.writeStringField("FECHA_CERTIFICADO", value.getFECHA_CERTIFICADO()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeNumberField("ID_REGIONAL", value.getID_REGIONAL());
            jgen.writeNumberField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeNumberField("ID_AGENCIA", value.getID_AGENCIA());
            jgen.writeNumberField("ID_SOLICITUD_SEGURO", value.getID_SOLICITUD_SEGURO());
            jgen.writeNumberField("ID_MONEDA", value.getID_MONEDA());

            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}