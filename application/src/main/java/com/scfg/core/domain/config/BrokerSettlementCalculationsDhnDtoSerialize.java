package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;

import java.io.IOException;

public class BrokerSettlementCalculationsDhnDtoSerialize extends StdSerializer<BrokerSettlementCalculationsDhnDTO> {

    public BrokerSettlementCalculationsDhnDtoSerialize() {
        this(null);
    }

    protected BrokerSettlementCalculationsDhnDtoSerialize(java.lang.Class<BrokerSettlementCalculationsDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(BrokerSettlementCalculationsDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("LINEA", value.getLINEA());
        jgen.writeObjectField("CAPITAL_ASEGURADO", value.getCAPITAL_ASEGURADO());
        jgen.writeObjectField("PRIMA", value.getPRIMA());
        jgen.writeObjectField("EXTRAPRIMA", value.getEXTRAPRIMA());
        jgen.writeObjectField("TOTAL_PRIMA", value.getTOTAL_PRIMA());
        jgen.writeObjectField("PRIMA_COMPANIA", value.getPRIMA_COMPANIA());
        jgen.writeObjectField("SERVICIO_COBRANZA", value.getSERVICIO_COBRANZA());
        jgen.writeObjectField("TOTAL_ASEGURADOS", value.getTOTAL_ASEGURADOS());
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_COBERTURA_ASEGURADOS", value.getID_COBERTURA_ASEGURADOS());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();


    }

}
