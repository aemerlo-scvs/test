package com.scfg.core.domain.dto.vin;

import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.Beneficiary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ResponseProposalDto {

    private Long id;
    private Long personId;
    private String identificationNumber;
    private String names;
    private String lastname;
    private String motherLastname;
    private String marriedLastname;
    private String operationNumber;
    private Integer operationStatusIdc;
    private LocalDateTime createdAt;
    private String complement;
    private Date createAtMessage;
    private Integer messageTypeIdc;
    private String detailToMessage;
    private String email;
    private List<String> cellphoneNumbers;
    private Integer termInYears;
    private Double totalPremium;
    private List<Beneficiary> beneficiaryList;
    private boolean messageStatus;


    //#region Constructors

    public ResponseProposalDto(Object[] columns) {

        Timestamp grCreatedAt = (Timestamp) columns[10];
        Timestamp msCreatedAt = (Timestamp) columns[13];
        Timestamp mtsCreatedAt = (Timestamp) columns[16];

        this.id = (columns[0] != null) ? Long.parseLong(columns[0].toString()) : 0;
        this.personId = (columns[1] != null) ? Long.parseLong(columns[1].toString()) : 0;
        this.identificationNumber = (columns[2] != null) ? (String) columns[2] : "";
        this.complement = (columns[3] != null) ? (String) columns[3] : "";
        this.names = (columns[4] != null) ? (String) columns[4] : "";
        this.lastname = (columns[5] != null) ? (String) columns[5] : "";
        this.motherLastname = (columns[6] != null) ? (String) columns[6] : "";
        this.marriedLastname = (columns[7] != null) ? (String) columns[7] : "";
        this.operationNumber = (columns[8] != null) ? (String) columns[8] : "";
        this.operationStatusIdc = (columns[9] != null) ? Integer.parseInt(columns[9].toString()) : 0;
        this.createdAt = (columns[10] != null) ? grCreatedAt.toLocalDateTime() : null;

        this.messageTypeIdc = (columns[11] != null) ?
                Integer.parseInt(columns[11].toString()) :
                (columns[14] != null) ? Integer.parseInt(columns[14].toString()) : 0;

        this.detailToMessage = (columns[12] != null) ?
                columns[12].toString() :
                (columns[15] != null) ? columns[15].toString() : "";

        this.createAtMessage = (msCreatedAt != null) ?
                DateUtils.asDate(msCreatedAt.toLocalDateTime()) :
                (mtsCreatedAt != null) ? DateUtils.asDate(mtsCreatedAt.toLocalDateTime()) : null;

        this.totalPremium = (columns[17] != null) ? (Double) columns[17] : 0;
        this.termInYears = (columns[18] != null) ? Integer.parseInt(columns[18].toString()) : 0;
        this.email = (columns[19] != null) ? (String) columns[19] : "";
        this.messageStatus = this.createAtMessage != null;

    }

    //#endregion
}
