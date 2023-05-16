package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PersonUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.SearchPepDTO;
import com.scfg.core.domain.dto.credicasas.CurrentAmountRequestDTO;
import com.scfg.core.domain.dto.credicasas.SearchClientDTO;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService implements PersonUseCase {

    private final PersonPort personPort;
    private final PEPPort pepPort;
    private final GeneralRequestPort generalRequestPort;
    private final DirectionPort directionPort;
    private final PlanPort planPort;
    private final TelephonePort telephonePort;

    private final CoveragePolicyItemPort coveragePolicyItemPort;

    @Override
    public List<Person> getAll() {
        return personPort.findAll();
    }

    @Override
    public List<Person> getAllByAssignedGroup(Integer assignedGroup) {
        return personPort.findAllByAssignedGroup(assignedGroup);
    }

    @Override
    public Person getById(long personId) {
        return personPort.findById(personId);
    }

    @Override
    public Person getByIdWitDirections(long personId) {
        Person person = personPort.findById(personId);
        if (person != null) {
            person.setDirections(directionPort.findAllByPersonId(personId));
        }
        return person;
    }

    @Override
    public Person getByIdentificationNumberAndType(String identificationNumber, Integer documentType) {
        return personPort.findByIdentificationNumberAndType(identificationNumber, documentType);
    }

    @Override
    public List<SearchClientDTO> getAllByParametersClf(PersonDTO personDTO) {

        List<Person> personList = personPort.findAllByFilters(personDTO);

        if (personList.isEmpty()) {
            boolean isPEP = pepPort.existsByIdentificationNumberOrName(new SearchPepDTO(personDTO));
            List<SearchClientDTO> searchClientDTOS = new ArrayList<>();
            searchClientDTOS.add(SearchClientDTO.builder()
                    .isPEP(isPEP)
                    .exists(false)
                    .currentAmount((double) 0)
                    .person(null)
                    .wasRejected(false)
                    .build());
            return searchClientDTOS;
        }

        List<SearchClientDTO> searchClientDTOS = new ArrayList<>();

        personList.forEach(person -> {
            searchClientDTOS.add(SearchClientDTO.builder()
                    .isPEP(false)
                    .exists(true)
                    .currentAmount((double) 0)
                    .person(person)
                    .wasRejected(false)
                    .build());
        });
        return searchClientDTOS;
    }

    @Override
    public SearchClientDTO getByParametersClf(Person person) {

        boolean isPEP = pepPort.existsByIdentificationNumberOrName(new SearchPepDTO(person.getNaturalPerson()));

        List<CurrentAmountRequestDTO> generalRequestList = generalRequestPort.findAllCurrentAmountGeneralRequestByPersonId(person.getId());
        List<Plan> planList = planPort.getPlanByFinancialGroup(BusinessGroupEnum.CREDICASAS.getValue());
        List<Direction> directionList = directionPort.findAllByPersonId(person.getId());
        person.setDirections(directionList);

        Set<Long> planIds = planList.stream().map(Plan::getId).collect(Collectors.toSet());

        List<CoveragePolicyItem> coveragePolicyItemList = coveragePolicyItemPort.findAllByPersonIdGEL(person.getId(), planList);
        boolean wasRejected = false;
        if (generalRequestList.stream().anyMatch(o ->  planIds.contains(o.getPlanId())
                && o.getRequestStatusIdc().equals(RequestStatusEnum.REJECTED.getValue()) ||
                o.getExclusionComment() != null && o.getExclusionComment().length() > 0) ||
                coveragePolicyItemList.stream().anyMatch(o -> o.getAdditionalPremiumPerPercentage() > 0)) {
            wasRejected = true;
        }


        double currentAmount = generalRequestList.stream().filter(o -> o.getCurrentAmount() != null && planIds.contains(o.getPlanId())
                        && (o.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue() || o.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()))
                .mapToDouble(CurrentAmountRequestDTO::getCurrentAmount).sum();

        return SearchClientDTO.builder()
                .exists(true)
                .isPEP(isPEP)
                .currentAmount(currentAmount)
                .person(person)
                .wasRejected(wasRejected)
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean save(Person person) {
        return personPort.saveOrUpdate(person) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean update(Person person) {
        return personPort.saveOrUpdate(person) > 0;
    }

    @Override
    public Boolean delete(Person person) {
        return null;
    }

    @Override
    public Person getJuridicalPersonByNitNumber(Long nitNumber) {
        Person person = personPort.findByNitNumber(nitNumber);
        List<Direction> directionList = directionPort.findAllByPersonId(person.getId());
        person.setDirections(directionList);
        List<Telephone> telephoneList = telephonePort.getAllByPersonId(person.getId());
        person.setTelephones(telephoneList);
        return person;
    }

    @Override
    public List<Object> searchPerson(Long docType, String documentNumber, String name) {
        return personPort.searchPerson(docType,documentNumber, name);
    }
}
