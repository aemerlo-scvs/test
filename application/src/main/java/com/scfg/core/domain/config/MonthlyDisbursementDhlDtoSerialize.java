package com.scfg.core.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import static com.scfg.core.common.util.HelpersMethods.*;

public class MonthlyDisbursementDhlDtoSerialize extends StdSerializer<MonthlyDisbursementDhlDTO> {

    public MonthlyDisbursementDhlDtoSerialize(){
        this(null);
    }

    protected MonthlyDisbursementDhlDtoSerialize(Class<MonthlyDisbursementDhlDTO> t) {
        super(t);
    }

    @Override
    public void serialize(MonthlyDisbursementDhlDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("NRO_OPERACION", value.getNRO_OPERACION());
        jgen.writeObjectField("NOMBRES", value.getNOMBRES());
        jgen.writeObjectField("APELLIDO_PATERNO", value.getAPELLIDO_PATERNO());
        jgen.writeObjectField("APELLIDO_MATERNO", value.getAPELLIDO_MATERNO());
        jgen.writeObjectField("APELLIDO_CASADA", value.getAPELLIDO_CASADA());
        jgen.writeObjectField("TIPO_DOCUMENTO", value.getTIPO_DOCUMENTO());
        jgen.writeObjectField("NRO_DOCUMENTO", value.getNRO_DOCUMENTO());
        jgen.writeObjectField("COPIA_DUPLICADO", value.getCOPIA_DUPLICADO());
        jgen.writeObjectField("EXTENSION", value.getEXTENSION());
        jgen.writeObjectField("PLAZA", value.getPLAZA());
        jgen.writeObjectField("FECHA_DESEMBOLSO", getValueObject(value.getFECHA_DESEMBOLSO()));
        jgen.writeObjectField("VALOR_ASEGURADO", value.getVALOR_ASEGURADO());
        jgen.writeObjectField("TASAX", value.getTASAX());
        jgen.writeObjectField("FECHA_NACIMIENTO", getValueObject(value.getFECHA_NACIMIENTO()));
        jgen.writeObjectField("MONTO_DESEMBOLSADO", value.getMONTO_DESEMBOLSADO());
        jgen.writeObjectField("FECHA_VENCIMIENTO", getValueObject(value.getFECHA_VENCIMIENTO()));
        jgen.writeObjectField("MONEDA", value.getMONEDA());
        jgen.writeObjectField("TIPO_CREDITO", value.getTIPO_CREDITO());
        jgen.writeObjectField("ASEGURADO", value.getASEGURADO());
        jgen.writeObjectField("COBERTURA", value.getCOBERTURA());
        jgen.writeObjectField("SEXO", value.getSEXO());
        jgen.writeObjectField("PERIODO", value.getPERIODO());
        jgen.writeObjectField("LINEA_CREDITO", value.getLINEA_CREDITO());
        jgen.writeObjectField("PLAZO_CREDITO_DIAS", value.getPLAZO_CREDITO_DIAS());
        jgen.writeObjectField("EXTRAPRIMA", value.getEXTRAPRIMA());
        jgen.writeObjectField("NACIONALIDAD", value.getNACIONALIDAD());
        jgen.writeObjectField("AGENCIA", value.getAGENCIA());
        jgen.writeObjectField("MONTO_PRIMA", value.getMONTO_PRIMA());
        jgen.writeObjectField("PAGADO_DESDE",getValueObject(value.getPAGADO_DESDE()));
        jgen.writeObjectField("PAGADO_HASTA", getValueObject(value.getPAGADO_HASTA()));
        if (!value.getHIDDEN_RELATIONSHIP()) {
            jgen.writeObjectField("ID_CLIENTE", value.getID_CLIENTE());
            jgen.writeObjectField("ID_OPERACION_CREDITICIA", value.getID_OPERACION_CREDITICIA());
            jgen.writeObjectField("ID_AGENCIA", value.getID_AGENCIA());
            jgen.writeObjectField("ID_TIPO_CREDITO", value.getID_TIPO_CREDITO());
            jgen.writeObjectField("ID_TIPO_COBERTURA", value.getID_TIPO_COBERTURA());
            jgen.writeObjectField("ID_MONEDA", value.getID_MONEDA());
            jgen.writeObjectField("ID_ITEM_DESGRAVAMEN", value.getID_ITEM_DESGRAVAMEN());
            // deprecated
            jgen.writeObjectField("ITEM_DESGRAVAMEN", value.getITEM_DESGRAVAMEN());
        }
        jgen.writeEndObject();
    }
}
