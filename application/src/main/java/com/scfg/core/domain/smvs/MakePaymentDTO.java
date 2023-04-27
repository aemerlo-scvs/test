package com.scfg.core.domain.smvs;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "DTO para realizar pagos del producto de sepelio")
public class MakePaymentDTO {

    @ApiModelProperty(notes = "Código Cliente", example = "123456", value = "123456", required = true)
    private Integer cod_cliente;

    @ApiModelProperty(notes = "Número de Documento de Identificación", example = "6050442", value = "6050442", required = true)
    private String nro_documento;

    @ApiModelProperty(notes = "Extensión", example = "SC", value = "SC", allowableValues = "SC, LP, CB, TJ, PO, BE,OR, CH, PD, EXT", required = true)
    private String extension;

    @ApiModelProperty(notes = "Complemento", example = ".", value = ".")
    private String complemento;

    @ApiModelProperty(notes = "Tipo de Documento de Identificación", example = "1", value = "1", allowableValues = "1, 2, 3, 4, 5", required = true)
    private Integer tipo_documento;

    @ApiModelProperty(notes = "Primer Nombre", example = "Juan", value = "Juan", required = true)
    private String primer_nombre;

    @ApiModelProperty(notes = "Segundo Nombre", example = ".", value = ".")
    private String segundo_nombre;

    @ApiModelProperty(notes = "Apellido Paterno (Al menos se debe tener un apellido)", example = "Pérez", value = "Pérez")
    private String apellido_paterno;

    @ApiModelProperty(notes = "Apellido Materno (Al menos se debe tener un apellido)", example = "Choque", value = "Choque")
    private String apellido_materno;

    @ApiModelProperty(notes = "Apellido de Casada (Al menos se debe tener un apellido)", example = ".", value = ".")
    private String apellido_casada;

    @ApiModelProperty(notes = "Estado Civil (*, si es cliente del banco)", example = "1", value = "1", allowableValues = "1, 2, 3, 4", required = true)
    private Integer estado_civil;

    @ApiModelProperty(notes = "Género (*, si es cliente del banco)", example = "1", value = "1", allowableValues = "1, 2")
    private Integer genero;

    @ApiModelProperty(notes = "Fecha de Nacimiento (*, si es cliente del banco)", example = "1997-12-08T00:00:00", value = "1997-12-08T00:00:00")
    private LocalDateTime fecha_nacimiento;

    @ApiModelProperty(notes = "Pais (*, si es cliente del banco)", example = "BOLIVIA", value = "BOLIVIA")
    private String pais;

    @ApiModelProperty(notes = "Domicilio (*, si es cliente del banco)", example = "B/Villa Fátima #100", value = "B/Villa Fátima #100")
    private String domicilio;

    @ApiModelProperty(notes = "Télefono Movil (*, si es cliente del banco)", example = "78223065", value = "78223065")
    private Integer telefono_movil;

    @ApiModelProperty(notes = "Correo Electrónico (*, si es cliente del banco)", example = "juan.perez@gmail.com", value = "juan.perez@gmail.com")
    private String correo;

    @ApiModelProperty(notes = "Código de Actividad (*, si es cliente del banco)", example = "1111", value = "1111")
    private Integer codigo_actividad;

    @ApiModelProperty(notes = "Actividad (*, si es cliente del banco)", example = "Cultivo de cereales", value = "Cultivo de cereales")
    private String actividad;

    @ApiModelProperty(notes = "Profesión (*, si es cliente del banco)", example = "Ingeniero en Sistemas", value = "Ingeniero en Sistemas")
    private String profesion;

    @ApiModelProperty(notes = "Id Plan", example = "1", value = "1", required = true)
    private Long plan_id;

    @ApiModelProperty(notes = "Monto de Pago", example = "90", value = "90", required = true)
    private Double monto_pago;

    @ApiModelProperty(notes = "Número de Comprobante", example = "1011100", value = "1011100", required = true)
    private String nro_comprobante;

    @ApiModelProperty(notes = "Fecha de Pago", example = "2021-06-08T08:20:00", value = "2021-06-08T08:20:00", required = true)
    private LocalDateTime fecha_pago;

    @ApiModelProperty(notes = "Id Usuario", example = "1", value = "1", required = true)
    private Integer id_usuario;

    @ApiModelProperty(notes = "Nombre Usuario", example = "Manuel Pérez", value = "Manuel Pérez", required = true)
    private String nombre_usuario;

    @ApiModelProperty(notes = "Id Agencia", example = "102", value = "102", required = true)
    private Integer id_agencia;

    @ApiModelProperty(notes = "Nombre Agencia", example = "Aroma", value = "Aroma", required = true)
    private String nombre_agencia;

    @ApiModelProperty(notes = "Tipo Moneda", example = "1", value = "1", allowableValues = "1, 2",required = true)
    private Integer tipo_moneda;

    @ApiModelProperty(notes = "Lugar de Venta", example = "Santa Cruz", value = "Santa cruz", allowableValues = "Santa Cruz, La Paz, Beni, Cochabamba, Sucre, Oruro, Potosí, Tarija, Pando",required = true)
    private String lugar_venta;

    @ApiModelProperty(notes = "Tipo Cliente", example = "2", value = "2", allowableValues = "1, 2",required = true)
    private Integer tipo_cliente;

}
