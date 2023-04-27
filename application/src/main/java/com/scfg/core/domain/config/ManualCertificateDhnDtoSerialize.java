package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhnDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import static com.scfg.core.common.util.HelpersMethods.*;

public class ManualCertificateDhnDtoSerialize extends StdSerializer<ManualCertificateDhnDTO> {

    public ManualCertificateDhnDtoSerialize(){
        this(null);
    }

    protected ManualCertificateDhnDtoSerialize(Class<ManualCertificateDhnDTO> t) {
        super(t);
    }

    @Override
    public void serialize(ManualCertificateDhnDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("NRO_DJS_MANUAL", value.getNRO_DJS_MANUAL());
        jgen.writeObjectField("TIPO_POLIZA", value.getTIPO_POLIZA());
        jgen.writeObjectField("FECHA_LLENADO_DJS", getValueObject(value.getFECHA_LLENADO_DJS()));
        jgen.writeObjectField("NRO_OPERACION", value.getNRO_OPERACION());
        jgen.writeObjectField("NOMBRE_COMPLETO", value.getNOMBRE_COMPLETO());
        jgen.writeObjectField("CI", value.getCI());
        jgen.writeObjectField("FECHA_NACIMIENTO", getValueObject(value.getFECHA_NACIMIENTO()));
        jgen.writeObjectField("GENERO", value.getGENERO());
        jgen.writeObjectField("NACIONALIDAD", value.getNACIONALIDAD());
        jgen.writeObjectField("PESO", value.getPESO());
        jgen.writeObjectField("ESTATURA_CM", value.getESTATURA_CM());
        jgen.writeObjectField("GESTOR", value.getGESTOR());
        jgen.writeObjectField("AGENCIA", value.getAGENCIA());
        jgen.writeObjectField("MONEDA", value.getMONEDA());
        jgen.writeObjectField("MONTO_SOLICITADO_BS", value.getMONTO_SOLICITADO_BS());
        jgen.writeObjectField("MONTO_ACUMULADO_USD", value.getMONTO_ACUMULADO_BS());
        jgen.writeObjectField("PLAZO_CREDITO", value.getPLAZO_CREDITO());
        jgen.writeObjectField("DIAS_MESES_ANHOS", value.getDIAS_MESES_ANHOS());
        jgen.writeObjectField("TIPO_CREDITO", value.getTIPO_CREDITO());
        jgen.writeObjectField("FECHA_ACEPTACION", getValueObject(value.getFECHA_ACEPTACION()));
        jgen.writeObjectField("NRO_CERTIFICADO", value.getNRO_CERTIFICADO());
        jgen.writeObjectField("ESTADO_SOLICITUD", value.getESTADO_SOLICITUD());
        jgen.writeObjectField("FECHA_DESEMBOLSO", getValueObject(value.getFECHA_DESEMBOLSO()));
        jgen.writeObjectField("VALOR_ASEGURADO", value.getVALOR_ASEGURADO());
        jgen.writeObjectField("VALOR_ASEGURADO_USD", value.getVALOR_ASEGURADO());
        jgen.writeObjectField("TASAX", value.getTASAX());
        jgen.writeObjectField("COBERTURA", value.getCOBERTURA());
        jgen.writeObjectField("TASA_EXTRAPRIMA", value.getTASA_EXTRAPRIMA());
        jgen.writeObjectField("PRIMA_BS", value.getPRIMA_BS());
        jgen.writeObjectField("TASA_EXTRAPRIMA_BANCO", value.getCOBERTURA());
        jgen.writeObjectField("EXTRAPRIMA_BS", value.getEXTRAPRIMA_BS());
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_SOLICITUD_SEGURO", value.getID_SOLICITUD_SEGURO());
            jgen.writeObjectField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeObjectField("ID_SOLICITUD_SEGURO", value.getID_SOLICITUD_SEGURO());
            jgen.writeObjectField("ID_GESTOR", value.getID_GESTOR());
            jgen.writeObjectField("ID_MONEDA", value.getID_MONEDA());
            jgen.writeObjectField("ID_COBERTURA", value.getID_COBERTURA());
            jgen.writeObjectField("ID_TIPO_CREDITO", value.getID_TIPO_CREDITO());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
