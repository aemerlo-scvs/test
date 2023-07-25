package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.SearchClientDTO;
import com.scfg.core.domain.person.Person;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PersonUseCase {

    List<Person> getAll();

    List<Person> getAllByAssignedGroup(Integer assignedGroup);

    Person getById(long personId);

    Person getByIdWitDirections(long personId);

    Person getByIdentificationNumberAndType(String identificationNumber, Integer documentType);

    List<SearchClientDTO> getAllByParametersClf(PersonDTO personDTO);

    SearchClientDTO getByParametersClf(Person person);

    Boolean save(Person person);

    Boolean update(Person person);

    Boolean delete(Person person);

    Person getJuridicalPersonByNitNumber(Long nitNumber);
}
