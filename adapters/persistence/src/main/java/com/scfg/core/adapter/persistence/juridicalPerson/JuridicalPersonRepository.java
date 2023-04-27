package com.scfg.core.adapter.persistence.juridicalPerson;

import com.scfg.core.adapter.persistence.document.DocumentJpaEntity;
import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.domain.dto.JuridicalPersonWithPersonIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JuridicalPersonRepository extends JpaRepository<JuridicalPersonJpaEntity,Long> {
    @Query("SELECT p FROM PersonJpaEntity p " +
            "JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.assignedGroupIdc = :assignedGroup AND p.status = :status")
    List<PersonJpaEntity> findAllJuridicalPerson(@Param("assignedGroup") int assignedGroup,@Param("status") int status);

    @Query("SELECT new com.scfg.core.domain.dto.JuridicalPersonWithPersonIdDTO(jp.id, jp.name, jp.businessTypeIdc, p.id) \n" +
            "FROM JuridicalPersonJpaEntity jp \n" +
            "INNER JOIN PersonJpaEntity p ON p.juridicalPerson.id  = jp.id \n" +
            "WHERE p.assignedGroupIdc = :assignedGroup AND p.status = :status")
    List<JuridicalPersonWithPersonIdDTO> findAllJuridicalPersonByAssignedGroup(@Param("assignedGroup") int assignedGroup, @Param("status") int status);

    @Query("SELECT pl FROM PersonJpaEntity p " +
            "JOIN GeneralRequestJpaEntity gr " +
            "ON gr.personId = p.id " +
            "JOIN PolicyJpaEntity pl " +
            "ON pl.generalRequestId = gr.id " +
            "WHERE p.id = :id AND pl.status = :status")
    List<PolicyJpaEntity> findAllPolicyJP(@Param("id") Long personId,@Param("status") int status);

    @Query("SELECT d FROM DocumentJpaEntity d " +
            "JOIN FETCH d.personJpaEntity p " +
            "WHERE p.id = :id AND d.documentTypeIdc = :logoPj AND d.status = :status")
    DocumentJpaEntity getByJuridicPerson(@Param("id") Long personId,@Param("logoPj") int logoPj,@Param("status") int status);
}
