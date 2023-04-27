package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestProposalSearchFiltersDto;

import java.text.ParseException;

public interface ProposalUseCase {
    PageableDTO getAllByIdPlanPageAndProposalFilters(Integer page, Integer size, Long planId, RequestProposalSearchFiltersDto filtersDto) throws ParseException;
}
