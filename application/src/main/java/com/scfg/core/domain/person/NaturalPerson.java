package com.scfg.core.domain.person;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.SMVSClientTypeEnum;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.smvs.MakePaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class NaturalPerson extends BaseDomain {

    private Integer clientCode;

    private Integer clientEventual;

    private Integer clientType;

    private String name;

    private String lastName;

    private String motherLastName;

    private String marriedLastName;

    private Integer maritalStatusIdc;

    private Integer documentType;

    private String identificationNumber;

    private String complement;

    private Integer extIdc;

    private LocalDateTime birthDate;

    private Integer genderIdc;

    private String profession;

    private String workPlace;

    private Integer workTypeIdc;

    private String position;

    private LocalDateTime entryDate;

    private Double salary;

    private Long internalClientCode;

    public NaturalPerson() {
    }

    //#region Constructors

    public NaturalPerson(NaturalPersonDomain naturalPersonDomain) {
        this.setId(naturalPersonDomain.getId());
        this.setClientCode(naturalPersonDomain.getClientCode());
        this.setClientEventual(naturalPersonDomain.getClientEventual());
        this.setClientType(naturalPersonDomain.getClientType());
        this.setName(naturalPersonDomain.getName());
        this.setLastName(naturalPersonDomain.getLastName());
        this.setMotherLastName(naturalPersonDomain.getMotherLastName());
        this.setMarriedLastName(naturalPersonDomain.getMarriedLastName());
        this.setMaritalStatusIdc(naturalPersonDomain.getMaritalStatusIdc());
        this.setDocumentType(naturalPersonDomain.getDocumentType());
        this.setIdentificationNumber(naturalPersonDomain.getIdentificationNumber());
        this.setComplement(naturalPersonDomain.getComplement());
        this.setExtIdc(naturalPersonDomain.getExtIdc());
        this.setBirthDate(naturalPersonDomain.getBirthDate());
        this.setGenderIdc(naturalPersonDomain.getGenderIdc());
        this.setProfession(naturalPersonDomain.getProfession());
        this.setWorkPlace(naturalPersonDomain.getWorkPlace());
        this.setWorkTypeIdc(naturalPersonDomain.getWorkTypeIdc());
        this.setCreatedAt(naturalPersonDomain.getCreatedAt());
        this.setLastModifiedAt(naturalPersonDomain.getLastModifiedAt());
        this.setCreatedBy(naturalPersonDomain.getCreatedBy());
        this.setLastModifiedBy(naturalPersonDomain.getLastModifiedBy());
        this.setInternalClientCode(naturalPersonDomain.getInternalClientCode());
    }

    //SMVS
    public NaturalPerson(MakePaymentDTO paymentDTO, Integer extIdc) {

        Integer clientEventual = PersistenceStatusEnum.DELETED.getValue();
        if (paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.EVENTUAL_CLIENT.getValue()) {
            clientEventual = PersistenceStatusEnum.CREATED_OR_UPDATED.getValue();
        }


        String nameAux = "";
        if (paymentDTO.getPrimer_nombre() != null) nameAux += paymentDTO.getPrimer_nombre().trim();
        if (paymentDTO.getSegundo_nombre() != null) nameAux += " " + paymentDTO.getSegundo_nombre().trim();
        String lastName = paymentDTO.getApellido_paterno() != null ? paymentDTO.getApellido_paterno().trim() : paymentDTO.getApellido_paterno();
        String motherLastName = paymentDTO.getApellido_materno() != null ? paymentDTO.getApellido_materno().trim() : paymentDTO.getApellido_materno();
        String marriedLastName = paymentDTO.getApellido_casada() != null ? paymentDTO.getApellido_casada().trim() : paymentDTO.getApellido_casada();

        this.setId(0L);
        this.setClientCode(paymentDTO.getCod_cliente());
        this.setClientEventual(clientEventual);
        this.setClientType(paymentDTO.getTipo_cliente());
        this.setName(nameAux);
        this.setLastName(lastName);
        this.setMotherLastName(motherLastName);
        this.setMarriedLastName(marriedLastName);
        this.setMaritalStatusIdc(paymentDTO.getEstado_civil() > 0 ? paymentDTO.getEstado_civil() : null);
        this.setDocumentType(paymentDTO.getTipo_documento());
        this.setIdentificationNumber(paymentDTO.getNro_documento());
        this.setComplement(paymentDTO.getComplemento());
        this.setExtIdc(extIdc > 0 ? extIdc : null);
        this.setBirthDate(paymentDTO.getFecha_nacimiento());
        this.setGenderIdc(paymentDTO.getGenero() > 0 ? paymentDTO.getGenero() : null);
        this.setProfession(paymentDTO.getProfesion());
    }

    //CLF
    public NaturalPerson(Person person) {
        String nameAux = person.getNaturalPerson().getName().trim();
        String lastName = person.getNaturalPerson().getLastName() != null ? person.getNaturalPerson().getLastName().trim() : person.getNaturalPerson().getLastName();
        String motherLastName = person.getNaturalPerson().getMotherLastName() != null ? person.getNaturalPerson().getMotherLastName().trim() : person.getNaturalPerson().getMotherLastName();
        String marriedLastName = person.getNaturalPerson().getMarriedLastName() != null ? person.getNaturalPerson().getMarriedLastName().trim() : person.getNaturalPerson().getMarriedLastName();

        this.setId(person.getNaturalPerson().getId());
        this.setClientCode(person.getNaturalPerson().getClientCode());
        this.setClientEventual(0);
        this.setClientType(person.getNaturalPerson().getClientType());
        this.setName(nameAux);
        this.setLastName(lastName);
        this.setMotherLastName(motherLastName);
        this.setMarriedLastName(marriedLastName);
        this.setMaritalStatusIdc(person.getNaturalPerson().getMaritalStatusIdc() > 0 ? person.getNaturalPerson().getMaritalStatusIdc() : null);
        this.setDocumentType((int) ClassifierEnum.CI_NATIONAL.getReferenceCode());
        this.setIdentificationNumber(person.getNaturalPerson().getIdentificationNumber());
        this.setComplement(person.getNaturalPerson().getComplement());
        this.setExtIdc(person.getNaturalPerson().getExtIdc() > 0 ? person.getNaturalPerson().getExtIdc() : null);
        this.setBirthDate(person.getNaturalPerson().getBirthDate());
        this.setGenderIdc(person.getNaturalPerson().getGenderIdc() > 0 ? person.getNaturalPerson().getGenderIdc() : null);
        this.setProfession(person.getNaturalPerson().getProfession());
        this.setWorkPlace(person.getNaturalPerson().getWorkPlace());
        this.setWorkTypeIdc(person.getNaturalPerson().getWorkTypeIdc());
    }

    //#endregion

    public String getCompleteName() {
        String completeName = "";
        if (this.name != null) completeName += this.name.trim();
        if (this.lastName != null) completeName += " " + this.lastName.trim();
        if (this.motherLastName != null) completeName += " " + this.motherLastName.trim();
        completeName += (this.marriedLastName == null || this.marriedLastName.isEmpty() || this.marriedLastName.trim().isEmpty()) ? "" :
                (ClassifierEnum.WIDOWED_STATUS.getReferenceCode() == this.maritalStatusIdc.longValue()) ?
                        " VIUDA DE " + this.marriedLastName.trim() : " DE " + this.marriedLastName.trim();
        return completeName;
    }
}
