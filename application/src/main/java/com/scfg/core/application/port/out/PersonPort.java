package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.person.Person;

import java.util.List;

public interface PersonPort {

    List<Person> findAll();

    Person findById(long personId);

    Person findByIdentificationNumberAndType(String identificationNumber, Integer documentType);

    List<Person> findAllByFilters(PersonDTO personDTO);

    Boolean existsIdentificationNumber(String identificationNumber);

    long saveOrUpdate(Person person);

    List<Person> findAllByAssignedGroup(Integer assignedGroup);

    List<Person> findAllByListOfPersonId(List<Long> personIdList);

    Person findByNitNumber(Long nitNumber);

    Person findByPolicyIdWhenPolicyAndPersonIsOneToOne(Long policyId);
}
