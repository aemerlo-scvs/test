package com.scfg.core.domain.configuracionesSistemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AgentDTOAll {
        private Long id;
        private String name;
        private String lastName;
        private String motherLastName;
        private Date birthDate;
        private Long ci;
        private Date ciDueDate;
        private String cellPhoneNumber;
        private String telephone;
        private String homeAddress;
        private String email;
        private String gender;
        private Long genderIdc;
        private Long nit;
        private String regional;
        private Long regionalIdc;
        private String office;
        private Long officeIdc;
        private Date admissionDate;
        private Date egressDate;
        private String businessLine;
        private int status;
        private Date createdAt;
        private Date lastModifiedAt;

        public AgentDTOAll(Long id, String name, String lastName, String motherLastName, Date birthDate, Long ci, Date ciDueDate, String cellPhoneNumber, String telephone, String homeAddress, String email, String gender, Long genderIdc, Long nit, String regional, Long regionalIdc, String office, Long officeIdc, Date admissionDate, Date egressDate, String businessLine, int status, Date createdAt, Date lastModifiedAt) {
                this.id = id;
                this.name = name;
                this.lastName = lastName;
                this.motherLastName = motherLastName;
                this.birthDate = birthDate;
                this.ci = ci;
                this.ciDueDate = ciDueDate;
                this.cellPhoneNumber = cellPhoneNumber;
                this.telephone = telephone;
                this.homeAddress = homeAddress;
                this.email = email;
                this.gender = gender;
                this.genderIdc = genderIdc;
                this.nit = nit;
                this.regional = regional;
                this.regionalIdc = regionalIdc;
                this.office = office;
                this.officeIdc = officeIdc;
                this.admissionDate = admissionDate;
                this.egressDate = egressDate;
                this.businessLine = businessLine;
                this.status = status;
                this.createdAt = createdAt;
                this.lastModifiedAt = lastModifiedAt;
        }
}
