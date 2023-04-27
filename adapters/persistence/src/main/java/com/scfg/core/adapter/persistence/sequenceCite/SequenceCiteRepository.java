package com.scfg.core.adapter.persistence.sequenceCite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SequenceCiteRepository extends JpaRepository<SequenceCiteJpaEntity,Long> {

    SequenceCiteJpaEntity findByCompanyIdcAndAndYear(long companyIdc, String year);
}
