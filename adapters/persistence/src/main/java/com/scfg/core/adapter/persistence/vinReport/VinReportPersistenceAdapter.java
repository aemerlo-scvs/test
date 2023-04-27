package com.scfg.core.adapter.persistence.vinReport;

import com.scfg.core.adapter.persistence.clfReport.CLFReportRepository;
import com.scfg.core.application.port.out.VinPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class VinReportPersistenceAdapter implements VinPort {

    private final VinReportRepository vinReportRepository;
    private final EntityManager em;
    @Override
    public List<Object> getProductionReport(VinReportFilterDTO filterDTO) {
        String query = vinReportRepository.getProductionReport(filterDTO);
        List objDt = em.createNativeQuery(query).getResultList();
        em.close();
        return objDt;
    }

    @Override
    public List<Object> getCommercialReport(VinReportFilterDTO filterDTO) {
        String query = vinReportRepository.getCommercialReport(filterDTO);
        List objDt = em.createNativeQuery(query).getResultList();
        em.close();
        return objDt;
    }
}
