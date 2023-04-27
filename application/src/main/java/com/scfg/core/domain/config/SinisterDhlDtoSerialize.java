package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhlDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SinisterDhlDtoSerialize extends StdSerializer<SinisterDhlDTO> {

    public SinisterDhlDtoSerialize() {
        this(null);
    }

    protected SinisterDhlDtoSerialize(Class<SinisterDhlDTO> t) {
        super(t);
    }

    @Override
    public void serialize(SinisterDhlDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("NRO_SINIESTRO", value.getNRO_SINIESTRO());
        jgen.writeStringField("ASEGURADO", value.getASEGURADO());
        jgen.writeStringField("CI_ASEGURADO", value.getCI_ASEGURADO());
        jgen.writeStringField("ESTADO", value.getESTADO());
        jgen.writeStringField("FECHA_PAGO", value.getFECHA_PAGO()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeNumberField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeNumberField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
