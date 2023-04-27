package com.scfg.core.domain.dto.fabolous;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Setter
@Getter
@SuperBuilder
public class FabolousFindClient {
    private String fullName;
    private String identification;
    private String ext;
    private String beneficiary;
    private String relationShip;

    private Double percentage;
    private Date startDate;
    private Date finishDate;

    public FabolousFindClient() {
    }

    public FabolousFindClient(String fullName, String identification, String ext, String beneficiary, String relationShip, Double percentage, Date startDate, Date finishDate) {
        this.fullName = fullName;
        this.identification = identification;
        this.ext = ext;
        this.beneficiary = beneficiary;
        this.relationShip = relationShip;
        this.percentage = percentage;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public FabolousFindClient(Object[] columns) {
        this.fullName = (columns[0] != null) ? (String) columns[0] : "";
        this.identification = (columns[1] != null) ? (String) columns[1] : "";
        this.ext = (columns[2] != null) ? (String) columns[2] : "";
        this.beneficiary = (columns[3] != null) ? (String) columns[3] : "";
        this.relationShip = (columns[4] != null) ? (String) columns[4] : "";
        this.percentage = (columns[5] != null) ? (Double) columns[5] : 0;
        this.startDate = (columns[6] != null) ? (Date) columns[6] : null;
        this.finishDate = (columns[7] != null) ? (Date) columns[7] : null;
    }
}
