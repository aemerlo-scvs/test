package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.Branch_OfficeJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.Branch_OfficeRepository;
import com.scfg.core.application.port.out.Branch_OfficePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.managers.Branch_Office;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class Branch_OfficePersistenceAdapter implements Branch_OfficePort {

    @Autowired
    private Branch_OfficeRepository branchOfficeRepository;

    public Branch_OfficeRepository getBranchOfficeRepository() {
        return branchOfficeRepository;
    }

    public void setBranchOfficeRepository(Branch_OfficeRepository branchOfficeRepository) {
        this.branchOfficeRepository = branchOfficeRepository;
    }

    @Override
    public List<Branch_Office> getAllBranch() {
        try {
            Object listBranch = branchOfficeRepository.findAll();
            return (List<Branch_Office>) listBranch;
        } catch (Exception e){
            return null;
        }
    }

    public synchronized Branch_OfficeJpaEntity insertBranch(Branch_OfficeJpaEntity mg){
        try {
           return branchOfficeRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return  null;
        }
    }

    //#region Mapper
    private Branch_OfficeJpaEntity mapToJpaEntity(Branch_Office branch) {
        return new Branch_OfficeJpaEntity(
                branch.getBRANCH_OFFICE_ID() == null ? null : branch.getBRANCH_OFFICE_ID(),
                branch.getDESCRIPTION(),
                branch.getUSER_CREATE(),
                branch.getUSER_UPDATE(),
                branch.getDATE_CREATE(),
                branch.getDATE_UPDATE(),
                branch.getSTATUS()
        );
    }

    private Branch_Office mapToDomain(Branch_OfficeJpaEntity branchJpa){
        return new Branch_Office(
                branchJpa.getBRANCH_OFFICE_ID() == null ? null : branchJpa.getBRANCH_OFFICE_ID(),
                branchJpa.getDESCRIPTION(),
                branchJpa.getUSER_CREATE(),
                branchJpa.getUSER_UPDATE(),
                branchJpa.getDATE_CREATE(),
                branchJpa.getDATE_UPDATE(),
                branchJpa.getSTATUS()
        );
    }
//#endregion
}
