package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.NewPerson;

import java.util.List;

public interface NewPersonPort {
//    List<NewPerson> findAll();
//    NewPerson findById(long personId);
//    long saveOrUpdate(NewPerson person);
    Object searchPerson(Long docType, String documentNumber, String name);
}
