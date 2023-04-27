package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static com.scfg.core.common.util.HelpersMethods.*;

public class SubscriptionTrackingDhnDtoSerialize extends StdSerializer<SubscriptionTrackingDhnDTO> {

    public SubscriptionTrackingDhnDtoSerialize() {
        this(null);
    }

    protected SubscriptionTrackingDhnDtoSerialize(Class<SubscriptionTrackingDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(SubscriptionTrackingDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("CORRELATIVO_CONTROL", value.getCORRELATIVO_CONTROL());
        jgen.writeObjectField("ITEM", value.getITEM());
        jgen.writeObjectField("FECHA_RECEPCION_DJS", getValueObject(value.getFECHA_RECEPCION_DJS()));
        jgen.writeObjectField("FECHA_LLENADO_DJS", getValueObject(value.getFECHA_LLENADO_DJS()));
        jgen.writeObjectField("NRO_DECLARACION", value.getNRO_DECLARACION());
        jgen.writeObjectField("NRO_OPERACION", value.getNRO_OPERACION());
        jgen.writeObjectField("ASEGURADO", value.getASEGURADO());
        jgen.writeObjectField("GENERO", value.getGENERO());
        jgen.writeObjectField("CI", value.getCI());
        jgen.writeObjectField("EXTENSION", value.getEXTENSION());
        jgen.writeObjectField("FECHA_NACIMIENTO", getValueObject(value.getFECHA_NACIMIENTO()));
        jgen.writeObjectField("MONEDA", value.getMONEDA());
        jgen.writeObjectField("MONTO_SOLICITADO_BS", value.getMONTO_SOLICITADO_BS());
        jgen.writeObjectField("MONTO_ACUMULADO_BS", value.getMONTO_ACUMULADO_BS());
        jgen.writeObjectField("REQUISITOS", value.getREQUISITOS());
        jgen.writeObjectField("FECHA_CUMPLIMIENTO_REQUISITOS", getValueObject(value.getFECHA_CUMPLIMIENTO_REQUISITOS()));
        jgen.writeObjectField("ESTADO", value.getESTADO());
        jgen.writeObjectField("CAUSA", value.getCAUSA());
        jgen.writeObjectField("COMENTARIOS_ADICIONALES", value.getCOMENTARIOS_ADICIONALES());
        jgen.writeObjectField("FECHA_PRONUNCIAMIENTO_AL_BANCO", getValueObject(value.getFECHA_PRONUNCIAMIENTO_AL_BANCO()));
        jgen.writeObjectField("FECHA_DESEMBOLSO", getValueObject(value.getFECHA_DESEMBOLSO()));
        jgen.writeObjectField("TIEMPO_VALIDEZ_DJS", value.getTIEMPO_VALIDEZ_DJS());
        jgen.writeObjectField("TIEMPO_RESPUESTA", value.getTIEMPO_RESPUESTA());
        jgen.writeObjectField("CARTA_EXCLUSION", value.getCARTA_EXCLUSION());
        jgen.writeObjectField("RIESGO", value.getRIESGO());
        jgen.writeObjectField("TASA_EXTRAPRIMA", value.getTASA_EXTRAPRIMA());
        jgen.writeObjectField("MOTIVO_EXTRAPRIMA", value.getMOTIVO_EXTRAPRIMA());
        jgen.writeObjectField("COBERTURA_OTORGADA", value.getCOBERTURA_OTORGADA());
        jgen.writeObjectField("DETALLE_MOTIVO", value.getDETALLE_MOTIVO());
        jgen.writeObjectField("EXAMENES_REALIZADOS", value.getEXAMENES_REALIZADOS());
        jgen.writeObjectField("OFICINA", value.getOFICINA());
        jgen.writeObjectField("SUCURSAL_OFICINA", value.getSUCURSAL_OFICINA());
        jgen.writeObjectField("GESTOR", value.getGESTOR());
        jgen.writeObjectField("TIPO_REQUERIMIENTO", value.getTIPO_REQUERIMIENTO());
        jgen.writeObjectField("FECHA_ENVIO_REASEGURO", getValueObject(value.getFECHA_ENVIO_REASEGURO()));
        jgen.writeObjectField("TIPO_OPERACION", value.getTIPO_OPERACION());
        jgen.writeObjectField("FECHA_RESPUESTA", getValueObject(value.getFECHA_RESPUESTA()));
        jgen.writeObjectField("TIEMPO_RESPUESTA_REASEGURO", value.getTIEMPO_RESPUESTA_REASEGURO());
        jgen.writeObjectField("CAPITAL_ASEGURADO_BS", value.getCAPITAL_ASEGURADO_BS());
        jgen.writeObjectField("NIVEL", value.getNIVEL());
        jgen.writeObjectField("PLAZO_CREDITO_MESES", value.getPLAZO_CREDITO_MESES());
        jgen.writeObjectField("FECHA_EMISION", getValueObject(value.getFECHA_EMISION()));
        jgen.writeObjectField("COMENTARIOS", value.getCOMENTARIOS());
        jgen.writeObjectField("PROCESO", value.getPROCESO());
        jgen.writeObjectField("FECHA_AGENDAMIENTO", getValueObject(value.getFECHA_AGENDAMIENTO()));

        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_REGIONAL", value.getID_REGIONAL());
            jgen.writeObjectField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeObjectField("ID_GESTOR", value.getID_GESTOR());
            jgen.writeObjectField("ID_TIPO_COBERTURA", value.getID_TIPO_COBERTURA());
            jgen.writeObjectField("ID_AGENCIA", value.getID_AGENCIA());
            jgen.writeObjectField("ID_SOLICITUD_SEGURO", value.getID_SOLICITUD_SEGURO());
            jgen.writeObjectField("ID_MONEDA", value.getID_MONEDA());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());

            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
