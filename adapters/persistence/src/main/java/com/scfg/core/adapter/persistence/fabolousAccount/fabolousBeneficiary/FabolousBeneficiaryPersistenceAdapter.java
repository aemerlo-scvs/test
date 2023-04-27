package com.scfg.core.adapter.persistence.fabolousAccount.fabolousBeneficiary;

import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousBeneficiaryPersistenceAdapter {

    private final FabolousBeneficiaryRepository fabolousBeneficiaryRepository;

    private final EntityManager em;

    public List<FabolousBeneficiaryJpaEntity> getAllBeneficiary() {
        try {
            return fabolousBeneficiaryRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousBeneficiaryJpaEntity insert(FabolousBeneficiaryJpaEntity mg){
        try {
            return fabolousBeneficiaryRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteInsertInsuranceReportUpload(String uploadId) {
        try {
            String queryDelete = "update FabolousBeneficiary set status = " + PersistenceStatusEnum.DELETED.getValue() + " " +
                    "where fabolousInsurancetId in " +
                    "(select i.id from FabolousBeneficiary b " +
                    "join FabolousInsurance i on b.fabolousInsurancetId = i.id " +
                    "join FabolousUploadReport u on u.id = i.fabolousUploadReportId " +
                    "where u.id = " + uploadId + ")";
            Query query = em.createNativeQuery(queryDelete);
            query.executeUpdate();
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
}
