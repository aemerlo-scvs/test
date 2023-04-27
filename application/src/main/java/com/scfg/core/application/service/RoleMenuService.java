package com.scfg.core.application.service;

import com.scfg.core.application.port.in.RoleMenuUseCase;
import com.scfg.core.application.port.out.RoleMenuPort;
import com.scfg.core.application.port.out.RolePort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.RoleMenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleMenuService implements RoleMenuUseCase {

    private final RoleMenuPort roleMenuPort;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    public Boolean save(RoleMenuDTO roleMenuDTO) {
         return roleMenuPort.save(roleMenuDTO);
    }
}
