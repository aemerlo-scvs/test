package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PepUseCase;
import com.scfg.core.application.port.out.PEPPort;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.Pep;
import com.scfg.core.domain.common.Role;
import com.scfg.core.domain.dto.SearchPepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PepService implements PepUseCase {

    private final PEPPort pepPort;

    @Override
    public List<Pep> getAll() {
        return pepPort.findAll();
    }

    @Override
    public List<Pep> getAllByKeyWord(String keyWord) {
        return pepPort.findAllByKeyWord(keyWord);
    }

    @Override
    public Object getAllByPage(int page, int size) {
        return pepPort.findAllByPage(page, size);
    }

    @Override
    public Boolean existsByIdentificationNumberOrName(SearchPepDTO pepDTO) {
        return pepPort.existsByIdentificationNumberOrName(pepDTO);
    }

    @Override
    public Boolean saveOrUpdate(Pep pep) {
        Pep auxPep = pepPort.saveOrUpdate(pep);
        return auxPep.getId() > 0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Override
    public Boolean saveOrUpdateAll(List<Pep> pepList) {
        List<Pep> listTemp = pepPort.findAll();
        pepList.forEach(pep -> {
            List<Pep> auxList = listTemp.stream().filter(pepTemp -> existsPep(pep, pepTemp)).collect(Collectors.toList());
            if (auxList.size() > 0) {
                pep.setId(auxList.get(0).getId());
            }
        });
        return pepPort.saveOrUpdateAll(pepList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotDataFoundException.class, Exception.class})
    @Override
    public Boolean delete(long pepId) {
        Pep pepAux = pepPort.findById(pepId);
        return pepPort.delete(pepAux);
    }

    //#region Auxiliary Methods
    String getPepFilters(Pep pep) {
        String queryFilter = "";

        if (pep.getIdentificationNumber() != null || !pep.getIdentificationNumber().isEmpty()) {
            queryFilter += " p.identificationNumber = " + "'" + pep.getIdentificationNumber().trim() + "' OR";
        }
        if (pep.getName() != null || !pep.getName().isEmpty()) {
            queryFilter += " (TRIM(p.name) LIKE " + "'%" + pep.getName().trim().toUpperCase() + "%' AND";
            queryFilter += " TRIM(p.lastName) LIKE " + "'%" + pep.getLastName().trim().toUpperCase() + "%' AND";
            queryFilter += " TRIM(p.motherLastName) LIKE " + "'%" + pep.getMotherLastName().trim().toUpperCase() + "%')";
        }
        return queryFilter;
    }

    Boolean existsPep(Pep pep, Pep pepToCompare) {
        boolean response = false;
        if ((!pep.getIdentificationNumber().equals("ND") &&
                pep.getIdentificationNumber().trim().equals(pepToCompare.getIdentificationNumber().trim())) ||
                (isCompleteNameEquals(pep, pepToCompare))) {
            response = true;
        }
        return response;
    }

    Boolean isCompleteNameEquals(Pep pep, Pep pepToCompare) {
        String completeName1 = pep.getName().trim().toUpperCase() + pep.getLastName().trim().toUpperCase() + pep.getMotherLastName();
        String completeName2 = pepToCompare.getName().trim().toUpperCase() + pepToCompare.getLastName().trim().toUpperCase() + pepToCompare.getMotherLastName();
        return completeName1.equals(completeName2);
    }

    //#endregion

}
