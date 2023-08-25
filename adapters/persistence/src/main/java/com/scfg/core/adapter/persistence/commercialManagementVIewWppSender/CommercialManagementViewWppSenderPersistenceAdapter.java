package com.scfg.core.adapter.persistence.commercialManagementVIewWppSender;

import com.scfg.core.application.port.out.CommercialManagementViewWppSenderPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.dto.virh.CommercialManagementViewWppSenderDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommercialManagementViewWppSenderPersistenceAdapter implements CommercialManagementViewWppSenderPort {
    private final CommercialManagementViewWppSenderRepository repository;

    @Override
    public List<CommercialManagementViewWppSenderDTO> findAll() {
        List<CommercialManagementViewWppSenderJpaEntity> list = repository.findAll();
        return !list.isEmpty() ? list.stream().map(o ->
                new ModelMapper().map(o, CommercialManagementViewWppSenderDTO.class)).collect(Collectors.toList()) : new ArrayList<>();
    }
}
