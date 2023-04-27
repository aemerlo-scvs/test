package com.scfg.core.adapter.persistence.user;

import com.scfg.core.domain.common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.common.User(u.id, u.createdAt, u.lastModifiedAt, u.createdBy, u.lastModifiedBy, u.name, u.surName, u.genderIdc, u.email, u.userName, u.password, u.secretQuestion, u.secretAnswer, u.regionalIdc, u.officeIdc, u.companyIdc, u.token, u.roleId) " +
            "FROM UserJpaEntity u " +
            "WHERE u.status = :status")
    List<User> customFindAll(@Param("status") int status);

    Optional<UserJpaEntity> findByUserName(String username);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findById(long id);

    Optional<UserJpaEntity> findByTokenEquals(String token);

}
