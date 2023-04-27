package com.scfg.core.domain.dto;

import com.scfg.core.domain.JksCertificate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class JksCertificateDTO {
    private Long id;
    private String abbreviation;
    private String description;
    private String content;
    private String password;
    private Date createdAt;
    private Date lastModifiedAt;


    //#region Constructors

    public JksCertificateDTO() {
    }

    public JksCertificateDTO(JksCertificate jksCertificate, String certPassword, String certContent) {
        this.id = jksCertificate.getId();
        this.abbreviation = jksCertificate.getAbbreviation();
        this.description = jksCertificate.getDescription();
        this.content = certContent;
        this.password = certPassword;
        this.createdAt = jksCertificate.getCreatedAt();
        this.lastModifiedAt = jksCertificate.getLastModifiedAt();
    }


    //#endregion

}
