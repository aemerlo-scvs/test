package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestAnnexeCancelaltionDto;
import com.scfg.core.domain.dto.RequestAnnexeSearchFiltersDto;
import com.scfg.core.domain.dto.RequestSaveVoucherPaymentDto;
import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import com.scfg.core.domain.common.AnnexeRequirementControl;
import com.scfg.core.domain.dto.vin.RequestAnnexeFileDocumentDTO;
import com.scfg.core.domain.dto.vin.UpdateRequestAnnexeDTO;

import java.text.ParseException;
import java.util.List;

public interface RequestAnnexeUseCase {
    List<AnnexeRequirementControl> processRequest(RequestAnnexeDTO requestAnnexeDTO);
    List<AnnexeRequirementDto> processRequest(UpdateRequestAnnexeDTO requestAnnexeDTO);
    RequestAnnexeFileDocumentDTO hasPendingRequests(Long policyId, Long annexeTypeId);
    PageableDTO getAllPageByAnnexeFilters(Integer page, Integer size, RequestAnnexeSearchFiltersDto filtersDto) throws ParseException;
    void processCancellationRequest(Long requestAnnexeId, RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto);
    Long validateVousherPayment(Long requestAnnexeId);
    void saveVoucherPayment(Long requestAnnexeId, RequestSaveVoucherPaymentDto requestSaveVoucherPaymentDto);
    void updateRequirements(RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto, Long requestAnnexeId);
}
