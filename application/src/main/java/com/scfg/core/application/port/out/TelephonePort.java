package com.scfg.core.application.port.out;

import com.scfg.core.domain.Telephone;

import java.util.List;

public interface TelephonePort {
    Telephone saveOrUpdate(Telephone telephone);

    boolean saveOrUpdateAll(List<Telephone> telephoneList);

    List<Telephone> getAllByPersonId(Long personId);
    List<Telephone> getAllByNewPersonId(Long personId);
}
