package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

public class VCMAManagerDTO {
    @Getter @Setter
    private BigInteger managerId;
    @Getter @Setter
    private String managerName;
    @Getter @Setter
    private BigInteger agencyId;
    @Getter @Setter
    private String agencyName;
    @Getter @Setter
    private BigInteger branchOfficeId;
    @Getter @Setter
    private String branchOfficeName;
    @Getter @Setter
    private BigInteger zoneId;
    @Getter @Setter
    private String zoneName;
    @Getter @Setter
    private String position;
    @Getter @Setter
    private int status;
    @Getter @Setter
    private String inclusionDate;

    public VCMAManagerDTO() {
    }

    public VCMAManagerDTO(BigInteger managerId, String managerName, BigInteger agencyId, String agencyName, BigInteger branchOfficeId, String branchOfficeName, BigInteger zoneId, String zoneName, String position, int status, String inclusionDate) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.agencyId = agencyId;
        this.agencyName = agencyName;
        this.branchOfficeId = branchOfficeId;
        this.branchOfficeName = branchOfficeName;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.position = position;
        this.status = status;
        this.inclusionDate = inclusionDate;
    }

    public VCMAManagerDTO(BigInteger managerId, String managerName, BigInteger agencyId, String agencyName, BigInteger branchOfficeId, String branchOfficeName, String zoneName, String position, int status, String inclusionDate) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.agencyId = agencyId;
        this.agencyName = agencyName;
        this.branchOfficeId = branchOfficeId;
        this.branchOfficeName = branchOfficeName;
        this.zoneName = zoneName;
        this.position = position;
        this.status = status;
        this.inclusionDate = inclusionDate;
    }

    public VCMAManagerDTO(BigInteger managerId, String managerName, BigInteger agencyId, String agencyName, BigInteger branchOfficeId, String branchOfficeName, String zoneName, String position, int status) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.agencyId = agencyId;
        this.agencyName = agencyName;
        this.branchOfficeId = branchOfficeId;
        this.branchOfficeName = branchOfficeName;
        this.zoneName = zoneName;
        this.position = position;
        this.status = status;
    }
}
