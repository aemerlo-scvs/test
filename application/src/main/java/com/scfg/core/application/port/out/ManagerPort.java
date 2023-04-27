package com.scfg.core.application.port.out;


import com.scfg.core.domain.managers.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ManagerPort {
//    Manager getManagerById(BigInteger id);
    List<Manager> getAllManager();

    Manager getManagerByFullname(String fullname);

    Map<String, Object> findOrUpsert(Manager managerDomain) throws NoSuchFieldException, IllegalAccessException;
}
