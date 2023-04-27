package com.scfg.core.application.service;

import com.scfg.core.application.port.in.UserUseCase;
import com.scfg.core.application.port.out.UserPort;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;

import com.scfg.core.domain.ChangePasswordRequest;
import com.scfg.core.domain.common.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserPort userPort;

    @Override
    public User getById(long id) {
        return userPort.findById(id);
    }

    @Override
    public List<User> get() {
        return userPort.findAll();
    }

    @Override
    public Object getByPage(int size, int page) {
        return userPort.findAllByPage(size, page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    public User save(User user) {
        String invalidField = this.validateUser(user);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
        return userPort.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    public User update(User user) {
        String invalidField = this.validateUser(user);
        if(invalidField != null){
            throw new OperationException("Invalid Field: " + invalidField);
        }
        User userAux = userPort.findById(user.getId());
        return userPort.update(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    public User delete(long userId) {
        User userAux = userPort.findById(userId);
        return userPort.delete(userAux);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        return userPort.changePassword(changePasswordRequest);
    }

    String validateUser(User user){
        String field = null;
        if(user.getName() == null || user.getName().isEmpty() || user.getName().length() > 100) {
            field = "name";
        }
        if(user.getSurName() == null || user.getName().length() > 100) {
            field = "surName";
        }
        if(user.getGenderIdc() == null) {
            field = "idcGender";
        }
        if(user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().length() > 150) {
            field = "email";
        }
        if(user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() > 250) {
            field = "password";
        }
//        if(user.getStatus() == null) {
//            field = "status";
//        }
        if(user.getRoleId() == null) {
            field = "idRole";
        }
        return  field;
    }
}
