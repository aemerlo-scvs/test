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
    @Override
    public Boolean save(NewPerson newPerson) {
        try {
            NewPerson newPersonAux = newPersonPort.findById(newPerson.getId());

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

                newPersonPort.saveOrUpdate(newPersonAux);

                // todo validar existencia antes de registrar
                if (!newPerson.getTelephones().isEmpty()) {
                    List<Telephone> telephoneList = new ArrayList<>();
                    for (Telephone obj : newPerson.getTelephones()) {
                        obj.setNewPersonId(newPerson.getId());
                        obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                        telephoneList.add(obj);
                    }
                    telephonePort.saveOrUpdateAll(telephoneList);
                }

                if (!newPerson.getDirections().isEmpty()) {
                    List<Direction> directionList = new ArrayList<>();
                    for (Direction obj : newPerson.getDirections()) {
                        obj.setNewPersonId(newPerson.getId());
                        obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                        directionList.add(obj);
                    }
                    directionPort.saveAllDirection(directionList);

                }

                if (!newPerson.getAccounts().isEmpty()) {
                    newPersonAux.setAccounts(newPerson.getAccounts());
                    accountPort.saveOrUpdateAll(newPersonAux.getAccounts());
                }

                if (newPerson.getDocumentTypeIdc() != ClassifierEnum.NIT_IdentificationType.getReferenceCode()) {
                    if (!newPerson.getReferencePersonInfo().isEmpty()) {
                        newPersonAux.setReferencePersonInfo(newPerson.getReferencePersonInfo());
                    }

                    //Es mujer y casada
                    if (newPerson.getMaritalStatusIdc() == ClassifierEnum.MARRIED_STATUS.getReferenceCode() && newPerson.getGenderIdc() == ClassifierEnum.FEMALE.getReferenceCode()) {
                        newPersonAux.getSpouse().setReferenceRelationshipIdc((int) ClassifierEnum.SPOUSE.getReferenceCode());
                        newPersonAux.setSpouse(newPerson.getSpouse());
                    }
                } else {
                    if (!newPerson.getRelatedPersons().isEmpty()) {
                        newPersonAux.setRelatedPersons(newPerson.getRelatedPersons());
                    } else {
                        throw new OperationException("Una persona jurídica debe tener al menos una persona relacionada");
                    }
                }




                return newPersonAux.getId() > 0;

            } else {
                if (validateIdentificationNumber(newPerson.getIdentificationNumber())) {
                    throw new OperationException("El número de identificación ya existe");
                }

                Long personId = newPersonPort.saveOrUpdate(newPerson);

                if (!newPerson.getTelephones().isEmpty()) {
                    List<Telephone> telephoneList = new ArrayList<>();
                    for (Telephone obj : newPerson.getTelephones()) {
                        obj.setNewPersonId(personId);
                        obj.setPersonId(newPerson.getPersonId());
                        telephoneList.add(obj);
                    }
                    telephonePort.saveOrUpdateAll(telephoneList);
                }

                if (!newPerson.getDirections().isEmpty()) {
                    List<Direction> directionList = new ArrayList<>();
                    for (Direction obj : newPerson.getDirections()) {
                        obj.setNewPersonId(personId);
                        obj.setPersonId(newPerson.getPersonId());
                        directionList.add(obj);
                    }
                    directionPort.saveAllDirection(directionList);
                }

                if (!newPerson.getAccounts().isEmpty()) {
                    List<Account> accountList = new ArrayList<>();
                    for (Account obj : newPerson.getAccounts()) {
                        obj.setNewPersonId(personId);
                        obj.setPersonId(newPerson.getPersonId());
                        accountList.add(obj);
                    }
                    accountPort.saveOrUpdateAll(accountList);
                }

                if (newPerson.getDocumentTypeIdc() != ClassifierEnum.NIT_IdentificationType.getReferenceCode()) {
                    List<ReferencePerson> referencePersonList = new ArrayList<>();
                    if (!newPerson.getReferencePersonInfo().isEmpty()) {
                        for (ReferencePerson obj : newPerson.getReferencePersonInfo()) {
                            obj.setPersonId(personId);
                            referencePersonList.add(obj);
                        }
                    }
                    if (newPerson.getMaritalStatusIdc() == ClassifierEnum.MARRIED_STATUS.getReferenceCode() && newPerson.getGenderIdc() == ClassifierEnum.FEMALE.getReferenceCode()) {
                        newPerson.getSpouse().setReferenceRelationshipIdc((int) ClassifierEnum.SPOUSE.getReferenceCode());
                        newPerson.getSpouse().setPersonId(personId);
                        referencePersonList.add(newPerson.getSpouse());
                    }
                    referencePersonPort.saveOrUpdateAll(referencePersonList);
                } else {
                    List<PersonRole> personRoleList = new ArrayList<>();
                    if (!newPerson.getRelatedPersons().isEmpty()) {
                        for (PersonRole obj : newPerson.getRelatedPersons()) {
                            obj.setPersonId((personId));
                            personRoleList.add(obj);
                        }
                        personRolePort.saveOrUpdateAll(personRoleList);

                    } else {
                        throw new OperationException("Una persona jurídica debe tener al menos una persona relacionada");
                    }

                }

                return personId > 0;
            }

        } catch (OperationException e) {
            throw new OperationException(e.getMessage());
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}
