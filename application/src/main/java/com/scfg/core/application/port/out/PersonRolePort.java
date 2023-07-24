package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.PersonRole;

import java.util.List;

public interface PersonRolePort {
    PersonRole saveOrUpdate(PersonRole personRole);

    boolean saveOrUpdateAll(List<PersonRole> personRoleList);
}
