package com.scfg.core.adapter.persistence.person;

import com.scfg.core.adapter.persistence.juridicalPerson.JuridicalPersonJpaEntity;
import com.scfg.core.adapter.persistence.juridicalPerson.JuridicalPersonRepository;
import com.scfg.core.adapter.persistence.naturalPerson.NaturalPersonJpaEntity;
import com.scfg.core.adapter.persistence.naturalPerson.NaturalPersonRepository;
import com.scfg.core.application.port.out.PersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.person.JuridicalPerson;
import com.scfg.core.domain.person.NaturalPerson;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PersonPersistenceAdapter implements PersonPort {

    private final PersonRepository personRepository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final JuridicalPersonRepository juridicalPersonRepository;

    private final EntityManager em;

    @Override
    public List<Person> findAll() {
        List<Person> personList = personRepository.findAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return personList;
    }

    @Override
    public Person findById(long personId) {
        Person person = personRepository.customFindById(personId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return person;
    }

    @Override
    public Person findByIdentificationNumberAndType(String identificationNumber, Integer documentType) {
        Person person = personRepository.findByNaturalPersonIdentificationNumberAndDocumentType(identificationNumber, documentType);
        return person;
    }

    @Override
    public List<Person> findAllByFilters(PersonDTO personDTO) {
        String query = personRepository.getFindAllByFiltersBaseQuery() + getPersonFilters(personDTO);

        List<Person> personList = em.createQuery(query).getResultList();

        em.close();
        return personList;
    }

    @Override
    public Boolean existsIdentificationNumber(String identificationNumber) {
        Person person = personRepository.findByNaturalPersonIdentificationNumber(identificationNumber);
        return person != null;
    }
    
    @Override
    public long saveOrUpdate(Person person) {
        PersonJpaEntity personJpaEntity = mapToPersonJpaEntity(person);
        personJpaEntity = personRepository.save(personJpaEntity);
        person.setId(personJpaEntity.getId());
        if (person.getNaturalPerson() != null) {
            NaturalPersonJpaEntity naturalPersonJpaEntity = mapToNaturalPersonJpaEntity(person);
            naturalPersonRepository.save(naturalPersonJpaEntity);
            return naturalPersonJpaEntity.getPerson().getId();
        }
        JuridicalPersonJpaEntity juridicalPersonJpaEntity = mapToJuridicalPersonJpaEntity(person);
        juridicalPersonJpaEntity = juridicalPersonRepository.save(juridicalPersonJpaEntity);
        return juridicalPersonJpaEntity.getPerson().getId();
    }

    @Override
    public List<Person> findAllByAssignedGroup(Integer assignedGroup) {
        List<Person> personList = personRepository.findAllByAssignedGroupIdc(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),assignedGroup);
        return personList;
    }

    @Override
    public List<Person> findAllByListOfPersonId(List<Long> personIdList) {
        List<Person> personList = personRepository.findAllByListId(personIdList,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return personList;
    }

    @Override
    public Person findByNitNumber(Long nitNumber) {
        Person person = personRepository.findByNitNumber(nitNumber,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return person;
    }

    @Override
    public Person findByPolicyIdWhenPolicyAndPersonIsOneToOne(Long policyId) {
        Person person = personRepository.findByPolicyId(policyId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return person;
    }

    //#region Mappers
    public static NaturalPersonJpaEntity mapToNaturalPersonJpaEntity(Person person) {
        NaturalPersonJpaEntity naturalPersonJpaEntity = NaturalPersonJpaEntity.builder()
                .id(person.getNaturalPerson().getId())
                .nit(person.getNit())
                .clientCode(person.getNaturalPerson().getClientCode())
                .clientEventual(person.getNaturalPerson().getClientEventual())
                .clientType(person.getNaturalPerson().getClientType())
                .name(person.getNaturalPerson().getName())
                .lastName(person.getNaturalPerson().getLastName())
                .motherLastName(person.getNaturalPerson().getMotherLastName())
                .marriedLastName(person.getNaturalPerson().getMarriedLastName())
                .maritalStatusIdc(person.getNaturalPerson().getMaritalStatusIdc())
                .documentType(person.getNaturalPerson().getDocumentType())
                .identificationNumber(person.getNaturalPerson().getIdentificationNumber())
                .complement(person.getNaturalPerson().getComplement())
                .extIdc(person.getNaturalPerson().getExtIdc())
                .birthDate(person.getNaturalPerson().getBirthDate())
                .genderIdc(person.getNaturalPerson().getGenderIdc())
                .profession(person.getNaturalPerson().getProfession())
                .workPlace(person.getNaturalPerson().getWorkPlace())
                .workTypeIdc(person.getNaturalPerson().getWorkTypeIdc())
                .position(person.getNaturalPerson().getPosition())
                .entryDate(person.getNaturalPerson().getEntryDate())
                .salary(person.getNaturalPerson().getSalary())
                .createdAt(person.getNaturalPerson().getCreatedAt())
                .lastModifiedAt(person.getNaturalPerson().getLastModifiedAt())
                .internalClientCode(person.getNaturalPerson().getInternalClientCode())
                .person(mapToPersonJpaEntity(person))
                .build();
        return naturalPersonJpaEntity;
    }

    public static JuridicalPersonJpaEntity mapToJuridicalPersonJpaEntity(Person person) {
        JuridicalPersonJpaEntity juridicalPersonJpaEntity = JuridicalPersonJpaEntity.builder()
                .id(person.getJuridicalPerson().getId())
                .nit(person.getNit())
                .businessTypeIdc(person.getJuridicalPerson().getBusinessTypeIdc())
                .name(person.getJuridicalPerson().getName())
                .webSite(person.getJuridicalPerson().getWebSite())
                .createdAt(person.getJuridicalPerson().getCreatedAt())
                .createdBy(person.getJuridicalPerson().getCreatedBy())
                .lastModifiedAt(person.getJuridicalPerson().getLastModifiedAt())
                .lastModifiedBy(person.getJuridicalPerson().getLastModifiedBy())
                .internalClientCode(person.getNaturalPerson().getInternalClientCode())
                .person(mapToPersonJpaEntity(person))
                .build();
        return juridicalPersonJpaEntity;
    }

    public static PersonJpaEntity mapToPersonJpaEntity(Person person) {
        PersonJpaEntity personJpaEntity = PersonJpaEntity.builder()
                .id(person.getId())
                .nationalityIdc(person.getNationalityIdc())
                .residenceIdc(person.getResidenceIdc())
                .activityIdc(person.getActivityIdc())
                .reference(person.getReference())
                .telephone(person.getTelephone())
                .email(person.getEmail())
                .holder(person.getHolder())
                .insured(person.getInsured())
                .createdAt(person.getCreatedAt())
                .lastModifiedAt(person.getLastModifiedAt())
                .build();

        return personJpaEntity;
    }


    public static JuridicalPersonJpaEntity mapToEntity(JuridicalPerson juridicalPerson) {
        return JuridicalPersonJpaEntity.builder()
                .id(juridicalPerson.getId())
                .businessTypeIdc(juridicalPerson.getBusinessTypeIdc())
                .name(juridicalPerson.getName())
                .webSite(juridicalPerson.getWebSite())
                .createdAt(juridicalPerson.getCreatedAt())
                .createdBy(juridicalPerson.getCreatedBy())
                .lastModifiedAt(juridicalPerson.getLastModifiedAt())
                .lastModifiedBy(juridicalPerson.getLastModifiedBy())
                .build();
    }

    //#endregion

    //#region Auxiliary Methods
    private String getPersonFilters(PersonDTO personDTO) {
        List<String> baseFilterList = new ArrayList<>();

        String joinChar = " AND ";
        if (!personDTO.getIdentificationNumber().isEmpty()) {
            baseFilterList.add(" np.identificationNumber = " + "'" + personDTO.getIdentificationNumber() + "'");
        }
        if (personDTO.getExtIdc() != 0) {
            baseFilterList.add(" np.extIdc = " + personDTO.getExtIdc());
        }
        if (!personDTO.getComplement().isEmpty()) {
            baseFilterList.add(" np.complement = " + "'" + personDTO.getComplement() + "'");
        }
        if (!personDTO.getNames().isEmpty()) {
            baseFilterList.add(" np.name LIKE " + "'%" + personDTO.getNames() + "%'");
        }
        if (!personDTO.getLastname().isEmpty()) {
            baseFilterList.add(" np.lastName LIKE " + "'%" + personDTO.getLastname() + "%'");
        }
        if (!personDTO.getMothersLastname().isEmpty()) {
            baseFilterList.add(" np.motherLastName LIKE " + "'%" + personDTO.getMothersLastname() + "%'");
        }
        if (!personDTO.getMarriedLastname().isEmpty()) {
            baseFilterList.add(" np.marriedLastName LIKE " + "'%" + personDTO.getMarriedLastname() + "%'");
        }

        return String.join(joinChar, baseFilterList);
    }

    //#endregion

}
