package com.scfg.core.adapter.persistence.mathReserve;

import com.scfg.core.application.port.out.MathReservePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.MathReserve;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MathReserveAdapter implements MathReservePort {

    private final MathReserveRepository mathReserveRepository;

    @Override
    public long saveOrUpdate(MathReserve mathReserve) {
        MathReserveJpaEntity mathReserveJpaEntity = new ModelMapper().map(mathReserve, MathReserveJpaEntity.class);
        mathReserveJpaEntity = mathReserveRepository.save(mathReserveJpaEntity);
        return mathReserveJpaEntity.getId();
    }

    @Override
    public MathReserve getById(Long mathReserveId) {
        MathReserveJpaEntity mathReserveJpaEntity = mathReserveRepository.findById(mathReserveId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if (mathReserveJpaEntity == null) {
            return null;
        }
        return new ModelMapper().map(mathReserveJpaEntity, MathReserve.class);
    }

    @Override
    public List<MathReserve> getAllByInsurerYearAndTotalYear(Integer insurerYear, Integer totalYear) {
        List<MathReserveJpaEntity> mathReserveJpaEntityList = mathReserveRepository.
                findAllByAgeAndTotalYear(insurerYear, totalYear, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        List<MathReserve> mathReserveList = new ArrayList<>();

        if (mathReserveJpaEntityList.isEmpty()) {
            return mathReserveList;
        }

        mathReserveJpaEntityList.forEach(e -> {
            MathReserve mathReserve = new ModelMapper().map(e, MathReserve.class);
            mathReserveList.add(mathReserve);
        });

        return mathReserveList;
    }
}
