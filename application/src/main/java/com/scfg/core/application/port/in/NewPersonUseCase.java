package com.scfg.core.application.port.in;

import com.scfg.core.domain.person.NewPerson;
import java.util.List;

public interface NewPersonUseCase {
//     List<NewPerson> getAll();
//     NewPerson getById(long personId);
//     Boolean save(NewPerson person);
//     Boolean update(NewPerson person);
    Object searchPerson(Long docType, String documentNumber, String name);
}
