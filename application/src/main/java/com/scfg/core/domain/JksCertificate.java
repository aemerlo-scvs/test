package com.scfg.core.domain;

import com.scfg.core.common.enums.JksAliasEnum;
import com.scfg.core.common.util.Jks;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.dto.JksCertificateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Lob;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class JksCertificate extends BaseDomain {
    private String abbreviation;
    private String description;
    @Lob
    private byte[] jks;

    //#region Constructors

    public JksCertificate() {
    }

    public JksCertificate(JksCertificateDTO jksCertificateDTO, String keyStore) {
        this.setId(jksCertificateDTO.getId());
        this.abbreviation = jksCertificateDTO.getAbbreviation();
        this.description = jksCertificateDTO.getDescription();
        this.jks = getJksContainer(jksCertificateDTO, keyStore);
    }

    //#endregion

    private byte[] getJksContainer(JksCertificateDTO jksCertificateDTO, String keyStore) {
        Map<String, String> jksContainer = new HashMap<>();
        jksContainer.put(JksAliasEnum.CERT_CONTENT.getValue(), jksCertificateDTO.getContent());
        jksContainer.put(JksAliasEnum.CERT_PASSWORD.getValue(), jksCertificateDTO.getPassword());

        return Jks.createKeyStoreTo(keyStore, jksContainer);
    }

}
