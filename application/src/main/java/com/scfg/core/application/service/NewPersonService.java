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

    public boolean validateIdentificationNumber(String identificationNumber){
        return newPersonPort.findByIdentificationNumber(identificationNumber);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean save(NewPerson newPerson) {
        try {
            if(!validateIdentificationNumber(newPerson.getIdentificationNumber())){
                Long personId = newPersonPort.saveOrUpdate(newPerson);
                List<Account> accountList = new ArrayList<>();
                List<Telephone> telephoneList = new ArrayList<>();
                List<Direction> directionList = new ArrayList<>();
                List<ReferencePerson> referencePersonList = new ArrayList<>();
                List<PersonRole> personRoleList = new ArrayList<>();

                for (Telephone obj : newPerson.getTelephones()) {
                    obj.setNewPersonId(personId);
                    obj.setPersonId(null);
                    telephoneList.add(obj);
                }
                telephonePort.saveOrUpdateAll(telephoneList);

                for (Direction obj : newPerson.getDirections()) {
                    obj.setNewPersonId(personId);
                    obj.setPersonId(null);
                    directionList.add(obj);
                }
                directionPort.saveAllDirection(directionList);

                if (!newPerson.getAccounts().isEmpty()) {
                    for (Account obj : newPerson.getAccounts()) {
                        obj.setNewPersonId(personId);
                        obj.setPersonId(null);
                        accountList.add(obj);
                    }
                    accountPort.saveOrUpdateAll(accountList);
                }

                if (newPerson.getDocumentTypeIdc() != ClassifierEnum.NIT_IdentificationType.getReferenceCode()) {
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
                        for (PersonRole obj : newPerson.getRelatedPersons()) {
                            obj.setPersonId((personId));
                            personRoleList.add(obj);
                        }
                        personRolePort.saveOrUpdateAll(personRoleList);

                    } else {
//                        Long personId = newPersonPort.saveOrUpdate(newPerson);
                        throw new OperationException("Una persona jurídica debe tener al menos una persona relacionada");
                    }

                }
                return personId > 0;
            } else {
                //obj que existe
                throw new OperationException("El número de identificación ya existe");
            }

        } catch (OperationException e) {
            throw new OperationException(e.getMessage());
        }
        catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}
