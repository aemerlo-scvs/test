package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Action;
import com.scfg.core.domain.common.Role;

import java.util.List;

public interface ActionPort {
    List<Action> findAll();

    Action findById(long id);
}
