package com.scfg.core.application.service;

import com.scfg.core.application.port.in.NewPersonUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.Telephone;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.vin.Account;
import com.scfg.core.domain.person.NewPerson;
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

    @Override
    public Object searchPerson(Long documentTypeIdc, String identificationNumber, String name) {
        return newPersonPort.searchPerson(documentTypeIdc, identificationNumber, name);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean save(NewPerson newPerson) {
//        try {
            Long personId = newPersonPort.saveOrUpdate(newPerson);
            List<Account> AccountAux = new ArrayList<>();
            List<Telephone> TelephoneAux = new ArrayList<>();
            List<Direction> DirectionAux = new ArrayList<>();
            List<ReferencePerson> referencePersonAux = new ArrayList<>();
            for (Telephone obj : newPerson.getTelephones()) {
                obj.setNewPersonId(personId);
                obj.setPersonId(null);
                TelephoneAux.add(obj);
            }
            telephonePort.saveOrUpdateAll(TelephoneAux);

            for (Account obj : newPerson.getAccounts()) {
                obj.setNewPersonId(personId);
                obj.setPersonId(null);
                AccountAux.add(obj);
            }
            accountPort.saveOrUpdateAll(AccountAux);

            for (Direction obj : newPerson.getDirections()) {
                obj.setNewPersonId(personId);
                obj.setPersonId(null);
                DirectionAux.add(obj);
            }
            directionPort.saveAllDirection(DirectionAux);

            if(newPerson.getDocumentTypeIdc() != ClassifierEnum.NIT_IdentificationType.getReferenceCode()){
                if (!newPerson.getReferencePersonInfo().isEmpty() || newPerson.getReferencePersonInfo().size() > 0) {
                    for (ReferencePerson obj : newPerson.getReferencePersonInfo()) {

                        obj.setPersonId(personId);
                        referencePersonAux.add(obj);
                    }
                }
                if (newPerson.getMaritalStatusIdc() == ClassifierEnum.MARRIED_STATUS.getReferenceCode()) {
                    newPerson.getSpouse().setReferenceRelationshipIdc((int) ClassifierEnum.SPOUSE.getReferenceCode());
                    newPerson.getSpouse().setPersonId(personId);
                    referencePersonAux.add(newPerson.getSpouse());
                }
                referencePersonPort.saveOrUpdateAll(referencePersonAux);
            } else {

            }



            return personId > 0;
//        } catch (Exception e) {
//            e.getMessage();
//            return false;
//        }
    }
}
