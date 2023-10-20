package com.scfg.core.application.service;

import com.scfg.core.application.port.in.RoleUseCase;
import com.scfg.core.application.port.out.RolePort;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleUseCase {

    private final RolePort rolePort;

    @Override
    public Role getById(long id) {
        return rolePort.findById(id);
    }

    @Override
    public List<Role> get() {
        return rolePort.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    public Role save(Role role) {
        String invalidField = this.validateRole(role);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
        return rolePort.save(role);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    public Role update(Role role) {
        String invalidField = this.validateRole(role);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
       Role roleAux = rolePort.findById(role.getId());
       return rolePort.update(role);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    public Role delete(long roleId) {
        Role roleAux = rolePort.findById(roleId);
        return rolePort.delete(roleAux);
    }

    @Override
    public Boolean existName(String name) {
        return rolePort.existName(name);
    }

    String validateRole(Role role){
        String field = null;
        if(role.getName() == null || role.getName().isEmpty() ) {
            field = "name";
        }
        return  field;
    }

}
