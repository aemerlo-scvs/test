package com.scfg.core.application.service;

import com.scfg.core.application.port.in.JuridicalPersonUseCase;
import com.scfg.core.application.port.out.JuridicalPersonPort;
import com.scfg.core.domain.dto.JuridicalPersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JuridicalPersonService implements JuridicalPersonUseCase {
    private final JuridicalPersonPort juridicalPersonPort;
    @Override
    public List<JuridicalPersonDTO> getAllJuridicalPerson(int assignedGroup) {
        return juridicalPersonPort.getAllJuridicalPerson(assignedGroup);
    }
}
