package com.scfg.core.application.port.in;

import com.scfg.core.domain.ChangePasswordRequest;
import com.scfg.core.domain.common.Role;
import com.scfg.core.domain.common.User;

import java.util.List;

public interface UserUseCase {
    User getById(long id);

    List<User> get();

    Object getByPage(int size, int page);

    User save(User user);

    User update(User user);

    User delete(long userId);

    Boolean changePassword(ChangePasswordRequest changePasswordRequest);
}
