package com.scfg.core.adapter.persistence.direction;

import com.scfg.core.adapter.persistence.newPerson.NewPersonJpaEntity;
import com.scfg.core.application.port.out.DirectionPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.DirectionTypeEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.person.NewPerson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class DirectionPersistenceAdapter implements DirectionPort {

    private final DirectionRepository directionRepository;

    @Override
    public List<Direction> findAllByPersonId(long personId) {
        List<DirectionJpaEntity> list = directionRepository.findAllByPersonId(personId);
        return list.stream().map(x -> new ModelMapper().map(x, Direction.class)).collect(Collectors.toList());
    }

    public List<Direction> findAllByNewPersonId(long newPersonId) {
        List<DirectionJpaEntity> list = directionRepository.findAllByNewPersonId(newPersonId);
        return list.stream().map(x -> new ModelMapper().map(x, Direction.class)).collect(Collectors.toList());
    }

    @Override
    public List<Direction> findAllByListOfPersonId(List<Long> personListId) {
        List<DirectionJpaEntity> list = directionRepository.findAllByPersonIdIn(personListId);
        return list.stream().map(x -> new ModelMapper().map(x, Direction.class)).collect(Collectors.toList());
    }

    @Override
    public Direction findDirectionByNameAndPersonId(Long personId, String direction) {
        return null;
    }

    @Override
    public long saveOrUpdate(Direction direction) {
        DirectionJpaEntity directionJpaEntity = mapToJpaEntity(direction);
        directionJpaEntity = directionRepository.save(directionJpaEntity);
        return directionJpaEntity.getId();
    }

    @Override
    public boolean saveAllDirection(List<Direction> directions) {
        List<DirectionJpaEntity> directionJpaEntities = new ArrayList<>();
        directions.forEach(e -> {
            DirectionJpaEntity directionJpaEntity = mapToJpaEntity(e);
            directionJpaEntities.add(directionJpaEntity);
        });
        directionRepository.saveAll(directionJpaEntities);
        return true;
    }

    @Override
    public void deletePersonalAndWorkDirectionByPersonId(long personId) {
        directionRepository.deleteWorkAndPersonalDirectionByPersonId(PersistenceStatusEnum.DELETED.getValue(), personId, DirectionTypeEnum.PERSONAL.getValue(),DirectionTypeEnum.WORK.getValue());
    }
    //#region Mappers
    public static DirectionJpaEntity mapToJpaEntity(Direction direction) {
        return new ModelMapper().map(direction,DirectionJpaEntity.class);
    }

    //#endregion

}
