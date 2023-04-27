package com.scfg.core.adapter.persistence.broker;

import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrokerRepository extends JpaRepository<BrokerJpaEntity, Long> {
    @Query("SELECT new com.scfg.core.domain.configuracionesSistemas.BrokerDTO( " +
            "br.id, br.businessName, br.nit, br.address, br.telephone, br.email, " +
            "cj.description, cj.id, br.approvalCode, br.status, br.createdAt, br.lastModifiedAt) " +
            "FROM BrokerJpaEntity  br " +
            "JOIN ClassifierJpaEntity cj " +
            "ON cj.id = br.cityIdc")
    List<BrokerDTO> findAllBroker();
}
