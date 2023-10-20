package com.scfg.core.domain.smvs;

import com.scfg.core.common.enums.RequestStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
public class ContactCenterRequestDTO {

    public ContactCenterRequestDTO(String name, String lastName, String motherLastName, String marriedLastName, String activationCode, String telephone, String email, Date requestDate, String salePlace, String agencyName, String voucherNumber, Integer requestStatusIdc) {
        this.completeName = this.getCompleteNameAux(name, lastName, motherLastName, marriedLastName);
        this.activationCode = activationCode;
//        this.cellphone = cellphone;
        this.telephone = parseToInt(telephone);
        this.email = email;
        this.requestDate = requestDate.toInstant().atZone(ZoneId.of("America/La_Paz")).toLocalDateTime();
        this.salePlace = salePlace + " - " + agencyName;
        this.voucherNumber = parseToLong(voucherNumber);
        this.requestStatus = (requestStatusIdc == RequestStatusEnum.PENDING.getValue()) ? "Pendiente" : "";
    }

    private String completeName;

    private String activationCode;

    private String cellphone;

    private Integer telephone;

    private String email;

    private LocalDateTime requestDate;

    private String salePlace;

    private Long voucherNumber;

    private String requestStatus;


    //#region Helpers

    private String getCompleteNameAux(String name, String lastName, String motherLastName, String marriedLastName) {
        String completeName = "";
        if (name != null) {
            completeName += name + " ";
        }
        if (lastName != null) {
            completeName += lastName + " ";
        }
        if (motherLastName != null) {
            completeName += motherLastName + " ";
        }
        if (marriedLastName != null) {
            completeName += marriedLastName + " ";
        }
        completeName = completeName.substring(0, completeName.length() - 1);
        return completeName;
    }

    private Integer parseToInt(String value) {
        Integer intVal = null;
        if (value == null) {
            return intVal;
        }

        try {
            value = value.trim();
            intVal = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            intVal = 0;
        }
        return intVal;
    }

    private Long parseToLong(String value) {
        Long longVal = null;
        if (value == null) {
            return longVal;
        }

        try {
            value = value.trim();
            longVal = Long.parseLong(value);
        } catch (NumberFormatException e) {
            longVal = 0L;
        }
        return longVal;
    }

    //#endregion

}
