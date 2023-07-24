package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.NewPerson;

import java.util.List;

public interface NewPersonPort {
    long saveOrUpdate(NewPerson newPerson);

    boolean saveOrUpdateAll(List<NewPerson> newPersonList);

    boolean findByIdentificationNumber(String identificationNumber);
    Object searchPerson(Long documentTypeIdc, String identificationNumber, String name);
}
