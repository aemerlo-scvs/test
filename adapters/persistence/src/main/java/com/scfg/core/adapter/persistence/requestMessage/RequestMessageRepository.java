package com.scfg.core.adapter.persistence.requestMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestMessageRepository extends JpaRepository<RequestMessageJpaEntity,Long> {
    //todo ELIMINAR
}
