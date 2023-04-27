package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.JuridicalPersonDTO;

import java.util.List;

public interface JuridicalPersonUseCase {
    List<JuridicalPersonDTO> getAllJuridicalPerson(int assignedGroup);
}
