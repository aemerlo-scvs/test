package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.InactiveRequestDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.SearchRequestDTO;
import com.scfg.core.domain.dto.credicasas.ClfSearchRequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import com.scfg.core.domain.dto.credicasas.SearchRequestResponseDTO;

import java.util.List;

public interface GeneralRequestUseCase {

    List<RequestDetailDTO> getAllClfBySubscriptionFilters(ClfSearchRequestDTO searchRequestDTO);

    List<RequestDTO> getAllClfByFilters(PersonDTO personDTO);

    SearchRequestResponseDTO getPaginatedRequests(ClfSearchRequestDTO searchRequestDTO, Integer page, Integer size);

    Boolean inactiveRequest(InactiveRequestDTO requestDTO);

    Boolean isOperationNumberDuplicated(String operationNumber, Integer requestId);

    Boolean updateDataRequestGel(Integer companyIdc);

}
