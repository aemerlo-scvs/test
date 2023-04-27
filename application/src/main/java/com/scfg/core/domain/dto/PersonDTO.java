package com.scfg.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {

    private String identificationNumber;

    private Long extIdc;

    private String complement;

    private String names;

    private String lastname;

    private String mothersLastname;

    private String marriedLastname;

    private Long requestNumber;

    //#region Constructors
    public PersonDTO(){}

    @Builder
    public PersonDTO(String identificationNumber, String name, String lastname, String mothersLastname, String marriedLastname) {
        this.identificationNumber = identificationNumber;
        this.names = name;
        this.lastname = lastname;
        this.mothersLastname = mothersLastname;
        this.marriedLastname = marriedLastname;
    }

    @Builder
    public PersonDTO(String identificationNumber, String name, String lastname, String mothersLastname, String marriedLastname, Long requestNumber) {
        this.identificationNumber = identificationNumber;
        this.names = name;
        this.lastname = lastname;
        this.mothersLastname = mothersLastname;
        this.marriedLastname = marriedLastname;
        this.requestNumber = requestNumber;
    }
    //#endregion

    @Override
    public String toString() {
        return "PersonDTO{" +
                "identificationNumber='" + identificationNumber + '\'' +
                ", extIdc=" + extIdc +
                ", complement='" + complement + '\'' +
                ", names='" + names + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mothersLastname='" + mothersLastname + '\'' +
                ", marriedLastname='" + marriedLastname + '\'' +
                ", requestNumber=" + requestNumber +
                '}';
    }
}
