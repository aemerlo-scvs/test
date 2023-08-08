package com.scfg.core.application.port.in;
import com.scfg.core.domain.person.NewPerson;

public interface NewPersonUseCase {
    Object searchPerson(Long docType, String documentNumber, String name);
    Boolean saveOrUpdate(NewPerson newPerson);

}
