package com.scfg.core.adapter.persistence.mathReserve;

import com.scfg.core.adapter.persistence.messageResponse.MessageResponseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MathReserveRepository extends JpaRepository<MathReserveJpaEntity, Long> {

    @Query("SELECT m from MathReserveJpaEntity m \n" +
            "WHERE m.status = :status AND m.id = :mathReserveId")
    MathReserveJpaEntity findById(@Param("mathReserveId") Long messageSentId, @Param("status") Integer status);

    @Query("SELECT m FROM MathReserveJpaEntity m \n" +
            "WHERE m.age = :age AND m.vigencyYears <= :totalYear AND m.status = :status \n" +
            "ORDER BY m.vigencyYears DESC")
    List<MathReserveJpaEntity> findAllByAgeAndTotalYear(@Param("age") Integer age,
                                                        @Param("totalYear") Integer totalYear,
                                                        @Param("status") Integer status);
}
