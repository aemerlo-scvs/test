package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhnDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import static com.scfg.core.common.util.HelpersMethods.*;

public class ConsolidatedObservedCaseDhnDtoSerialize extends StdSerializer<ConsolidatedObservedCaseDhnDTO> {

    public ConsolidatedObservedCaseDhnDtoSerialize() {
        this(null);
    }

    protected ConsolidatedObservedCaseDhnDtoSerialize(Class<ConsolidatedObservedCaseDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(ConsolidatedObservedCaseDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("NRO_OPERACION", value.getNRO_OPERACION());
        jgen.writeObjectField("FECHA_DESEMBOLSO", getValueObject(value.getFECHA_DESEMBOLSO()));
        jgen.writeObjectField("CLIENTE", value.getCLIENTE());
        jgen.writeObjectField("CI", value.getCI());
        jgen.writeObjectField("DESEMBOLSOS_ANTERIORES", value.getDESEMBOLSOS_ANTERIORES());
        jgen.writeObjectField("DESEMBOLSOS_MES_ACTUAL", value.getDESEMBOLSOS_MES_ACTUAL());
        jgen.writeObjectField("getMONTO_ACUMULADO", value.getMONTO_ACUMULADO());
        jgen.writeObjectField("COMENTARIOS_MES_ACTUAL", value.getCOMENTARIOS_MES_ACTUAL());
        jgen.writeObjectField("CAPITAL_BS", value.getCAPITAL_BS());
        jgen.writeObjectField("FECHA", getValueObject(value.getFECHA()));
        jgen.writeObjectField("CONDICION", value.getCONDICION());
        jgen.writeObjectField("NRO_SOLICITUD_WEB", value.getNRO_SOLICITUD_WEB());
        jgen.writeObjectField("ESTADO_SOLICITUD", value.getESTADO_SOLICITUD());
        jgen.writeObjectField("COMENTARIOS_BROKER", value.getCOMENTARIOS_BROKER());
        jgen.writeObjectField("ESTADO", value.getESTADO());
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_CASO_OBSERVADO", value.getID_CASO_OBSERVADO());
            jgen.writeObjectField("ID_SOLICITUD", value.getID_SOLICITUD());
            jgen.writeObjectField("ID_TIPO_MONEDA", value.getID_TIPO_MONEDA());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());

            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
