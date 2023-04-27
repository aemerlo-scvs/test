package com.scfg.core.adapter.persistence.fabolousAccount.fabolousBranch;

import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousBranchPersistenceAdapter {

    private final FabolousBranchRepository fabolousBranchRepository;

    public List<FabolousBranchJpaEntity> getAllBranch() {
        try {
            return fabolousBranchRepository.getAll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized FabolousBranchJpaEntity insert(FabolousBranchJpaEntity mg){
        try {
            return fabolousBranchRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
