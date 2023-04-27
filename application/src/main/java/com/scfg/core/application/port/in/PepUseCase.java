package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.Pep;
import com.scfg.core.domain.dto.SearchPepDTO;

import java.util.List;

public interface PepUseCase {

    List<Pep> getAll();

    List<Pep> getAllByKeyWord(String keyWord);

    Object getAllByPage(int page, int size);

    Boolean existsByIdentificationNumberOrName(SearchPepDTO pepDTO);

    Boolean saveOrUpdate(Pep pep);

    Boolean saveOrUpdateAll(List<Pep> pepList);

    Boolean delete(long pepId);
}
