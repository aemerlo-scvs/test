package com.scfg.core.adapter.persistence.fabolousAccount.fabolousZone;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousZonePersistenceAdapter {

    private final FabolousZoneRepository fabolousZoneRepository;

    public List<FabolousZoneJpaEntity> getAllZone() {
        try {
            return fabolousZoneRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousZoneJpaEntity insert(FabolousZoneJpaEntity mg){
        try {
            return fabolousZoneRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
