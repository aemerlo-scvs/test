package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.Manager_AgencyJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.Manager_AgencyRepository;
import com.scfg.core.application.port.out.Manager_AgencyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.managers.Manager_Agency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class Manager_AgencyPersistenceAdapter implements Manager_AgencyPort {
    @Autowired
    private Manager_AgencyRepository managerAgencyRepository;

    public Manager_AgencyRepository getManagerAgencyRepository() {
        return managerAgencyRepository;
    }

    public void setManagerAgencyRepository(Manager_AgencyRepository managerAgencyRepository) {
        this.managerAgencyRepository = managerAgencyRepository;
    }

    @Override
    public List<Manager_Agency> getAllManagerAgency() {
        try {
            Object managerAgencyList = managerAgencyRepository.findAll();
            return (List<Manager_Agency>) managerAgencyList;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public synchronized Manager_AgencyJpaEntity insertManagerAgency(Manager_AgencyJpaEntity mg){
        try {
           return managerAgencyRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public synchronized Manager_AgencyJpaEntity updateManagerAgency(Manager_AgencyJpaEntity mg){
        try {
            return managerAgencyRepository.save(mg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //#region Mapper
    public Manager_AgencyJpaEntity mapToJpaEntity(Manager_Agency managerAgency) {
        return new Manager_AgencyJpaEntity(
                managerAgency.getMANAGER_AGENCY_ID() == null ? null : managerAgency.getMANAGER_AGENCY_ID(),
                managerAgency.getAGENCY_ID(),
                managerAgency.getMANAGER_ID(),
                managerAgency.getUSER_CREATE(),
                managerAgency.getDATE_CREATE(),
                managerAgency.getINCLUSION_DATE(),
                managerAgency.getEXCLUSION_DATE(),
                managerAgency.getSTATUS()
        );
    }

    private Manager_Agency mapToDomain(Manager_AgencyJpaEntity managerAgencyJpaEntity){
        return new Manager_Agency(
                managerAgencyJpaEntity.getMANAGER_AGENCY_ID() == null ? null : managerAgencyJpaEntity.getMANAGER_AGENCY_ID(),
                managerAgencyJpaEntity.getAGENCY_ID(),
                managerAgencyJpaEntity.getMANAGER_ID(),
                managerAgencyJpaEntity.getUSER_CREATE(),
                managerAgencyJpaEntity.getDATE_CREATE(),
                managerAgencyJpaEntity.getINCLUSION_DATE(),
                managerAgencyJpaEntity.getEXCLUSION_DATE(),
                managerAgencyJpaEntity.getSTATUS()
        );
    }
//#endregion
}
