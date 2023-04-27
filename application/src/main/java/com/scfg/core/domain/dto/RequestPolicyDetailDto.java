package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequestPolicyDetailDto {

    private Long id;
    private String identificationNumber;
    private String names;
    private String lastName;
    private String motherLastName;
    private String product;
    private String plan;
    private String numberPolicy;
    private Integer statePolicy;
    private String toDate;
    private String fromDate;

    public RequestPolicyDetailDto(Long id, String identificationNumber, String names, String lastName,
                                  String motherLastName, String product, String plan, String numberPolicy,
                                  Integer statePolicy, Date toDate, Date fromDate) {
        this.id = id;
        this.identificationNumber = identificationNumber;
        this.names = names;
        this.lastName = lastName;
        this.motherLastName = motherLastName;
        this.product = product;
        this.plan = plan;
        this.numberPolicy = numberPolicy;
        this.statePolicy = statePolicy;
        this.toDate = toDate.toString();
        this.fromDate = fromDate.toString();
    }
}
