package com.scfg.core.domain.person;

import com.scfg.core.domain.Telephone;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.common.Direction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "Model para crear una persona")
public class Person extends BaseDomain {

    @ApiModelProperty(notes = "NIT", example = "1002032321")
    private Long nit;

    @ApiModelProperty(notes = "Nacionalidad (viene de: Classifier)", example = "1")
    private Integer nationalityIdc;

    @ApiModelProperty(notes = "Residencia (viene de: Classifier)", example = "1")
    private Integer residenceIdc;

    @ApiModelProperty(notes = "Actividad (viene de: Classifier)", example = "1")
    private Integer activityIdc;

    @ApiModelProperty(notes = "Referencia", example = "Juan Flores")
    private String reference;

    @ApiModelProperty(notes = "Número de teléfono", example = "72156022")
    private String telephone;

    @ApiModelProperty(notes = "Listado de teléfonos")
    private List<Telephone> telephones;

    @ApiModelProperty(notes = "Correo Electrónico", example = "@gmail.com")
    private String email;

    @ApiModelProperty(notes = "Tomador (boolean)", example = "1")
    private Integer holder;

    @ApiModelProperty(notes = "Asegurado (boolean)", example = "1")
    private Integer insured;

    @ApiModelProperty(notes = "Persona natural (viene de: NaturalPerson)", example = "1")
    private NaturalPerson naturalPerson;

    @ApiModelProperty(notes = "Persona Juridica (viene de: JuridicalPerson)", example = "null")
    private JuridicalPerson juridicalPerson;

    @ApiModelProperty(notes = "Direcciones")
    private List<Direction> directions;

    @ApiModelProperty(notes = "Dirección")
    private Direction direction;

    @ApiModelProperty(notes = "Edad")
    private Double age;

    public static String getCompleteName(String name, String lastName, String motherLastName, String marriedLastName) {
        String completeName = "";
        if (name != null) completeName += name.trim();
        if (lastName != null) completeName += " " + lastName.trim();
        if (motherLastName != null) completeName += " " + motherLastName.trim();
        if (marriedLastName != null) completeName += " " + marriedLastName.trim();
        return completeName;
    }
    @Override
    public String toString() {
        return "Person{" +
                "nit=" + nit +
                ", nationalityIdc=" + nationalityIdc +
                ", residenceIdc=" + residenceIdc +
                ", activityIdc=" + activityIdc +
                ", reference='" + reference + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", holder=" + holder +
                ", insured=" + insured +
                ", naturalPerson=" + naturalPerson +
                ", juridicalPerson=" + juridicalPerson +
                ", directions=" + directions +
                ", direction=" + direction +
                ", age=" + age +
                '}';
    }
}
