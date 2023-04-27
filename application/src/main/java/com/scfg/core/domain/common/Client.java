package com.scfg.core.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Client extends BaseDomain {

    private String names;

    private String lastName;

    private String mothersLastName;

    private String marriedLastName;

    private String duplicateCopy;

    private String extension;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private String nationality;

    private String documentNumber;

    private String gender;

    private Double accumulatedAmountDhl;

    private Double accumulatedAmountDhn;

    private Long documentTypeId;

    private Long currencyId;

    // Relationships
    private Classifier documentType;
    private Classifier currency;
}
