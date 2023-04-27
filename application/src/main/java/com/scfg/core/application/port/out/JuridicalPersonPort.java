package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.JuridicalPersonDTO;

import java.util.List;

public interface JuridicalPersonPort {
    List<JuridicalPersonDTO> getAllJuridicalPerson(int assignedGroup);
}
