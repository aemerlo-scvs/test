package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.Action;
import com.scfg.core.domain.common.Role;

import java.util.List;

public interface ActionUseCase {
    List<Action> getAll();

    Action getById(long id);
}
