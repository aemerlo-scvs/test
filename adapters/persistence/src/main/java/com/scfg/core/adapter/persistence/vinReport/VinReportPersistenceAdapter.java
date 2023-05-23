package com.scfg.core.adapter.persistence.vinReport;

import com.scfg.core.adapter.persistence.clfReport.CLFReportRepository;
import com.scfg.core.application.port.out.VinPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class VinReportPersistenceAdapter implements VinPort {

    private final VinReportRepository vinReportRepository;
    private final EntityManager em;
    @Override
    public List<Object> getProductionReport(VinReportFilterDTO filterDTO) {
        Query query = em.createNativeQuery(vinReportRepository.getProductionReport());
        query.setParameter("fromDate", filterDTO.getFromDate());
        query.setParameter("toDate", filterDTO.getToDate());
        query.setParameter("policyStatusIdc", filterDTO.getPolicyStatusIdc());

        List list = query.getResultList();
        em.close();

        return list;
    }

    @Override
    public List<Object> getCommercialReport(VinReportFilterDTO filterDTO) {
        Query query = em.createNativeQuery(vinReportRepository.getCommercialReport());
        query.setParameter("fromDate", filterDTO.getFromDate());
        query.setParameter("toDate", filterDTO.getToDate());
        query.setParameter("policyStatusIdc", filterDTO.getPolicyStatusIdc());

        List list = query.getResultList();
        em.close();

        return list;
    }
}
