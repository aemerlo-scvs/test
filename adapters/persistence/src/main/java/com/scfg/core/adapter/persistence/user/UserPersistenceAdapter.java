package com.scfg.core.adapter.persistence.user;

import com.scfg.core.application.port.out.AuthUserPort;
import com.scfg.core.application.port.out.UserPort;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.PasswordEncoder;
import com.scfg.core.common.PersistenceAdapter;

import com.scfg.core.domain.common.AuthUser;
import com.scfg.core.domain.ChangePasswordRequest;
import com.scfg.core.domain.common.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
class UserPersistenceAdapter implements AuthUserPort, UserPort {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserPersistenceAdapter(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthUser findByUserName(String username) {
        UserJpaEntity userJpaEntity = userRepository.findByUserName(username).orElseThrow(() -> new NotDataFoundException("Usuario no encontrado."));
        return mapToDomainAuthUser(userJpaEntity);
    }

    @Override
    public AuthUser findByEmail(String email) {
        UserJpaEntity userJpaEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotDataFoundException("Usuario no encontrado."));
        return mapToDomainAuthUser(userJpaEntity);
    }

    @Override
    public AuthUser findByToken(String token) {
        UserJpaEntity userJpaEntity = this.userRepository.findByTokenEquals(token).orElseThrow(() -> new NotDataFoundException("Token invalido"));
        return this.mapToDomainAuthUser(userJpaEntity);
    }

    @Override
    public AuthUser update(AuthUser authUser) {
        UserJpaEntity userJpaEntity = mapToJpaEntity(authUser);
        // userJpaEntity.setLastModifiedAt(LocalDateTime.now());
        userJpaEntity = userRepository.save(userJpaEntity);
        return mapToDomainAuthUser(userJpaEntity);
    }

    @Override
    public User findById(long id) {
        UserJpaEntity userJpaEntity = userRepository.findById(id).orElseThrow(() -> new NotDataFoundException("User: " + id + " Not Found"));
        return mapToDomain(userJpaEntity);
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        String username = changePasswordRequest.getUsername();
        UserJpaEntity userJpaEntity = userRepository.findByUserName(username).orElseThrow(() -> new NotDataFoundException("Usuario: " + username + ", no encontrado"));
        if (userJpaEntity != null) {
            String newPassword = passwordEncoder.password().encode(changePasswordRequest.getConfirmPassword());
            userJpaEntity.setPassword(newPassword);
            userJpaEntity = userRepository.save(userJpaEntity);
        }
        return (userJpaEntity != null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.customFindAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public Object findAllByPage(int page, int size) {
        Pageable newPage = PageRequest.of(page, size);
        Page<UserJpaEntity> list = userRepository.findAll(newPage);
        list.getContent().forEach(c -> c.setMortgageReliefItems(new ArrayList<>()));
        return list;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = mapToJpaEntity(user);

        // userJpaEntity.setCreatedAt(LocalDateTime.now());

        String newPassword = passwordEncoder.password().encode(userJpaEntity.getPassword());
        userJpaEntity.setPassword(newPassword);

        userJpaEntity = userRepository.save(userJpaEntity);

        return mapToDomain(userJpaEntity);
    }

    @Override
    public User update(User user) {
        UserJpaEntity userJpaEntity = mapToJpaEntity(user);
        // userJpaEntity.setLastModifiedAt(LocalDateTime.now());
        userJpaEntity = userRepository.save(userJpaEntity);
        return mapToDomain(userJpaEntity);
    }

    @Override
    public User delete(User user) {
        UserJpaEntity userJpaEntity = mapToJpaEntity(user);
        userJpaEntity.setStatus(PersistenceStatusEnum.DELETED.getValue());
        userJpaEntity = userRepository.save(userJpaEntity);
        return mapToDomain(userJpaEntity);
    }


    //#region Mapper
    private UserJpaEntity mapToJpaEntity(User user) {

        UserJpaEntity userJpaEntity = UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .surName(user.getSurName())
                .genderIdc(user.getGenderIdc())
                .email(user.getEmail())
                .userName(user.getUserName())
                .password(user.getPassword())
                .secretQuestion(user.getSecretQuestion())
                .secretAnswer(user.getSecretAnswer())
                .createdAt(user.getCreatedAt())
                .lastModifiedAt(user.getLastModifiedAt())
                .officeIdc(user.getOfficeIdc())
                .regionalIdc(user.getRegionalIdc())
                .companyIdc(user.getCompanyIdc())
                .token(user.getToken())
                .roleId(user.getRoleId())
                .bfsUserCode(user.getBfsUserCode())
                .bfsAgencyCode(user.getBfsAgencyCode())
                .mortgageReliefItems(null) //TODO: revisar, será deprecado
                .build();

        return userJpaEntity;

    }

    private User mapToDomain(UserJpaEntity userJpaEntity) {

        User user = User.builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .surName(userJpaEntity.getSurName())
                .genderIdc(userJpaEntity.getGenderIdc())
                .email(userJpaEntity.getEmail())
                .userName(userJpaEntity.getUserName())
                .password(userJpaEntity.getPassword())
                .secretQuestion(userJpaEntity.getSecretQuestion())
                .secretAnswer(userJpaEntity.getSecretAnswer())
                .createdAt(userJpaEntity.getCreatedAt())
                .lastModifiedAt(userJpaEntity.getLastModifiedAt())
                .officeIdc(userJpaEntity.getOfficeIdc())
                .regionalIdc(userJpaEntity.getRegionalIdc())
                .companyIdc(userJpaEntity.getCompanyIdc())
                .bfsUserCode(userJpaEntity.getBfsUserCode())
                .bfsAgencyCode(userJpaEntity.getBfsAgencyCode())
                .token(userJpaEntity.getToken())
                .roleId(userJpaEntity.getRoleId())
                .build();

        return user;

    }

    private AuthUser mapToDomainAuthUser(UserJpaEntity userJpaEntity) {

        AuthUser authUser = AuthUser.builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .surName(userJpaEntity.getSurName())
                .genderIdc(userJpaEntity.getGenderIdc())
                .email(userJpaEntity.getEmail())
                .userName(userJpaEntity.getUserName())
                .password(userJpaEntity.getPassword())
                .secretQuestion(userJpaEntity.getSecretQuestion())
                .secretAnswer(userJpaEntity.getSecretAnswer())
                .createdAt(userJpaEntity.getCreatedAt())
                .lastModifiedAt(userJpaEntity.getLastModifiedAt())
                .officeIdc(userJpaEntity.getOfficeIdc())
                .regionalIdc(userJpaEntity.getRegionalIdc())
                .companyIdc(userJpaEntity.getCompanyIdc())
                .bfsUserCode(userJpaEntity.getBfsUserCode())
                .bfsAgencyCode(userJpaEntity.getBfsAgencyCode())
                .token(userJpaEntity.getToken())
                .roleId(userJpaEntity.getRoleId())
                .build();

        return authUser;

    }
    //#endregion
}
