package com.scfg.core.domain.dto.vin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.exolab.castor.types.DateTime;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class BfsPaymentDTO {
    private String nro_operacion;

    private Integer tipo_documento;

    private String nro_documento;

    private String extension;

    private String complemento;

    private Long nro_comprobante;

    private Long cod_usuario;

    private String nombre_usuario;
    private String correo_usuario;

    private Long cod_oficina;

    private String oficina;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    private LocalDateTime fecha_aceptacion;
}
