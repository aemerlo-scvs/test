package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.Knowledge_HistoryJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.Knowledge_HistoryRepository;
import com.scfg.core.application.port.out.Knowledge_HistoryPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Knowledge_History;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class Knowledge_HistoryAdapter implements Knowledge_HistoryPort {
    @Autowired
    private final Knowledge_HistoryRepository repository;

    @Override
    public List<Knowledge_History> findall() {
        return null;
    }

    @Override
    public boolean guardar(List<Knowledge_History> list) {
        try {
            if (list.size() > 0) {
                for (Knowledge_History knowledge_history : list) {
                    if (!add(knowledge_history)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    private boolean add(Knowledge_History knowledge_history) {
        try {
            Knowledge_HistoryJpaEntity obj = convetToJpa(knowledge_history);
            repository.save(obj);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    private Knowledge_HistoryJpaEntity convetToJpa(Knowledge_History knowledge_history) {
        return new Knowledge_HistoryJpaEntity(
                knowledge_history.getPolicy_number(), knowledge_history.getNames_incorrect(), knowledge_history.getNames_correct(), knowledge_history.getManager_code()
        );

    }
}
