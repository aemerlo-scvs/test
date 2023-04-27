package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManagerAgency;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousManagerAgencyPersistenceAdapter {

    private final FabolousManagerAgencyRepository fabolousManagerAgencyRepository;

    public List<FabolousManagerAgencyJpaEntity> getAllManagerAgency() {
        try {
            return fabolousManagerAgencyRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousManagerAgencyJpaEntity insert(FabolousManagerAgencyJpaEntity mg){
        try {
            return fabolousManagerAgencyRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public synchronized FabolousManagerAgencyJpaEntity update(FabolousManagerAgencyJpaEntity mg){
        try {
            return fabolousManagerAgencyRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
