package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.NewPerson;

public interface NewPersonPort {
    long saveOrUpdate(NewPerson newPerson);
    Object searchPerson(Long documentTypeIdc, String identificationNumber, String name);
}
