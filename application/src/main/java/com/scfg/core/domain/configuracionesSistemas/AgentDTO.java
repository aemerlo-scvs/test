package com.scfg.core.domain.configuracionesSistemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AgentDTO {
    private Long id;
    private String name;
    private String lastName;
    private String motherLastName;
    private Date birthDate;
    private long ci;
    private Date ciDueDate;
    private String cellPhoneNumber;
    private String telephone;
    private String homeAddress;
    private String email;
    private String gender;
    private long genderIdc;
    private long nit;
    private String regional;
    private long regionalIdc;
    private String office;
    private long officeIdc;
    private Date admissionDate;
    private Date egressDate;
    private String businessLine;
    private List<Long> businessLineIdc;
    private int status;
    private Date createdAt;
    private Date lastModifiedAt;
}
