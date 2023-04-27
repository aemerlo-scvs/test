package com.scfg.core.adapter.persistence.fabolousAccount.fabolousClient;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousClientPersistenceAdapter {

    private final FabolousClientRepository fabolousClientRepository;

    public List<FabolousClientJpaEntity> getAllClient() {
        try {
            return fabolousClientRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousClientJpaEntity insert(FabolousClientJpaEntity mg){
        try {
            return fabolousClientRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
