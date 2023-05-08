package com.scfg.core.application.port.out;



import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestAnnexeSearchFiltersDto;
import com.scfg.core.domain.common.RequestAnnexe;

import java.text.ParseException;
import java.util.List;

public interface RequestAnnexePort {
    List<RequestAnnexe> findAllRequestByPolicyIdAndAnnexeTypeIdAndRequestStatus(Long policyId, Long annexeTypeId,
                                                                                List<Integer> requestAnnexeStatusList);
    PageableDTO findAllPageByFilters(RequestAnnexeSearchFiltersDto filtersDto, Integer page, Integer size) throws ParseException;

    RequestAnnexe findRequestAnnexeIdOrThrowExcepcion (Long requestAnnexeId);
    List<RequestAnnexe> getRequestByPolicyIdAndAnnexeTypeId(Long policyId, Long annexeTypeId);
    Long saveOrUpdate(RequestAnnexe requestAnnexe);
}
