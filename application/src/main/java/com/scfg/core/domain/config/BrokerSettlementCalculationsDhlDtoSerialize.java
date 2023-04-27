package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;

import java.io.IOException;

public class BrokerSettlementCalculationsDhlDtoSerialize extends StdSerializer<BrokerSettlementCalculationsDhlDTO> {

    public BrokerSettlementCalculationsDhlDtoSerialize(){
        this(null);
    }

    protected BrokerSettlementCalculationsDhlDtoSerialize(Class<BrokerSettlementCalculationsDhlDTO> t) {
        super(t);
    }

    @Override
    public void serialize(BrokerSettlementCalculationsDhlDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("ITEMS", value.getITEMS());
        jgen.writeObjectField("VALOR_ASEGURADO", value.getVALOR_ASEGURADO());
        jgen.writeObjectField("COM_FASSIL", value.getCOM_FASSIL());
        jgen.writeObjectField("CANT_ASEGURADOS", value.getCANT_ASEGURADOS());
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_COBERTURA_ASEGURADOS", value.getID_COBERTURA_ASEGURADOS());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
