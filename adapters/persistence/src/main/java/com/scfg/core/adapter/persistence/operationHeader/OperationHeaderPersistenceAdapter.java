package com.scfg.core.adapter.persistence.operationHeader;

import com.scfg.core.application.port.out.OperationHeaderPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.OperationHeader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class OperationHeaderPersistenceAdapter implements OperationHeaderPort {

    private final OperationHeaderRepository operationHeaderRepository;

    @Override
    public OperationHeader findByGeneralRequestId(Long generalRequestId) {
        List<OperationHeader> list = operationHeaderRepository.findAllByGeneralRequestId(generalRequestId).stream().map(x -> new ModelMapper().map(x, OperationHeader.class)).collect(Collectors.toList());
        return list.size() > 0 ? list.get(0) : null;
    }
}
