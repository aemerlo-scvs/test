package com.scfg.core.adapter.persistence.fabolousAccount.fabolousInsurance;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousInsurancePersistenceAdapter {

    private final FabolousInsuranceRepository fabolousInsuranceRepository;

    public List<FabolousInsuranceJpaEntity> getAllInsurance() {
        try {
            return fabolousInsuranceRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousInsuranceJpaEntity insertIns(FabolousInsuranceJpaEntity mg){
        try {
            return fabolousInsuranceRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<FabolousInsuranceJpaEntity> getAllInsuranceByUploadId(Long id) {
        try {
            return fabolousInsuranceRepository.findAllByUploadReportId(id);
        } catch (Exception e) {
            System.out.println("No se pudieron encontrar los insurance de UploadReport solicitado: " + id);
            return null;
        }
    }

    public void deleteInsertReportUpload(Long uploadId) {
        try {
            int DELETE_STATUS = 0;
            fabolousInsuranceRepository.deleteInsuranceByReportId(uploadId, DELETE_STATUS);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
}
