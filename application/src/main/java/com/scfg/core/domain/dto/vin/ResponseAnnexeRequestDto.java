package com.scfg.core.domain.dto.vin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ResponseAnnexeRequestDto {

    private Long id;
    private String identificationNumber;
    private String names;
    private String lastname;
    private String motherLastname;
    private String marriedLastname;
    private Integer statusIdc;
    private Date createdAt;
    private String complement;
    private Long generalRequestId;
    private Long annexeTypeId;
    private Long policyId;
    private String numberPolicy;
    private Long requestAnnexeId;
    private LocalDateTime requestDate;

    public ResponseAnnexeRequestDto(Long requestAnnexeId, String identificationNumber, String complement, String names, String lastName,
                                    String motherLastName, String marriedLastname, Integer statusIdc, Date createAt, Long generalRequestId,
                                    Long annexeTypeId, Long policyId, String numberPolicy, LocalDateTime requestDate) {
        this.requestAnnexeId = requestAnnexeId;
        this.identificationNumber = identificationNumber;
        this.complement = complement;
        this.names = names;
        this.lastname = lastName;
        this.motherLastname = motherLastName;
        this.marriedLastname = marriedLastname;
        this.statusIdc = statusIdc;
        this.createdAt = createAt;
        this.generalRequestId = generalRequestId;
        this.annexeTypeId = annexeTypeId;
        this.policyId = policyId;
        this.numberPolicy = numberPolicy;
        this.requestDate = requestDate;
    }
}
