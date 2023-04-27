package com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousAgencyPersistenceAdapter {
    private final FabolousAgencyRepository fabolousAgencyRepository;

    public List<FabolousAgencyJpaEntity> getAllAgency() {
        try {
            return fabolousAgencyRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousAgencyJpaEntity insert(FabolousAgencyJpaEntity mg){
        try {
            return fabolousAgencyRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
