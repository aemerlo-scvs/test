package com.scfg.core.adapter.persistence.newPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPersonRepository extends JpaRepository<NewPersonJpaEntity, Long> {

}
