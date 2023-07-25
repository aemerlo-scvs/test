package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.JksCertificateDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface JksCertificateUseCase {

    List<JksCertificateDTO> getAll();
    JksCertificateDTO getById(Long id);

    Boolean saveOrUpdate(JksCertificateDTO jksCertificateDTO);

    String signDocumentWithP12Cert(String document, List<String> ownerSigns, LocalDateTime signatureDate);

    byte[] signDocumentWithP12Cert(byte[] document, List<String> ownerSigns);

}
