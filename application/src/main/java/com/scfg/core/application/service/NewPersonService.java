package com.scfg.core.application.service;
import com.scfg.core.application.port.in.NewPersonUseCase;
import com.scfg.core.application.port.out.NewPersonPort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.person.NewPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewPersonService implements NewPersonUseCase {

    private final NewPersonPort newPersonPort;
    @Override
    public Object searchPerson(Long docType, String documentNumber, String name) {
        return newPersonPort.searchPerson(docType,documentNumber, name);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean save(NewPerson newPerson) {
        return newPersonPort.saveOrUpdate(newPerson) > 0;
    }
}
