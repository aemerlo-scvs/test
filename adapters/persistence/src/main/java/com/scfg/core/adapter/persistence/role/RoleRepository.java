package com.scfg.core.adapter.persistence.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleJpaEntity, Long> {
    Optional<RoleJpaEntity> findById(long id);
    Optional<RoleJpaEntity> findByName(String name);
}
