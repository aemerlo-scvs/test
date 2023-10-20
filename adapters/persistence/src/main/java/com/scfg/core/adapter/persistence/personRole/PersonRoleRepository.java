package com.scfg.core.adapter.persistence.personRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRoleRepository extends JpaRepository<PersonRoleJpaEntity, Long> {

    PersonRoleJpaEntity save(PersonRoleJpaEntity personRoleJpaEntity);
}
