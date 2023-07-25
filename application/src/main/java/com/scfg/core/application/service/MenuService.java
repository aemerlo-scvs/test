package com.scfg.core.application.service;

import com.scfg.core.application.port.in.MenuUseCase;
import com.scfg.core.application.port.out.MenuPort;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuUseCase {

    private final MenuPort menuPort;

    @Override
    public List<Menu> getByRoleId(long roleId) {
        return menuPort.findAllByRoleId(roleId);
    }

    @Override
    public List<Menu> getAll() {
        return menuPort.findAll();
    }

    @Override
    @Transactional
    public List<Menu> getAllWithDetail() {
        return menuPort.findAllWithDetail();
    }

    @Override
    @Transactional
    public Menu getById(long id) {
        return menuPort.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    public Boolean save(Menu menu) {
        String invalidField = this.validateMenu(menu);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
        return menuPort.save(menu);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    public Boolean update(Menu menu) {
        String invalidField = this.validateMenu(menu);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
        Menu menuAux = menuPort.findById(menu.getId());
        return menuPort.save(menu);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    public Boolean delete(long menuId) {
        Menu menuAux = menuPort.findById(menuId);
        return menuPort.delete(menuAux);
    }

    @Override
    public Boolean existNameFather(String name) {
        return menuPort.existNameFather(name);
    }

    String validateMenu(Menu menu){
        String field = null;
        if(menu.getName() == null || menu.getName().isEmpty() ) {
            field = "name";
        }
        return  field;
    }

}
