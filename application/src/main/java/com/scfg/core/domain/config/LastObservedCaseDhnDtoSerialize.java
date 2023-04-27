package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static com.scfg.core.common.util.HelpersMethods.getValueObject;

public class LastObservedCaseDhnDtoSerialize extends StdSerializer<LastObservedCaseDhnDTO> {

    public LastObservedCaseDhnDtoSerialize(){
        this(null);
    }

    protected LastObservedCaseDhnDtoSerialize(Class<LastObservedCaseDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(LastObservedCaseDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("NRO", value.getNRO());
        jgen.writeObjectField("NRO_OPERACION", value.getNRO_OPERACION());
        jgen.writeObjectField("NRO_SOLICITUD", value.getNRO_SOLICITUD());
        jgen.writeObjectField("NOMBRE", value.getNOMBRE());
        jgen.writeObjectField("NRO_CI", value.getNRO_CI());

        jgen.writeObjectField("FECHA_NACIMIENTO",getValueObject(value.getFECHA_NACIMIENTO()));
        jgen.writeObjectField("SALDO", value.getSALDO());
        jgen.writeObjectField("FECHA_DESEMBOLSO", getValueObject(value.getFECHA_DESEMBOLSO()));
        jgen.writeObjectField("CAPITAL_EXCLUIDO", value.getCAPITAL_EXCLUIDO());
        jgen.writeObjectField("TIPO_OBSERVACION", value.getTIPO_OBSERVACION());
        if (!value.getHIDDEN_RELATIONSHIP()) {

            jgen.writeObjectField("ID_TIPO_OBSERVACION", value.getID_TIPO_OBSERVACION());
            jgen.writeObjectField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeObjectField("ID_OPERACION_CREDITICIA", value.getID_OPERACION_CREDITICIA());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
