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

import java.lang.reflect.Field;
import java.util.Date;
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

    public Person(Long id, Integer nationalityIdc, Integer residenceIdc, Integer activityIdc, String reference, String telephone,
                  String email, Integer holder, Integer insured, Date createdAt, Date lastModifiedAt, Long createdBy, Long lastModifiedBy,
                  Object naturalPerson, Object juridicalPerson) throws NoSuchFieldException, IllegalAccessException {
        this.setId(id);
        this.setNationalityIdc(nationalityIdc);
        this.setResidenceIdc(residenceIdc);
        this.setActivityIdc(activityIdc);
        this.setReference(reference);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setHolder(holder);
        this.setInsured(insured);
        this.setCreatedAt(createdAt);
        this.setLastModifiedAt(lastModifiedAt);
        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);

        if (naturalPerson != null) {
            NaturalPersonDomain naturalPersonDomain = new NaturalPersonDomain();
            copyObject(naturalPerson, naturalPersonDomain);
            this.setNit(naturalPersonDomain.getNit());
            this.setNaturalPerson(new NaturalPerson(naturalPersonDomain));
        }

        if (juridicalPerson != null) {
            JuridicalPersonDomain juridicalPersonDomain = new JuridicalPersonDomain();
            copyObject(juridicalPerson, juridicalPersonDomain);
            this.setNit(juridicalPersonDomain.getNit());
            this.setJuridicalPerson(new JuridicalPerson(juridicalPersonDomain));
        }

    }

    public static void copyObject(Object src, Object dest)
            throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException {
        for (Field field : src.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Field fieldDest = dest.getClass().getDeclaredField(field.getName());
            fieldDest.setAccessible(true);
            if (!fieldDest.getName().equals("person"))
                fieldDest.set(dest, field.get(src));
        }
        Field idFieldSrc = src.getClass().getSuperclass().getDeclaredField("id");
        Field createdAtFieldSrc = src.getClass().getSuperclass().getDeclaredField("createdAt");
        Field lastModifiedAtFieldSrc = src.getClass().getSuperclass().getDeclaredField("lastModifiedAt");
        Field createdByFieldSrc = src.getClass().getSuperclass().getDeclaredField("createdBy");
        Field lastModifiedByFieldSrc = src.getClass().getSuperclass().getDeclaredField("lastModifiedBy");

        idFieldSrc.setAccessible(true);
        createdAtFieldSrc.setAccessible(true);
        lastModifiedAtFieldSrc.setAccessible(true);
        createdByFieldSrc.setAccessible(true);
        lastModifiedByFieldSrc.setAccessible(true);

        Field idFieldDest = dest.getClass().getDeclaredField(idFieldSrc.getName());
        Field createdAtFieldDest = dest.getClass().getDeclaredField(createdAtFieldSrc.getName());
        Field lastModifiedAtFieldDest = dest.getClass().getDeclaredField(lastModifiedAtFieldSrc.getName());
        Field createdByFieldDest = dest.getClass().getDeclaredField(createdByFieldSrc.getName());
        Field lastModifiedByFieldDest = dest.getClass().getDeclaredField(lastModifiedByFieldSrc.getName());

        idFieldDest.setAccessible(true);
        createdAtFieldDest.setAccessible(true);
        lastModifiedAtFieldDest.setAccessible(true);
        createdByFieldDest.setAccessible(true);
        lastModifiedByFieldDest.setAccessible(true);

        idFieldDest.set(dest, idFieldSrc.get(src));
        createdAtFieldDest.set(dest, createdAtFieldSrc.get(src));
        lastModifiedAtFieldDest.set(dest, lastModifiedAtFieldSrc.get(src));
        createdByFieldDest.set(dest, createdByFieldSrc.get(src));
        lastModifiedByFieldDest.set(dest, lastModifiedByFieldSrc.get(src));

    }

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
