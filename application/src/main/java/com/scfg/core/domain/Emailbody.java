package com.scfg.core.domain;

import com.scfg.core.common.enums.AlertEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Emailbody {

    private String[] email;

    private String[] emailcopy;

    private String content;

    private String subject;

    private byte[] bytes;

    private String name_attachment;

    private Long referenceId;

    private String productName;

    //#region Constructors

    // ALERT

    public Emailbody(Alert alert, String productName, Long referenceId) {
        this.email = alert.getMail_to().split(";");
        this.emailcopy = alert.getMail_cc().split(";");
        this.content = alert.getMail_body();
        this.subject = alert.getMail_subject();
        this.referenceId = referenceId;
        this.productName = getProductName(productName);
    }


    //#endregion

    private String getProductName(String productName) {
        String auxProductName = "";

        if(productName.toUpperCase().trim().contains("DIAMANTE")){
            auxProductName = "DH – DIAMANTES URUBO";
        }

        if(productName.toUpperCase().trim().contains("SIRARI")){
            auxProductName = "DH – SIRARI URUBO";
        }
        return auxProductName;
    }

}
