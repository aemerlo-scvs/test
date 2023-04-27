package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ActionUseCase;
import com.scfg.core.application.port.out.ActionPort;
import com.scfg.core.application.port.out.RolePort;
import com.scfg.core.domain.common.Action;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService implements ActionUseCase {

    private final ActionPort actionPort;

    @Override
    public List<Action> getAll() {
        return actionPort.findAll();
    }

    @Override
    public Action getById(long id) {
        return actionPort.findById(id);
    }
}
