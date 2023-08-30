package com.scfg.core.application.service;

import com.scfg.core.application.port.in.NewPersonUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.Telephone;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.vin.Account;
import com.scfg.core.domain.person.NewPerson;
import com.scfg.core.domain.person.PersonRole;
import com.scfg.core.domain.person.ReferencePerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewPersonService implements NewPersonUseCase {

    private final NewPersonPort newPersonPort;
    private final TelephonePort telephonePort;
    private final DirectionPort directionPort;
    private final AccountPort accountPort;
    private final ReferencePersonPort referencePersonPort;
    private final PersonRolePort personRolePort;

    @Override
    public Object searchPerson(Long documentTypeIdc, String identificationNumber, String name) {
        return newPersonPort.searchPerson(documentTypeIdc, identificationNumber, name);
    }

    public boolean validateIdentificationNumber(String identificationNumber) {
        return newPersonPort.findByIdentificationNumber(identificationNumber);
    }

    public NewPerson getById(long newPersonId) {
        return newPersonPort.findById(newPersonId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    public Boolean saveOrUpdate(NewPerson newPerson) {
        try {
            long personId = saveOrUpdatePerson(newPerson);

            saveOrUpdateTelephones(newPerson, personId);
            saveOrUpdateDirections(newPerson, personId);
            saveOrUpdateAccounts(newPerson, personId);
            handleReferencePersonOrRole(newPerson, personId);

            return personId > 0;

        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private long saveOrUpdatePerson(NewPerson newPerson) {
        NewPerson newPersonAux = newPersonPort.findById(newPerson.getId());
        long personId = 0L;

        if (newPersonAux != null) {
            newPersonAux.setDocumentTypeIdc(newPerson.getDocumentTypeIdc());
            newPersonAux.setIdentificationNumber(newPerson.getIdentificationNumber());
            newPersonAux.setName(newPerson.getName());
            newPersonAux.setLastName(newPerson.getLastName());
            newPersonAux.setMotherLastName(newPerson.getMotherLastName());
            newPersonAux.setMarriedLastName(newPerson.getMarriedLastName());
            newPersonAux.setGenderIdc(newPerson.getGenderIdc());
            newPersonAux.setMaritalStatusIdc(newPerson.getMaritalStatusIdc());
            newPersonAux.setSpouse(newPerson.getSpouse());
            newPersonAux.setBirthDate(newPerson.getBirthDate());
            newPersonAux.setBirthPlaceIdc(newPerson.getBirthPlaceIdc());
            newPersonAux.setNationalityIdc(newPerson.getNationalityIdc());
            newPersonAux.setResidencePlaceIdc(newPerson.getResidencePlaceIdc());
            newPersonAux.setActivityIdc(newPerson.getActivityIdc());
            newPersonAux.setProfessionIdc(newPerson.getProfessionIdc());
            newPersonAux.setWorkerTypeIdc(newPerson.getWorkerTypeIdc());
            newPersonAux.setWorkerCompany(newPerson.getWorkerCompany());
            newPersonAux.setWorkEntryYear(newPerson.getWorkEntryYear());
            newPersonAux.setWorkPosition(newPerson.getWorkPosition());
            newPersonAux.setMonthlyIncomeRangeIdc(newPerson.getMonthlyIncomeRangeIdc());
            newPersonAux.setYearlyIncomeRangeIdc(newPerson.getYearlyIncomeRangeIdc());
            newPersonAux.setBusinessTypeIdc(newPerson.getBusinessTypeIdc());
            newPersonAux.setBusinessRegistrationNumber(newPerson.getBusinessRegistrationNumber());
            newPersonAux.setEmail(newPerson.getEmail());
            newPersonAux.setEventualClient(newPerson.getEventualClient());
            newPersonAux.setInternalClientCode(newPerson.getInternalClientCode());
            newPersonAux.setInstitutionalClientCode(newPerson.getInstitutionalClientCode());
            newPersonAux.setClientCode(newPerson.getClientCode());
            newPersonAux.setClientType(newPerson.getClientType());

            personId = newPersonPort.saveOrUpdate(newPersonAux);


        } else {
            if (validateIdentificationNumber(newPerson.getIdentificationNumber())) {
                throw new OperationException("El número de identificación ya existe");
            }
            personId = newPersonPort.saveOrUpdate(newPerson);
        }
        return personId;
    }

    private void saveOrUpdateTelephones(NewPerson newPerson, long personId) {
        if (newPerson != null && personId > 0) {
            if (newPerson.getTelephones() != null && !newPerson.getTelephones().isEmpty()) {
                List<Telephone> actTelephoneList = telephonePort.getAllByNewPersonId(personId);
                List<Telephone> newTelephones = new ArrayList<>();
                if (actTelephoneList != null) {
                    for (Telephone obj : newPerson.getTelephones()) {
                        if ((!actTelephoneList.stream().map(o -> o.getNumber().equals(obj.getNumber())).findAny().isPresent())) {
                            obj.setNewPersonId(personId);
                            obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                            newTelephones.add(obj);
                        }
                    }
                } else {
                    newTelephones.addAll(newPerson.getTelephones());
                }

                if (!newTelephones.isEmpty()) {
                    telephonePort.saveOrUpdateAll(newTelephones);
                }
            }
        }
    }

    private void saveOrUpdateDirections(NewPerson newPerson, long personId) {
        if (newPerson != null && personId > 0) {
            if (!newPerson.getDirections().isEmpty()) {
                List<Direction> actDirectionList = directionPort.findAllByNewPersonId(personId);
                List<Direction> newDirections = new ArrayList<>();
                if (actDirectionList != null) {
                    for (Direction obj : newPerson.getDirections()) {
                        if ((!actDirectionList.stream().map(o -> o.getDescription().equals(obj.getDescription())).findAny().isPresent()) &&
                                (!actDirectionList.stream().map(o -> o.getDirectionTypeIdc().equals(obj.getDirectionTypeIdc())).findAny().isPresent())) {
                            obj.setNewPersonId(personId);
                            obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                            newDirections.add(obj);
                        }
                    }
                } else {
                    newDirections.addAll(newPerson.getDirections());
                }
                if (!newDirections.isEmpty()) {
                    directionPort.saveAllDirection(newDirections);
                }
            }
        }
    }

    private void saveOrUpdateAccounts(NewPerson newPerson, long personId) {
        if (newPerson != null && personId > 0) {
            if (newPerson.getAccounts() != null && !newPerson.getAccounts().isEmpty()) {
                List<Account> actAccountList = accountPort.findAllByNewPersonId(personId);
                List<Account> newAccounts = new ArrayList<>();
                if (actAccountList != null) {
                    for (Account obj : newPerson.getAccounts()) {
                        if (!actAccountList.stream().map(o -> o.getAccountNumber().equals(obj.getAccountNumber())).findAny().isPresent()) {
                            obj.setNewPersonId(personId);
                            obj.setNewPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                            newAccounts.add(obj);
                        }
                    }
                } else {
                    newAccounts.addAll(newPerson.getAccounts());
                }
                if (!newAccounts.isEmpty()) {
                    accountPort.saveOrUpdateAll(newAccounts);
                }
            }
        }
    }

    private void handleReferencePersonOrRole(NewPerson newPerson, long personId) {
        if (newPerson != null && personId > 0) {
            if (newPerson.getDocumentTypeIdc() != ClassifierEnum.NIT_IdentificationType.getReferenceCode()) {
                List<ReferencePerson> newReferencePerson = new ArrayList<>();
                if (newPerson.getReferencePersonInfo() != null && !newPerson.getReferencePersonInfo().isEmpty()) {
                    for (ReferencePerson obj : newPerson.getReferencePersonInfo()) {
                        obj.setPersonId(personId);
                        newReferencePerson.add(obj);
                    }

                }
                if (newPerson.getMaritalStatusIdc() == ClassifierEnum.MARRIED_STATUS.getReferenceCode() && newPerson.getGenderIdc() == ClassifierEnum.FEMALE.getReferenceCode()) {
                    if (newPerson.getSpouse() != null) {
                        newPerson.getSpouse().setReferenceRelationshipIdc((int) ClassifierEnum.SPOUSE.getReferenceCode());
                        newPerson.getSpouse().setPersonId(personId);
                        newReferencePerson.add(newPerson.getSpouse());
                    }
                }
                if (!newReferencePerson.isEmpty()) {
                    referencePersonPort.saveOrUpdateAll(newReferencePerson);
                }

            } else {
                if (!newPerson.getRelatedPersons().isEmpty()) {
                    List<PersonRole> newPersonRole = new ArrayList<>();
                    for (PersonRole obj : newPerson.getRelatedPersons()) {
                        obj.setPersonId((personId));
                        newPersonRole.add(obj);
                    }
                    if (!newPersonRole.isEmpty()) {
                        personRolePort.saveOrUpdateAll(newPersonRole);
                    }

                } else {
                    throw new OperationException("Una persona jurídica debe tener al menos una persona relacionada");
                }

            }
        }

    }
}

