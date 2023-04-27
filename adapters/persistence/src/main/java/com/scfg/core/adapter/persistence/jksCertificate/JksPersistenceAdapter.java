package com.scfg.core.adapter.persistence.jksCertificate;

import com.scfg.core.application.port.out.JksCertificatePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.JksCertificate;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.dto.JksCertificateDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class JksPersistenceAdapter implements JksCertificatePort {

    private final JksCertificateRepository jksCertificateRepository;
    private final EntityManager em;


    @Override
    public List<JksCertificateDTO> findAllDTO() {
        List<JksCertificateJpaEntity> list = jksCertificateRepository
                .findAllByStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, JksCertificateDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JksCertificate> findAllByAbbreviations(List<String> abbreviations) {
        Query query = em.createQuery(jksCertificateRepository.getFindAllByAbbreviationStatusQuery());
        query.setParameter("status", PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        query.setParameter("abbreviations", abbreviations);

        List<JksCertificateJpaEntity> list = (List<JksCertificateJpaEntity>) query.getResultList();

        em.close();

        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, JksCertificate.class))
                .collect(Collectors.toList());
    }

    @Override
    public JksCertificateDTO findByIdDTO(Long id) {
        JksCertificateJpaEntity jksCertificateJpaEntity = jksCertificateRepository.findById(id)
                .orElseThrow(() -> new NotDataFoundException("Certificado, no encontrado"));

        return new ModelMapperConfig().getStrictModelMapper().map(jksCertificateJpaEntity, JksCertificateDTO.class);
    }

    @Override
    public JksCertificate findById(Long id) {
        JksCertificateJpaEntity jksCertificateJpaEntity = jksCertificateRepository.findById(id)
                .orElseThrow(() -> new NotDataFoundException("Certificado, no encontrado"));

        return new ModelMapperConfig().getStrictModelMapper().map(jksCertificateJpaEntity, JksCertificate.class);
    }

    @Override
    public JksCertificate findByAbbreviation(String abbreviation) {
        JksCertificateJpaEntity jksCertificateJpaEntity = jksCertificateRepository
                .findByAbbreviationAndStatus(abbreviation, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .orElseThrow(() -> new NotDataFoundException("Certificado, no encontrado"));

        return new ModelMapperConfig().getStrictModelMapper().map(jksCertificateJpaEntity, JksCertificate.class);
    }

    @Override
    public Long saveOrUpdate(JksCertificate jksCertificate) {
        JksCertificateJpaEntity jksCertificateJpaEntity = new ModelMapperConfig().getStrictModelMapper()
                .map(jksCertificate, JksCertificateJpaEntity.class);
        return jksCertificateRepository.save(jksCertificateJpaEntity).getId();
    }
}
