package com.scfg.core.domain.dto.credicasas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scfg.core.domain.person.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchClientDTO {


    @JsonProperty("exists")
    private Boolean exists;

    @JsonProperty("isPEP")
    private Boolean isPEP;

    @JsonProperty("currentAmount")
    private Double currentAmount;

    @JsonProperty("person")
    private Person person;

    @JsonProperty("wasRejected")
    private Boolean wasRejected;

    public SearchClientDTO(){}

    @Builder
    public SearchClientDTO(Boolean exists, Boolean isPEP, Double currentAmount, Person person, Boolean wasRejected) {
        this.exists = exists;
        this.isPEP = isPEP;
        this.currentAmount = currentAmount;
        this.person = person;
        this.wasRejected = wasRejected;
    }
}
