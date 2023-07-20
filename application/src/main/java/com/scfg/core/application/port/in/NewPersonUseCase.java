package com.scfg.core.application.port.in;

import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.person.NewPerson;
import com.scfg.core.domain.person.Person;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewPersonUseCase {
//     List<NewPerson> getAll();
//     NewPerson getById(long personId);
//     Boolean update(NewPerson person);
    Object searchPerson(Long docType, String documentNumber, String name);
    Boolean save(NewPerson newPerson);

}
