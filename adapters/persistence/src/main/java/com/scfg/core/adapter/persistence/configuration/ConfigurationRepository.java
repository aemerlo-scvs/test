package com.scfg.core.adapter.persistence.configuration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<ConfigurationJpaEntity, Long> {
}
