package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.NewPerson;
import com.scfg.core.domain.person.Person;

import java.util.List;

public interface NewPersonPort {
    long saveOrUpdate(NewPerson newPerson);

    boolean saveOrUpdateAll(List<NewPerson> newPersonList);

    boolean findByIdentificationNumber(String identificationNumber);
    NewPerson findById(long newPersonId);
    Object searchPerson(Long documentTypeIdc, String identificationNumber, String name);
}
