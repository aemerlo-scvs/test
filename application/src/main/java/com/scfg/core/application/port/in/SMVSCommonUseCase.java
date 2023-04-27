package com.scfg.core.application.port.in;

import com.scfg.core.domain.person.Person;
import com.scfg.core.domain.smvs.IngressFormDTO;
import com.scfg.core.domain.smvs.SMVSResponseDTO;

public interface SMVSCommonUseCase {

    SMVSResponseDTO verifyActivationCode(IngressFormDTO ingressForm);

    Boolean updatePerson(Person person);

}
