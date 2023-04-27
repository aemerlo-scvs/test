package com.scfg.core.adapter.persistence.alert;

import com.scfg.core.application.port.out.AlertPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Alert;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AlertPersistenceAdapter  implements AlertPort {
    private final AlertRepository repository;
    @Override
    public Alert findByAlert(Integer id) {
        Optional <AlertJpaEntity> alertJpaEntity= repository.findById(id);
        Alert alert= alertJpaEntity.isPresent()?ConvetJpaToDomian( alertJpaEntity.get()):null;
        return alert;
    }

    @Override
    public List<Alert> loadAllAlerts() {
        List<AlertJpaEntity> list = (List<AlertJpaEntity>) repository.findAll();
        return list.stream().map(o -> new ModelMapper().map(o, Alert.class)).collect(Collectors.toList());
    }

    private Alert ConvetJpaToDomian(AlertJpaEntity alertJpaEntity) {
        Alert alert=Alert.builder()
                .alert_id(alertJpaEntity.getAlert_id())
                .count_email(alertJpaEntity.getCount_email())
                .description(alertJpaEntity.getDescription())
                .date_create(alertJpaEntity.getDate_create())
                .exclusion_date(alertJpaEntity.getExclusion_date())
                .environment_id(alertJpaEntity.getEnvironment_id())
                .mail_body(alertJpaEntity.getMail_body())
                .mail_subject(alertJpaEntity.getMail_subject())
                .mail_cc(alertJpaEntity.getMail_cc())
                .mail_to(alertJpaEntity.getMail_to())
                .status(alertJpaEntity.getStatus())
                .user_create(alertJpaEntity.getUser_create())
                .user_update(alertJpaEntity.getUser_update())
                .build();
        return alert;
    }
}
