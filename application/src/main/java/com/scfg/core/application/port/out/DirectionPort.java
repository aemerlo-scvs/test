package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Direction;

import java.util.List;

public interface DirectionPort {

    List<Direction> findAllByPersonId(long personId);
    List<Direction> findAllByNewPersonId(long newPersonId);

    List<Direction> findAllByListOfPersonId(List<Long> personListId);

    Direction findDirectionByNameAndPersonId(Long personId, String direction);

    long saveOrUpdate(Direction direction);

    boolean saveAllDirection(List<Direction> directions);

    void deletePersonalAndWorkDirectionByPersonId(long personId);
}
