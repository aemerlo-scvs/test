package com.scfg.core.adapter.persistence.clfReport;

import com.scfg.core.application.port.out.CLFReportPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.credicasas.CommercialReportDTO;
import com.scfg.core.domain.dto.credicasas.SuscriptionReportDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CLFReportPersistenceAdapter implements CLFReportPort {

    private final CLFReportRepository clfReportRepository;
    private final EntityManager em;

    @Override
    public List<SuscriptionReportDTO> getReport(SearchReportParamDTO searchReportParamDTO) {
        String query = clfReportRepository.getReport(searchReportParamDTO);
        List objDt = em.createNativeQuery(query).getResultList();
        em.close();
        return objDt;
    }

    @Override
    public List<CommercialReportDTO> getComercialReport(SearchReportParamDTO searchReportParamDTO) {
        String query = clfReportRepository.getCommercialReport(searchReportParamDTO);
        List objDt = em.createNativeQuery(query).getResultList();
        em.close();
        return objDt;
    }
}
