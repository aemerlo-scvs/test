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
            NewPerson newPersonAux = newPersonPort.findById(newPerson.getId());
            long personId = 0L;

            //#region personas
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

            //#endregion personas

            //#region secciones del módulo de personas
//            if (!newPerson.getTelephones().isEmpty()) {
//                    List<Telephone> telephoneList = new ArrayList<>();
//                    for (Telephone obj : newPerson.getTelephones()) {
//                        obj.setNewPersonId(personId);
//                        obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
//                        telephoneList.add(obj);
//                    }
//                    telephonePort.saveOrUpdateAll(telephoneList);
//            }

            // Si la lista de teléfonos no está vacía
            if (!newPerson.getTelephones().isEmpty()) {
                List<Telephone> actTelephoneList =  telephonePort.getAllByPersonId(personId);

                // Listas para nuevos teléfonos y teléfonos a actualizar
                List<Telephone> telephonesToUpdate = new ArrayList<>();
                List<Telephone> newTelephones = new ArrayList<>();

                // Si hay teléfonos ya asociados
                if (actTelephoneList != null) {
                    for (Telephone obj : newPerson.getTelephones()) {
                        if (actTelephoneList.contains(obj)) {
                            // Aquí puedes agregar validaciones adicionales si lo necesitas
                            telephonesToUpdate.add(obj);
                        } else {
                            obj.setNewPersonId(personId);
                            obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                            newTelephones.add(obj);
                        }
                    }
                    // Guarda o actualiza los teléfonos ya existentes
                    if (!telephonesToUpdate.isEmpty()) {
                        telephonePort.saveOrUpdateAll(telephonesToUpdate);
                    }
                } else {
                    // Si no hay teléfonos asociados, todos los teléfonos son nuevos
                    newTelephones.addAll(newPerson.getTelephones());
                }

                // Guarda los nuevos teléfonos
                if (!newTelephones.isEmpty()) {
                    telephonePort.saveOrUpdateAll(newTelephones);
                }
            }



//            if (!newPerson.getDirections().isEmpty()) {
//                List<Direction> directionList = new ArrayList<>();
//                for (Direction obj : newPerson.getDirections()) {
//                    obj.setNewPersonId(personId);
//                    obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
//                    directionList.add(obj);
//                }
//                directionPort.saveAllDirection(directionList);
//            }


            // Si la lista de teléfonos no está vacía
            if (!newPerson.getDirections().isEmpty()) {
                List<Direction> actDirectionList =  directionPort.findAllByNewPersonId(personId);

                // Listas para nuevas direcciones y direcciones a actualizar
                List<Direction> directionsToUpdate = new ArrayList<>();
                List<Direction> newDirections = new ArrayList<>();

                // Si hay teléfonos ya asociados
                if (actDirectionList != null) {
                    for (Direction obj : newPerson.getDirections()) {
                        Direction obj2 = obj;
//                        if (actDirectionList.contains(obj.getDescription())) {
                        if (actDirectionList.stream().map(o -> o.getDescription().equals(obj2.getDescription())).findAny().isPresent()) {
                            directionsToUpdate.add(obj);
                        } else {
                            obj.setNewPersonId(personId);
                            //todo validar que personId sea null
                            obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                            newDirections.add(obj);
                        }
                    }
                    // Guarda o actualiza los teléfonos ya existentes
                    if (!directionsToUpdate.isEmpty()) {
                        directionPort.saveAllDirection(directionsToUpdate);
                    }
                } else {
                    // Si no hay teléfonos asociados, todos los teléfonos son nuevos
                    newDirections.addAll(newPerson.getDirections());
                }

                // Guarda los nuevos teléfonos
//                if (!newDirections.isEmpty()) {
//                    directionPort.saveAllDirection(newPerson.getDirections());
//                }
            }

//            if (!newPerson.getDirections().isEmpty()) {
//                List<Direction> directionList = new ArrayList<>();
//                for (Direction obj : newPerson.getDirections()) {
//                    obj.setNewPersonId(personId);
//                    obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
//                    directionList.add(obj);
//                }
//                directionPort.saveAllDirection(directionList);
//            }



            if (!newPerson.getAccounts().isEmpty()) {
                List<Account> accountList = new ArrayList<>();
                for (Account obj : newPerson.getAccounts()) {
                    obj.setNewPersonId(personId);
                    obj.setPersonId((newPerson.getPersonId() != 0) ? newPerson.getPersonId() : null);
                    accountList.add(obj);
                }
                accountPort.saveOrUpdateAll(accountList);
            }

            //#endregion

            //#region validaciones

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
                if (!newPerson.getRelatedPersons().isEmpty()) {
                    List<PersonRole> personRoleList = new ArrayList<>();
                    for (PersonRole obj : newPerson.getRelatedPersons()) {
                        obj.setPersonId((personId));
                        personRoleList.add(obj);
                    }
                    personRolePort.saveOrUpdateAll(personRoleList);

                } else {
                    throw new OperationException("Una persona jurídica debe tener al menos una persona relacionada");
                }

            }
            //#endregion validaciones

            return personId > 0;

        } catch (OperationException e) {
            throw new OperationException(e.getMessage());
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}

