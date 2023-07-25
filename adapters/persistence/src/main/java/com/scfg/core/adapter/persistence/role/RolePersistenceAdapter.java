package com.scfg.core.adapter.persistence.role;

import com.scfg.core.application.port.out.RolePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.common.Role;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RolePort {

    private final RoleRepository roleRepository;

    @Override
    public Role findById(long id) {
        RoleJpaEntity roleJpaEntity = roleRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Rol: " + id + ", no encontrado"));
        return mapToDomain(roleJpaEntity);
    }

    @Override
    public Role findByName(String name) {
        RoleJpaEntity roleJpaEntity = roleRepository.findByName(name).orElseThrow(() -> new NotDataFoundException("Rol: " + name + ", no encontrado"));
        return mapToDomain(roleJpaEntity);
    }

    @Override
    public List<Role> findAll() {
        Object roles = roleRepository.findAll();
        return (List<Role>) roles;
    }

    @Override
    public Role save(Role role) {
        RoleJpaEntity roleJpaEntity = mapToJpaEntity(role);
        // roleJpaEntity.setCreatedAt(LocalDateTime.now());
        roleJpaEntity = roleRepository.save(roleJpaEntity);
        return mapToDomain(roleJpaEntity);
    }

    @Override
    public Role update(Role role) {
        RoleJpaEntity roleJpaEntity = mapToJpaEntity(role);
        // roleJpaEntity.setLastModifiedAt(LocalDateTime.now());
        roleJpaEntity = roleRepository.save(roleJpaEntity);
        return mapToDomain(roleJpaEntity);
    }

    @Override
    public Role delete(Role role) {
        RoleJpaEntity roleJpaEntity = mapToJpaEntity(role);
        roleJpaEntity.setStatus(PersistenceStatusEnum.DELETED.getValue());
        roleJpaEntity = roleRepository.save(roleJpaEntity);
        return mapToDomain(roleJpaEntity);
    }

    @Override
    public Boolean existName(String name) {
        return roleRepository.existsByName(name);
    }

    //#region Mapper
    public static RoleJpaEntity mapToJpaEntity(Role role) {
        RoleJpaEntity roleJpaEntity = RoleJpaEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .lastModifiedAt(role.getCreatedAt())
                .build();
        return roleJpaEntity;
    }

    private Role mapToDomain(RoleJpaEntity roleJpaEntity) {

        Role role = Role.builder()
                .id(roleJpaEntity.getId())
                .name(roleJpaEntity.getName())
                .description(roleJpaEntity.getDescription())
                .createdAt(roleJpaEntity.getCreatedAt())
                .lastModifiedAt(roleJpaEntity.getLastModifiedAt())
                .build();

        return role;
    }
    //#endregion

}


