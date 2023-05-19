package com.scfg.core.adapter.persistence.person;

import com.scfg.core.adapter.persistence.juridicalPerson.JuridicalPersonJpaEntity;
import com.scfg.core.adapter.persistence.naturalPerson.NaturalPersonJpaEntity;
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

    private final EntityManager em;

    @Override
    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();
        List<PersonJpaEntity> listAux = personRepository.findAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        listAux.forEach(item -> {
            list.add(mapToDomain(item));
        });
        return list;
    }

    @Override
    public Person findById(long personId) {
        PersonJpaEntity personJpaEntity = personRepository.customFindById(personId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return (personJpaEntity != null) ? mapToDomain(personJpaEntity) : null;
    }

    @Override
    public Person findByIdentificationNumberAndType(String identificationNumber, Integer documentType) {
        PersonJpaEntity personJpaEntity = personRepository.findByNaturalPersonIdentificationNumberAndDocumentType(identificationNumber, documentType);
        return (personJpaEntity != null) ? mapToDomain(personJpaEntity) : null;
    }

    @Override
    public List<Person> findAllByFilters(PersonDTO personDTO) {
        String query = personRepository.getFindAllByFiltersBaseQuery() + getPersonFilters(personDTO);

        List<PersonJpaEntity> personJpaEntityList = em.createQuery(query).getResultList();

        em.close();
        return personJpaEntityList.stream().map(o -> new ModelMapper().map(o, Person.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean existsIdentificationNumber(String identificationNumber) {
        PersonJpaEntity personJpaEntity = personRepository.findByNaturalPersonIdentificationNumber(identificationNumber);
        return personJpaEntity != null;
    }
    
    @Override
    public long saveOrUpdate(Person person) {
        PersonJpaEntity personJpaEntity = mapToJpaEntity(person);
        personJpaEntity = personRepository.save(personJpaEntity);
        return personJpaEntity.getId();
    }

    @Override
    public List<Person> findAllByAssignedGroup(Integer assignedGroup) {
        List<PersonJpaEntity> personGroups = personRepository.findAllByAssignedGroupIdc(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),assignedGroup);
        List<Person> listAux = new ArrayList<>();
        personGroups.forEach(item -> {
            listAux.add(mapToDomain(item));
        });
        return listAux;
    }

    @Override
    public List<Person> findAllByListOfPersonId(List<Long> personIdList) {
        List<PersonJpaEntity> personJpaEntityList = personRepository.findAllByListId(personIdList,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return personJpaEntityList.stream().map(o -> new ModelMapper().map(o, Person.class)).collect(Collectors.toList());
    }

    @Override
    public Person findByNitNumber(Long nitNumber) {
        PersonJpaEntity person = personRepository.findByNitNumber(nitNumber,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return new ModelMapper().map(person, Person.class);
    }

    @Override
    public Person findByPolicyIdWhenPolicyAndPersonIsOneToOne(Long policyId) {
        PersonJpaEntity person = personRepository.findByPolicyId(policyId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(person);
    }

    //#region Mappers

    public static PersonJpaEntity mapToJpaEntity(Person person) {
        PersonJpaEntity personJpaEntity = PersonJpaEntity.builder()
                .id(person.getId())
                .nit(person.getNit())
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

        if (person.getNaturalPerson() != null) {
            personJpaEntity.setNaturalPerson(mapToJpaEntity(person.getNaturalPerson()));
        }

        if (person.getJuridicalPerson() != null) {
            personJpaEntity.setJuridicalPerson(mapToEntity(person.getJuridicalPerson()));
        }

        return personJpaEntity;
    }

    public static NaturalPersonJpaEntity mapToJpaEntity(NaturalPerson naturalPerson) {
        return NaturalPersonJpaEntity.builder()
                .id(naturalPerson.getId())
                .clientCode(naturalPerson.getClientCode())
                .clientEventual(naturalPerson.getClientEventual())
                .clientType(naturalPerson.getClientType())
                .name(naturalPerson.getName())
                .lastName(naturalPerson.getLastName())
                .motherLastName(naturalPerson.getMotherLastName())
                .marriedLastName(naturalPerson.getMarriedLastName())
                .maritalStatusIdc(naturalPerson.getMaritalStatusIdc())
                .documentType(naturalPerson.getDocumentType())
                .identificationNumber(naturalPerson.getIdentificationNumber())
                .complement(naturalPerson.getComplement())
                .extIdc(naturalPerson.getExtIdc())
                .birthDate(naturalPerson.getBirthDate())
                .genderIdc(naturalPerson.getGenderIdc())
                .profession(naturalPerson.getProfession())
                .workPlace(naturalPerson.getWorkPlace())
                .workTypeIdc(naturalPerson.getWorkTypeIdc()) // TODO: Revisar que funcione para SEPELIO
                .position(naturalPerson.getPosition())
                .entryDate(naturalPerson.getEntryDate())
                .salary(naturalPerson.getSalary())
                .createdAt(naturalPerson.getCreatedAt())
                .lastModifiedAt(naturalPerson.getLastModifiedAt())
                .build();

        // return naturalPersonJpaEntity;
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

    public static Person mapToDomain(PersonJpaEntity personJpaEntity) {
        Person person = Person.builder()
                .id(personJpaEntity.getId())
                .nit(personJpaEntity.getNit())
                .nationalityIdc(personJpaEntity.getNationalityIdc())
                .residenceIdc(personJpaEntity.getResidenceIdc())
                .activityIdc(personJpaEntity.getActivityIdc())
                .reference(personJpaEntity.getReference())
                .telephone(personJpaEntity.getTelephone())
                .email(personJpaEntity.getEmail())
                .holder(personJpaEntity.getHolder())
                .insured(personJpaEntity.getInsured())
                // .naturalPerson(new NaturalPerson())
                // .juridicalPerson(new JuridicalPerson())
                .createdAt(personJpaEntity.getCreatedAt())
                .lastModifiedAt(personJpaEntity.getLastModifiedAt())
                .build();

        if (personJpaEntity.getNaturalPerson() != null) {
            person.setNaturalPerson(mapToDomain(personJpaEntity.getNaturalPerson()));
        }

        if (personJpaEntity.getJuridicalPerson() != null) {
            person.setJuridicalPerson(mapToDomain(personJpaEntity.getJuridicalPerson()));
        }

        return person;
    }

    public static NaturalPerson mapToDomain(NaturalPersonJpaEntity naturalPersonJpaEntity) {
        return NaturalPerson.builder()
                .id(naturalPersonJpaEntity.getId())
                .clientCode(naturalPersonJpaEntity.getClientCode())
                .clientEventual(naturalPersonJpaEntity.getClientEventual())
                .clientType(naturalPersonJpaEntity.getClientType())
                .name(naturalPersonJpaEntity.getName())
                .lastName(naturalPersonJpaEntity.getLastName())
                .motherLastName(naturalPersonJpaEntity.getMotherLastName())
                .marriedLastName(naturalPersonJpaEntity.getMarriedLastName())
                .maritalStatusIdc(naturalPersonJpaEntity.getMaritalStatusIdc())
                .documentType(naturalPersonJpaEntity.getDocumentType())
                .identificationNumber(naturalPersonJpaEntity.getIdentificationNumber())
                .complement(naturalPersonJpaEntity.getComplement())
                .extIdc(naturalPersonJpaEntity.getExtIdc())
                .birthDate(naturalPersonJpaEntity.getBirthDate())
                .genderIdc(naturalPersonJpaEntity.getGenderIdc())
                .profession(naturalPersonJpaEntity.getProfession())
                .workPlace(naturalPersonJpaEntity.getWorkPlace())
                .workTypeIdc(naturalPersonJpaEntity.getWorkTypeIdc()) // TODO: Revisar que funcione para SEPELIO
                .position(naturalPersonJpaEntity.getPosition())
                .entryDate(naturalPersonJpaEntity.getEntryDate())
                .salary(naturalPersonJpaEntity.getSalary())
                .createdAt(naturalPersonJpaEntity.getCreatedAt())
                .lastModifiedAt(naturalPersonJpaEntity.getLastModifiedAt())
                .build();
    }

    public static JuridicalPerson mapToDomain(JuridicalPersonJpaEntity juridicalPersonJpaEntity) {
        return JuridicalPerson.builder()
                .id(juridicalPersonJpaEntity.getId())
                .businessTypeIdc(juridicalPersonJpaEntity.getBusinessTypeIdc())
                .name(juridicalPersonJpaEntity.getName())
                .webSite(juridicalPersonJpaEntity.getWebSite())
                .createdAt(juridicalPersonJpaEntity.getCreatedAt())
                .createdBy(juridicalPersonJpaEntity.getCreatedBy())
                .lastModifiedAt(juridicalPersonJpaEntity.getLastModifiedAt())
                .lastModifiedBy(juridicalPersonJpaEntity.getLastModifiedBy())
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
