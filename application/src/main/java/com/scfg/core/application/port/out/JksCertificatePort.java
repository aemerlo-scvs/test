package com.scfg.core.application.port.out;

import com.scfg.core.domain.JksCertificate;
import com.scfg.core.domain.dto.JksCertificateDTO;

import java.util.List;

public interface JksCertificatePort {

    List<JksCertificateDTO> findAllDTO();

    List<JksCertificate> findAllByAbbreviations(List<String> abbreviations);

    JksCertificateDTO findByIdDTO(Long id);

    JksCertificate findById(Long id);

    JksCertificate findByAbbreviation(String abbreviation);

    Long saveOrUpdate(JksCertificate jksCertificate);

}
