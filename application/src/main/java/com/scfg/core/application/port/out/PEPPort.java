package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Pep;
import com.scfg.core.domain.dto.SearchPepDTO;

import java.util.List;

public interface PEPPort {

    Pep findById(long pepId);

    List<Pep> findAll();

    List<Pep> findAllByKeyWord(String keyWord);

    Object findAllByPage(int page, int size);

    Pep saveOrUpdate(Pep pep);

    Boolean saveOrUpdateAll(List<Pep> pep);

    Boolean delete(Pep pep);

    List<Pep> existsByIdentificationNumber(String filters); // Is deprecated

    Boolean existsByIdentificationNumberOrName(SearchPepDTO pepDTO);
    Boolean existsIdentificationNumber(String filters);

}
