package com.scfg.core.application.port.out;

import com.scfg.core.domain.ChangePasswordRequest;
import com.scfg.core.domain.common.User;

import java.util.List;

public interface UserPort {

    User findById(long id);

    Boolean changePassword(ChangePasswordRequest changePasswordRequest);

    List<User> findAll();

    Object findAllByPage(int page, int size);

    User save(User user);

    User update(User user);

    User delete(User user);

}
