package com.scfg.core.adapter.persistence.broker;

import com.scfg.core.application.port.out.BrokerPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Broker;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class BrokerPersistenceAdapter implements BrokerPort {
    private final BrokerRepository brokerRepository;

    @Override
    public PersistenceResponse save(Broker broker, boolean returnEntity) {
        BrokerJpaEntity brokerJpaEntity = format(broker);
        brokerJpaEntity = brokerRepository.save(brokerJpaEntity);
        return new PersistenceResponse(
                Broker.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                brokerJpaEntity
        );
    }

    @Override
    public PersistenceResponse update(Broker broker) {
        Optional<BrokerJpaEntity> brokerJpaEntity = brokerRepository.findById(broker.getId());
        BrokerJpaEntity brokerJpaEntity1 = brokerJpaEntity.isPresent() ? brokerJpaEntity.get() : null;
        brokerJpaEntity1.setBusinessName(broker.getBusinessName());
        brokerJpaEntity1.setNit(broker.getNit());
        brokerJpaEntity1.setAddress(broker.getAddress());
        brokerJpaEntity1.setTelephone(broker.getTelephone());
        brokerJpaEntity1.setEmail(broker.getEmail());
        brokerJpaEntity1.setCityIdc(broker.getCityIdc());
        brokerJpaEntity1.setApprovalCode(broker.getApprovalCode());
        brokerJpaEntity1.setLastModifiedAt(new Date());
        brokerJpaEntity1.setStatus(broker.getStatus());
        brokerRepository.save(brokerJpaEntity1);
        return new PersistenceResponse(
                Broker.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                brokerJpaEntity1
        );
    }

    @Override
    public PersistenceResponse delete(BrokerDTO broker) {
        Optional<BrokerJpaEntity> brokerJpaEntity = brokerRepository.findById(broker.getId());
        BrokerJpaEntity brokerJpaEntity1 = brokerJpaEntity.isPresent() ? brokerJpaEntity.get() : null;
        brokerJpaEntity1.setLastModifiedAt(new Date());
        brokerJpaEntity1.setStatus(0);
        brokerRepository.save(brokerJpaEntity1);

        return new PersistenceResponse(
                Broker.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                brokerJpaEntity1
        );
    }

    @Override
    public List<BrokerDTO> getAllBrokers() {
        return brokerRepository.findAllBroker();
    }

    @Override
    public List<BrokerDTO> getfilterParamenters(FilterParamenter paramenter) {
        List<BrokerDTO> list = brokerRepository.findAllBroker();
        List<BrokerDTO>list1=listMap(list);
        return list1;
    }

    @Override
    public Broker getBrokerById(long brokerId) {
        Optional<BrokerJpaEntity> brokerJpaEntity = brokerRepository.findById(brokerId);
        return formatToDomain(brokerJpaEntity.get());
    }

    private List<BrokerDTO> listMap(List<BrokerDTO> list) {
        List<BrokerDTO> brokerList = new ArrayList();
        for (BrokerDTO obj : list) {
            brokerList.add(formatToDomain1(obj));
        }
        return brokerList;
    }

    private BrokerJpaEntity format(Broker broker) {
        BrokerJpaEntity brokerJpaEntity = BrokerJpaEntity.builder()
                .id(broker.getId())
                .businessName(broker.getBusinessName())
                .nit(broker.getNit())
                .address(broker.getAddress())
                .telephone(broker.getTelephone())
                .email(broker.getEmail())
                .cityIdc(broker.getCityIdc())
                .approvalCode(broker.getApprovalCode())
                .status(broker.getStatus())
                .createdAt(broker.getCreatedAt())
                .build();
        return brokerJpaEntity;
    }

    private Broker formatToDomain(BrokerJpaEntity obj) {
        Broker br = Broker.builder()
                .id(obj.getId())
                .businessName(obj.getBusinessName())
                .nit(obj.getNit())
                .address(obj.getAddress())
                .telephone(obj.getTelephone())
                .email(obj.getEmail())
                .cityIdc(obj.getCityIdc())
                .approvalCode(obj.getApprovalCode())
                .createdAt(obj.getCreatedAt())
                .lastModifiedAt(obj.getLastModifiedAt())
                .status(obj.getStatus())
                .build();
        return br;
    }

    private BrokerDTO formatToDomain1(BrokerDTO obj) {
        BrokerDTO br = BrokerDTO.builder()
                .id(obj.getId())
                .businessName(obj.getBusinessName())
                .nit(obj.getNit())
                .address(obj.getAddress())
                .telephone(obj.getTelephone())
                .email(obj.getEmail())
                .city(obj.getCity())
                .cityIdc(obj.getCityIdc())
                .approvalCode(obj.getApprovalCode())
                .createdAt(obj.getCreatedAt())
                .lastModifiedAt(obj.getLastModifiedAt())
                .status(obj.getStatus())
                .build();
        return br;
    }

}
