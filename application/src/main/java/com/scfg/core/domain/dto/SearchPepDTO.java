package com.scfg.core.domain.dto;

import com.scfg.core.domain.person.NaturalPerson;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "DTO para buscar un cliente PEP")
public class SearchPepDTO {

    private String name;

    private String lastName;

    private String motherLastName;

    private String identificationNumber;


    //#region Constructors
    public SearchPepDTO(NaturalPerson naturalPerson) {
        this.name = naturalPerson.getName();
        this.lastName = naturalPerson.getLastName();
        this.motherLastName = naturalPerson.getMotherLastName();
        this.identificationNumber = naturalPerson.getIdentificationNumber();
    }

    public SearchPepDTO(PersonDTO personDTO) {
        this.name = personDTO.getNames();
        this.lastName = personDTO.getLastname();
        this.motherLastName = personDTO.getMothersLastname();
        this.identificationNumber = personDTO.getIdentificationNumber();
    }

    //#endregion

}
