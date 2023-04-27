package com.scfg.core.adapter.persistence.TempCajeros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

public interface TempCajerosRepository extends JpaRepository<TempCajerosJpaEntity,Long> {

    @Procedure("proc_asiganacion_cajeros_por_cada_mes")
    String getExcuteAsignacionCajerosPorCadaMes();

}
