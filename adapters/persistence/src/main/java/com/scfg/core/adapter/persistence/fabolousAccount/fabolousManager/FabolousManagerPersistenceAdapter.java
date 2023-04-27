package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManager;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousManagerPersistenceAdapter {

    private final FabolousManagerRepository fabolousManagerRepository;

    public List<FabolousManagerJpaEntity> getAllManager() {
        try {
            return fabolousManagerRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousManagerJpaEntity insert(FabolousManagerJpaEntity mg){
        try {
            return fabolousManagerRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
