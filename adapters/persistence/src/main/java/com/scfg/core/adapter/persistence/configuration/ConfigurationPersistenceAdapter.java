package com.scfg.core.adapter.persistence.configuration;

import com.scfg.core.application.port.out.ConfigurationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.Configuration;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
class ConfigurationPersistenceAdapter implements ConfigurationPort {

    private final ConfigurationRepository configurationRepository;

    @Override
    public Configuration find() {
        List<ConfigurationJpaEntity> configurationJpaEntityList = configurationRepository.findAll();
        if (configurationJpaEntityList.size() <= 0) {
            return null;
        }
        ConfigurationJpaEntity configurationJpaEntity = configurationJpaEntityList.get(0);
        return mapToDomain(configurationJpaEntity);
    }

    @Override
    public Configuration saveOrUpdate(Configuration configuration) {
        ConfigurationJpaEntity configurationJpaEntity = mapToJpaEntity(configuration);
        configurationJpaEntity = configurationRepository.save(configurationJpaEntity);
        return mapToDomain(configurationJpaEntity);
    }

    //#region Mappers

    public static ConfigurationJpaEntity mapToJpaEntity(Configuration configuration) {
        ConfigurationJpaEntity configurationJpaEntity = ConfigurationJpaEntity.builder()
                .id(configuration.getId())
                .numberFormat(configuration.getNumberFormat())
                .numberDigits(configuration.getNumberDigits())
                .dateFormat(configuration.getDateFormat())
                .timeFormat(configuration.getTimeFormat())
                .createdAt(configuration.getCreatedAt())
                .lastModifiedAt(configuration.getLastModifiedAt())
                .build();
        return configurationJpaEntity;
    }

    public static Configuration mapToDomain(ConfigurationJpaEntity configurationJpaEntity) {

        Configuration configuration = Configuration.builder()
                .id(configurationJpaEntity.getId())
                .numberFormat(configurationJpaEntity.getNumberFormat())
                .numberDigits(configurationJpaEntity.getNumberDigits())
                .dateFormat(configurationJpaEntity.getDateFormat())
                .timeFormat(configurationJpaEntity.getTimeFormat())
                .createdAt(configurationJpaEntity.getCreatedAt())
                .lastModifiedAt(configurationJpaEntity.getLastModifiedAt())
                .build();

        return configuration;
    }

    //#endregion
}
